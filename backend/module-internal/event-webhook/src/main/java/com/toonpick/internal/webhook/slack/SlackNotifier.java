package com.toonpick.internal.webhook.slack;

import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class SlackNotifier {

    final private Logger logger = LoggerFactory.getLogger(SlackNotifier.class);

    private final WebClient webClient;

    public void send(String message) {
        SlackMessage payload = new SlackMessage(message);
        try {
            webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> logger.error(ErrorCode.SLACK_SEND_FAIL.getMessage()))
                .subscribe();
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.SLACK_SEND_FAIL.getMessage());
        }
    }
}
