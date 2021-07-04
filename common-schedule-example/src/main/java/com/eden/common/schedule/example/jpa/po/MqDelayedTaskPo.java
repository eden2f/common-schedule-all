package com.eden.common.schedule.example.jpa.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "t_mq_delayed_task", indexes = {@Index(columnList = "taskId"), @Index(columnList = "retryContext")})
public class MqDelayedTaskPo {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 256, nullable = false)
    private String taskId;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Date createdTime;

    @Column(nullable = false)
    private Date updatedTime;

    @Column(nullable = false)
    private Date executeTime;

    @Column(length = 256, nullable = false)
    private String retryContext;

    @Column(nullable = false)
    private Long maxExecutionSecond;

}