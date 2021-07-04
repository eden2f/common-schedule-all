package com.eden.common.schedule.example.jpa.repository;

import com.eden.common.schedule.example.jpa.po.MqDelayedTaskPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eden
 * @date 2020/07/25
 */
public interface MqDelayedTaskRepository extends JpaRepository<MqDelayedTaskPo, Long> {

    List<MqDelayedTaskPo> findByStatus(int status);

    List<MqDelayedTaskPo> findByStatusIn(List<Integer> statusIn);

    List<MqDelayedTaskPo> findByRetryContextAndStatus(String retryContext, int status);

    MqDelayedTaskPo findByTaskId(String taskId);
}