package com.toonpick.member.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastReadUpdateRequest {
    private Integer episode;
    private LocalDateTime timestamp;
}
