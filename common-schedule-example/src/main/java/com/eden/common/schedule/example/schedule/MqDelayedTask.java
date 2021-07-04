package com.eden.common.schedule.example.schedule;

import com.eden.common.schedule.DelayedTask;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 发送MQ延时任务
 *
 * @author Eden
 * @date 2021/1/4 16:37
 */
@Slf4j
@ToString
public class MqDelayedTask implements DelayedTask {

    /**
     * 业务id
     */
    private final Long businessId;
    private final Integer delayedTime;
    private final String taskId;
    private final String retryContext;
    private final Integer maxExecutionTime;
    public static final String RETRY_CONTEXT_PREFIX = "MQ_";
    public static final String RETRY_CONTEXT_FORMAT = "MQ_%s";
    public static final String ID_FORMAT = "MQ_%s_%s";

    public MqDelayedTask(Long businessId, Integer delayedTime, Integer maxExecutionTime) {
        this.businessId = businessId;
        this.delayedTime = delayedTime;
        this.maxExecutionTime = maxExecutionTime;
        taskId = MqDelayedTask.getTaskIdByBusinessId(businessId);
        retryContext = MqDelayedTask.getRetryContextByBusinessId(businessId);
    }

    @Override
    public String getId() {
        return this.taskId;
    }

    @Override
    public long getDelay() {
        return delayedTime;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public String getRetryContext() {
        return this.retryContext;
    }

    @Override
    public Long getMaxExecutionSecond() {
        return this.maxExecutionTime.longValue();
    }


    public static String getRetryContextByBusinessId(Long businessId) {
        return String.format(RETRY_CONTEXT_FORMAT, businessId);
    }


    private static String getTaskIdByBusinessId(Long businessId) {
        // 如果发送并发问题, 建议换成全局唯一id
        return String.format(ID_FORMAT, businessId, System.currentTimeMillis());
    }

    public static Optional<Long> getBusinessIdByRetryContext(String retryContext) {
        if (StringUtils.isNotBlank(retryContext)) {
            if (retryContext.contains(RETRY_CONTEXT_PREFIX)) {
                String temp = retryContext.replace(RETRY_CONTEXT_PREFIX, "");
                return Optional.of(Long.valueOf(temp));
            }
        }
        return Optional.empty();
    }

    @Override
    public void run() {
        log.info("QcCompletedMqDelayedTask.run start businessId = {}", this.businessId);
        log.info("QcCompletedMqDelayedTask.run end businessId = {}", this.businessId);
    }


}
