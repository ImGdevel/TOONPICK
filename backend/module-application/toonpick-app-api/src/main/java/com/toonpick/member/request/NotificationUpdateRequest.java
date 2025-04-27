package com.toonpick.member.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationUpdateRequest {
    private boolean enabled;
}
