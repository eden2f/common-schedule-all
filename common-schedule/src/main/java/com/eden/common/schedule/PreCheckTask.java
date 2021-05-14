package com.eden.common.schedule;

import com.eden.common.schedule.persistent.DelayedTaskDao;
import com.eden.common.schedule.persistent.DelayedTaskPo;
import lombok.extern.slf4j.Slf4j;

/**
 * 延时任务前置校验
 *
 * @author Eden
 * @date 2021/1/4 11:01
 */
@Slf4j
public class PreCheckTask implements Runnable {


    private final DelayedTask delayedTask;

    private final DelayedTaskDao delayedTaskDao;

    public PreCheckTask(DelayedTask delayedTask, DelayedTaskDao delayedTaskDao) {
        this.delayedTask = delayedTask;
        this.delayedTaskDao = delayedTaskDao;
    }

    @Override
    public void run() {
        try {
            DelayedTaskPo task = this.delayedTaskDao.findByTaskId(delayedTask.getId());
            if (task == null) {
                log.info("Can't execute task: {}, not find", delayedTask.getId());
                return;
            }
            TaskStatus status = task.getStatus();
            if (status == TaskStatus.NEW) {
                task.setStatus(TaskStatus.RUNNING);
                this.delayedTaskDao.saveOrUpdate(task);
                log.info("Start execute delayed task: {}", task);
                delayedTask.run();
                task.setStatus(TaskStatus.FINISHED);
                this.delayedTaskDao.saveOrUpdate(task);
                log.info("Finish execute delayed task: {}", task);
            } else {
                log.info("Can't execute task: {}, dut to status: {}", delayedTask.getId(), status);
            }

        } catch (Exception e) {
            log.error("Execute task: {} error", delayedTask.getId(), e);
        }
    }
}
