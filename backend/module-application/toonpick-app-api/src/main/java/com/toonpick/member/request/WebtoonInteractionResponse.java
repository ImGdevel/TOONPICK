package com.toonpick.member.request;

import com.toonpick.domain.member.entity.MemberWebtoonInteraction;
import com.toonpick.domain.member.enums.WatchingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WebtoonInteractionResponse {

    private boolean liked;
    private boolean bookmarked;
    private boolean notificationEnabled;
    private WatchingStatus status;
    private int lastReadEpisode;
    private LocalDateTime lastReadAt;

    public static WebtoonInteractionResponse from(MemberWebtoonInteraction interaction) {
        return WebtoonInteractionResponse.builder()
                .liked(interaction.isLiked())
                .bookmarked(interaction.isBookmarked())
                .notificationEnabled(interaction.isNotificationEnabled())
                .status(interaction.getStatus())
                .lastReadEpisode(interaction.getLastReadEpisode())
                .lastReadAt(interaction.getLastReadAt())
                .build();
    }

    public static WebtoonInteractionResponse empty() {
        return WebtoonInteractionResponse.builder()
                .liked(false)
                .bookmarked(false)
                .notificationEnabled(false)
                .status(WatchingStatus.NONE)
                .lastReadEpisode(0)
                .lastReadAt(null)
                .build();
    }
}
