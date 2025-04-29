package com.toonpick.internal.webhook.slack.aspect;

import com.toonpick.internal.webhook.slack.annotation.NotifyJobResult;
import com.toonpick.internal.webhook.slack.enums.AlertLevel;
import com.toonpick.internal.webhook.slack.notifier.SlackNotifier;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JobResultAop {

    private final SlackNotifier slackNotifier;

    @Around("@annotation(notifyJobResult)")
    public Object around(ProceedingJoinPoint pjp, NotifyJobResult notifyJobResult) throws Throwable {
        String jobName = notifyJobResult.jobName();
        AlertLevel level = notifyJobResult.level();

        try {
            Object result = pjp.proceed();
            slackNotifier.send("작업 성공: " + jobName, level);
            return result;
        } catch (Throwable e) {
            slackNotifier.send("작업 실패: " + jobName + "\n오류: " + e.getMessage(), AlertLevel.CRITICAL);
            throw e;
        }
    }
}
