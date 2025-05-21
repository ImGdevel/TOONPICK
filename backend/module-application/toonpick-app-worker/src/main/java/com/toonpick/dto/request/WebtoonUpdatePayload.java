package com.toonpick.dto.request;


import com.toonpick.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WebtoonUpdatePayload {
    private Long id;

    private String platform;

    private String url;

}
