package com.toonpick.internal.webhook.slack.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.event.webhook.slack")
public class SlackProperties {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }
}
