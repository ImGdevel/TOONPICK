package com.toonpick.internal.webhook.slack;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SlackNotifier {

    final private Logger logger = LoggerFactory.getLogger(SlackNotifier.class);

    @Value("${spring.event.webhook.slack.url}")
    private String slackWebhookUrl;

    final private RestTemplate restTemplate = new RestTemplate();

    public void send(String message) {
        try {
            Map<String, String> payload = Map.of("text", message);
            restTemplate.postForEntity(slackWebhookUrl, payload, String.class);
        } catch (Exception e) {
            logger.error("Slack 메시지 전송 실패", e);
        }
    }
}
