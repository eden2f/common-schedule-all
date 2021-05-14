package com.eden.common.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eden
 * @date 2021/1/4 10:41
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    /**
     * 任务状态 待执行, 运行中, 已取消, 已结束
     */
    NEW(0, "待执行"),

    RUNNING(1, "运行中"),

    CANCELED(2, "已取消"),

    FINISHED(3, "已结束");

    private final int status;

    private final String desc;

    private static final Map<Integer, TaskStatus> CACHE = new HashMap<>();


    static {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            CACHE.put(taskStatus.status, taskStatus);
        }
    }

    public static TaskStatus getByIntStatus(Integer status) {
        return CACHE.get(status);
    }
}
