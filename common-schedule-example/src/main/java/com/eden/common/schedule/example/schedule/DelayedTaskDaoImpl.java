package com.eden.common.schedule.example.schedule;

import com.eden.common.schedule.TaskStatus;
import com.eden.common.schedule.example.jpa.po.MqDelayedTaskPo;
import com.eden.common.schedule.example.jpa.repository.MqDelayedTaskRepository;
import com.eden.common.schedule.persistent.DelayedTaskDao;
import com.eden.common.schedule.persistent.DelayedTaskPo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eden
 * @date 2021/5/13 15:03
 */
@Service
public class DelayedTaskDaoImpl implements DelayedTaskDao {

    @Resource
    private MqDelayedTaskRepository mqDelayedTaskRepository;

    @Override
    public void saveOrUpdate(DelayedTaskPo delayedTask) {
        MqDelayedTaskPo mqDelayedTaskPo = mqDelayedTaskRepository.findByTaskId(delayedTask.getTaskId());
        if (mqDelayedTaskPo == null) {
            mqDelayedTaskPo = new MqDelayedTaskPo();
        }
        BeanUtils.copyProperties(delayedTask, mqDelayedTaskPo);
        mqDelayedTaskPo.setStatus(delayedTask.getStatus().getStatus());
        mqDelayedTaskPo.setUpdatedTime(new Date());
        mqDelayedTaskRepository.save(mqDelayedTaskPo);
    }

    @Override
    public DelayedTaskPo findByTaskId(String taskId) {
        MqDelayedTaskPo mqDelayedTaskPo = mqDelayedTaskRepository.findByTaskId(taskId);
        return getDelayedTaskPoByMqDelayedTaskPo(mqDelayedTaskPo);
    }

    private DelayedTaskPo getDelayedTaskPoByMqDelayedTaskPo(MqDelayedTaskPo mqDelayedTaskPo) {
        if (mqDelayedTaskPo == null) {
            return null;
        }
        DelayedTaskPo delayedTaskPo = new DelayedTaskPo();
        BeanUtils.copyProperties(mqDelayedTaskPo, delayedTaskPo);
        delayedTaskPo.setStatus(TaskStatus.getByIntStatus(mqDelayedTaskPo.getStatus()));
        return delayedTaskPo;
    }


    @Override
    public List<DelayedTaskPo> selectByStatus(TaskStatus taskStatus) {
        return mqDelayedTaskRepository.findByStatus(taskStatus.getStatus())
                .stream().map(this::getDelayedTaskPoByMqDelayedTaskPo).collect(Collectors.toList());
    }

    @Override
    public List<DelayedTaskPo> selectByStatusIn(List<TaskStatus> taskStatusIn) {
        return mqDelayedTaskRepository.findByStatusIn(taskStatusIn.stream().map(TaskStatus::getStatus).collect(Collectors.toList()))
                .stream().map(this::getDelayedTaskPoByMqDelayedTaskPo).collect(Collectors.toList());
    }

    public List<DelayedTaskPo> findByRetryContext(String retryContext, int status) {
        return mqDelayedTaskRepository.findByRetryContextAndStatus(retryContext, status)
                .stream().map(this::getDelayedTaskPoByMqDelayedTaskPo).collect(Collectors.toList());
    }
}
