package com.eden.common.schedule;


import com.eden.common.schedule.persistent.DelayedTaskDao;
import com.eden.common.schedule.persistent.DelayedTaskPo;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Eden
 * @date 2020/12/31 15:02
 */
@Slf4j
public class TaskPersistentScheduler implements DelayedScheduler {

    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);

    private final DelayedTaskDao delayedTaskDao;

    private boolean retryEnable = true;

    /**
     * 默认重试监听器
     */
    private final DelayedTaskRetryListener defaultDelayedTaskRetryListener = (taskId, retryContext) -> log.info("None task retry,id= {}, retryContext= {}", taskId, retryContext);

    private DelayedTaskRetryListener delayedTaskRetryListener = defaultDelayedTaskRetryListener;


    public TaskPersistentScheduler(DelayedTaskDao delayedTaskDao) {
        this.delayedTaskDao = delayedTaskDao;
    }

    public TaskPersistentScheduler(DelayedTaskDao delayedTaskDao, boolean retryEnable, DelayedTaskRetryListener delayedTaskRetryListener) {
        this.retryEnable = retryEnable;
        this.delayedTaskDao = delayedTaskDao;
        this.delayedTaskRetryListener = delayedTaskRetryListener;
    }

    @Override
    public void init() {
        log.info("TaskPersistentScheduler.init start, retryEnable = {}", retryEnable);
        if (retryEnable) {
            List<DelayedTaskPo> tasks = this.delayedTaskDao.selectByStatus(TaskStatus.NEW);
            for (DelayedTaskPo task : tasks) {
                if (task.getExecuteTime().before(new Date())) {
                    delayedTaskRetryListener.onRetry(task.getTaskId(), task.getRetryContext());
                    task.setStatus(TaskStatus.FINISHED);
                    delayedTaskDao.saveOrUpdate(task);
                } else {
                    DelayedTask delayedTask = new DelayedTask() {
                        @Override
                        public String getId() {
                            return task.getTaskId();
                        }

                        @Override
                        public long getDelay() {
                            return System.currentTimeMillis() - task.getExecuteTime().getTime();
                        }

                        @Override
                        public TimeUnit getTimeUnit() {
                            return TimeUnit.MILLISECONDS;
                        }

                        @Override
                        public String getRetryContext() {
                            return task.getRetryContext();
                        }

                        @Override
                        public void run() {
                            delayedTaskRetryListener.onRetry(task.getTaskId(), task.getRetryContext());
                            task.setStatus(TaskStatus.FINISHED);
                            delayedTaskDao.saveOrUpdate(task);
                        }
                    };
                    if (!scheduledExecutorService.isShutdown()) {
                        scheduledExecutorService.schedule(new PreCheckTask(delayedTask, delayedTaskDao),
                                System.currentTimeMillis() - task.getExecuteTime().getTime(), TimeUnit.MILLISECONDS);
                    } else {
                        log.info("Can't schedule execute task: {}, dut to scheduledExecutorService isTerminated", delayedTask.getId());
                    }
                }
            }
        }
        log.info("TaskPersistentScheduler.init start, end = {}", retryEnable);
    }

    @Override
    public void submit(DelayedTask delayedTask) {
        PreCheckTask preCheckTask = new PreCheckTask(delayedTask, delayedTaskDao);
        //持久化任务
        DelayedTaskPo task = new DelayedTaskPo();
        task.setTaskId(delayedTask.getId());
        task.setStatus(TaskStatus.NEW);
        task.setCreatedTime(new Date());
        task.setUpdatedTime(new Date());
        task.setRetryContext(delayedTask.getRetryContext());
        //设置延时执行的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MILLISECOND, (int) delayedTask.getTimeUnit().toMillis(delayedTask.getDelay()));
        task.setExecuteTime(calendar.getTime());
        delayedTaskDao.saveOrUpdate(task);
        log.info("Submit delayed task: {}", delayedTask);
        //提交延时执行任务
        if (!scheduledExecutorService.isShutdown()) {
            ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(preCheckTask,
                    delayedTask.getDelay(), delayedTask.getTimeUnit());
        } else {
            log.info("Can't schedule execute task: {}, dut to scheduledExecutorService isTerminated", delayedTask.getId());
        }
    }

    @Override
    public void cancel(String taskId, boolean mayInterruptIfRunning) {
        DelayedTaskPo task = delayedTaskDao.findByTaskId(taskId);
        if (task == null) {
            log.error("Cancel delayed task:{} not find", taskId);
            return;
        }
        if (task.getStatus() == TaskStatus.NEW) {
            task.setStatus(TaskStatus.CANCELED);
            //需要带条件更新
            delayedTaskDao.saveOrUpdate(task);
            log.error("Cancel delayed task:{} success, due to status: {}", task.getTaskId(), task.getStatus());
        } else {
            log.error("Cancel delayed task:{} failed, due to status: {}", task.getTaskId(), task.getStatus());
        }
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        log.info("TaskPersistentScheduler.shutdown");
    }

    @Override
    public void setDelayedTaskRetryListener(DelayedTaskRetryListener delayedTaskRetryListener) {
        this.delayedTaskRetryListener = delayedTaskRetryListener;
    }

    @Override
    public void shutdownNow() {
        scheduledExecutorService.shutdownNow();
        log.info("TaskPersistentScheduler.shutdownNow");
    }

}
