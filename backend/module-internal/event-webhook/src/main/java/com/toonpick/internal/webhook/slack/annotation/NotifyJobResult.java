package com.toonpick.internal.webhook.slack.annotation;

import com.toonpick.internal.webhook.slack.enums.AlertLevel;

import java.lang.annotation.*;

/**
 * @NotifyJobResult 어노테이션을 통해 슬랙으로 작업 결과를 알립니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyJobResult {
    /**
     * 작업 이름을 지정합니다.
     */
    String jobName();

    /**
     * 알림 레벨을 지정합니다. (기본값: INFO)
     */
    AlertLevel level() default AlertLevel.INFO;
}
