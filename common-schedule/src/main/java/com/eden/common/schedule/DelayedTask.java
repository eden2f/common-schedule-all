package com.eden.common.schedule;

import java.util.concurrent.TimeUnit;

/**
 * 延时任务基类
 *
 * @author Eden
 * @date 2020/12/31 14:54
 */
public interface DelayedTask extends Runnable {

    /**
     * 无业务主键
     *
     * @return 任务id
     */
    String getId();

    /**
     * @return 延时时长
     */
    long getDelay();

    /**
     * @return 延时时间单位
     */
    TimeUnit getTimeUnit();

    /**
     * 外键 用于业务重试
     *
     * @return 重试标识
     */
    String getRetryContext();

}
