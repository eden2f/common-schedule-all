package com.eden.common.schedule.example.web;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author Eden
 * @date 2021/5/13 21:33
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DelayTaskAddCancel {

    @NotNull(message = "businessId不能为空")
    private Long businessId;
}
