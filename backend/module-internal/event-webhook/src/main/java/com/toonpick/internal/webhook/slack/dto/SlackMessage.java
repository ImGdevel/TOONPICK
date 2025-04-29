package com.toonpick.internal.webhook.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlackMessage {
    private String text;
}
