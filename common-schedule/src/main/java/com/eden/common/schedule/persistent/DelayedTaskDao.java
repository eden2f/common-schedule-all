package com.eden.common.schedule.persistent;

import com.eden.common.schedule.TaskStatus;

import java.util.List;

/**
 * 延时任务持久化DAO接口
 *
 * @author Eden
 * @date 2021/1/4 10:26
 */
public interface DelayedTaskDao {

    /**
     * 新增或保存一个延时任务
     *
     * @param delayedTask 延时任务对象
     */
    void saveOrUpdate(DelayedTaskPo delayedTask);

    /**
     * 根据任务id查询延时任务
     *
     * @param taskId 任务id
     * @return 延时任务对象
     */
    DelayedTaskPo findByTaskId(String taskId);

    /**
     * 根据任务状态批量查询
     *
     * @param taskStatus 任务状态
     * @return 延时任务对象列表
     */
    List<DelayedTaskPo> selectByStatus(TaskStatus taskStatus);

    /**
     * 根据任务状态批量查询
     *
     * @param taskStatusIn 任务状态
     * @return 延时任务对象列表
     */
    List<DelayedTaskPo> selectByStatusIn(List<TaskStatus> taskStatusIn);

}
