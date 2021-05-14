package com.eden.common.schedule.example.schedule;

import com.eden.common.schedule.DelayedScheduler;
import com.eden.common.schedule.TaskPersistentScheduler;
import com.eden.common.schedule.persistent.DelayedTaskDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eden
 * @date 2021/1/9 13:54
 */
@Configuration
@Slf4j
public class DelayTaskConfiguration {

    @Bean(destroyMethod = "shutdown")
    public DelayedScheduler delayedScheduler(DelayedTaskDao delayedTaskDao,
                                             MqDelayedTaskRetryListenerImpl mqDelayedTaskRetryListener) {
        TaskPersistentScheduler scheduler = new TaskPersistentScheduler(delayedTaskDao, true, mqDelayedTaskRetryListener);
        scheduler.init();
        return scheduler;
    }
}
