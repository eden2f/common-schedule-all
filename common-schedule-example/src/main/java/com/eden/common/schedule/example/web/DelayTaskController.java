package com.eden.common.schedule.example.web;

import com.eden.common.schedule.DelayedScheduler;
import com.eden.common.schedule.TaskStatus;
import com.eden.common.schedule.example.schedule.DelayedTaskDaoImpl;
import com.eden.common.schedule.example.schedule.MqDelayedTask;
import com.eden.common.schedule.persistent.DelayedTaskPo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Eden
 * @date 2021/5/13 21:28
 */
@RestController
@RequestMapping("delayTask")
public class DelayTaskController {

    @Resource
    private DelayedScheduler delayedScheduler;
    @Resource
    private DelayedTaskDaoImpl delayedTaskDao;

    @PostMapping("submit")
    @ApiOperation("提交一个延时任务")
    public RetResult<Void> submit(@RequestBody @Valid DelayTaskAddReq req) {
        MqDelayedTask mqDelayedTask = new MqDelayedTask(req.getBusinessId(), req.getDelayedTime());
        delayedScheduler.submit(mqDelayedTask);
        return RetResult.success();
    }


    @PostMapping("cancel")
    @ApiOperation("取消一个延时任务")
    public RetResult<Void> cancel(@RequestBody @Valid DelayTaskAddCancel req) {
        String retryContext = MqDelayedTask.getRetryContextByBusinessId(req.getBusinessId());
        List<DelayedTaskPo> delayedTasks = delayedTaskDao.findByRetryContext(retryContext, TaskStatus.NEW.getStatus());
        for (DelayedTaskPo delayedTask : delayedTasks) {
            delayedScheduler.cancel(delayedTask.getTaskId(), false);
        }
        return RetResult.success();
    }


    @PostMapping("shutdown")
    @ApiOperation("停止处理延时任务")
    public RetResult<Void> shutdown() {
        delayedScheduler.shutdown();
        return RetResult.success();
    }


    @PostMapping("shutdownNow")
    @ApiOperation("强制停止处理延时任务")
    public RetResult<Void> shutdownNow() {
        delayedScheduler.shutdownNow();
        return RetResult.success();
    }
}
