package com.toonpick.internal.webhook.slack.notifier;

import com.toonpick.internal.webhook.slack.dto.SlackMessage;
import com.toonpick.internal.webhook.slack.enums.AlertLevel;
import com.toonpick.common.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class SlackNotifier {

    private final Logger logger = LoggerFactory.getLogger(SlackNotifier.class);
    private final WebClient webClient;

    public void send(String message, AlertLevel level) {
        SlackMessage slackMessage = formatMessage(message, level);

        try {
            webClient.post()
                .bodyValue(slackMessage)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> logger.error(ErrorCode.SLACK_SEND_FAIL.getMessage()))
                .subscribe();
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.SLACK_SEND_FAIL.getMessage());
        }
    }

    private SlackMessage formatMessage(String message, AlertLevel level) {
        String prefix = switch (level) {
            case INFO     -> "[알림] ";
            case WARNING  -> "[경고] ";
            case CRITICAL -> "[긴급] ";
        };
        return new SlackMessage(prefix + message);
    }

}
