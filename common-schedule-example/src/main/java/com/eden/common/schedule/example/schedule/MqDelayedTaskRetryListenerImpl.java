package com.eden.common.schedule.example.schedule;

import com.eden.common.schedule.DelayedTaskRetryListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Eden
 * @date 2021/1/4 16:32
 */
@Slf4j
@Service
public class MqDelayedTaskRetryListenerImpl implements DelayedTaskRetryListener {

    @Override
    public void onRetry(String taskId, String retryContext) {
        log.info("MqDelayedTaskRetryListenerImpl.onRetry start taskId = {}, retryContext = {}", taskId, retryContext);
        log.info("MqDelayedTaskRetryListenerImpl.onRetry end ");
    }
}
