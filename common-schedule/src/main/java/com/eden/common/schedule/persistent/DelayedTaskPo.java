package com.eden.common.schedule.persistent;

import com.eden.common.schedule.TaskStatus;
import lombok.*;

import java.util.Date;

/**
 * @author Eden
 * @date 2021/1/4 11:24
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DelayedTaskPo {

    private String taskId;

    private TaskStatus status;

    private Date createdTime;

    private Date updatedTime;

    private Date executeTime;

    private String retryContext;

    private Long maxExecutionSecond;
}
