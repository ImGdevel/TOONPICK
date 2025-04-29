package com.toonpick.internal.webhook.slack;

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
}
