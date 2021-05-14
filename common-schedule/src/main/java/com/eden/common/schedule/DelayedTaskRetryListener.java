package com.eden.common.schedule;

/**
 * 延时任务重试监听器
 *
 * @author Eden
 * @date 2021/1/4 13:55
 */
public interface DelayedTaskRetryListener {

    /**
     * 执行重试
     *
     * @param taskId       根据id找到需要重试的延时任务
     * @param retryContext 根据重试找到需要重试的延时任务
     */
    void onRetry(String taskId, String retryContext);
}
