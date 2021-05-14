package com.eden.common.schedule;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 延时触发调度器，基于JDK ScheduledExecutorService
 *
 * @author Eden
 * @date 2020/12/31 14:53
 * @see ScheduledExecutorService
 */
public interface DelayedScheduler {


    /**
     * 延时触发调度器初始化
     */
    void init();

    /**
     * 提交延时任务
     *
     * @param delayedTask 待执行任务
     */
    void submit(DelayedTask delayedTask);


    /**
     * 取消延时任务
     *
     * @param taskId                任务id
     * @param mayInterruptIfRunning 是否需要打断如果正在运行中 这个还没实现
     */
    void cancel(String taskId, boolean mayInterruptIfRunning);


    /**
     * 延时触发调度器关闭
     */
    void shutdown();


    /**
     * 延时触发调度器强制关闭
     */
    void shutdownNow();


    /**
     * 设置延时任务重试监听器
     *
     * @param delayedTaskRetryListener 重试任务监听器
     */
    void setDelayedTaskRetryListener(DelayedTaskRetryListener delayedTaskRetryListener);

}
