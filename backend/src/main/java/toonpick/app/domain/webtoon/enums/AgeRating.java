package toonpick.app.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgeRating {

	ALL(0),
	AGE_12(12),
	AGE_15(15),
	ADULT(19);

	private final int value;
}
