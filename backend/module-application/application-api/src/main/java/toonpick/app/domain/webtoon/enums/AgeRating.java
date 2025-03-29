package toonpick.app.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgeRating {
	ALL,
	AGE_12,
	AGE_15,
	ADULT
}
