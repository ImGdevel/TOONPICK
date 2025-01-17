package toonpick.app.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 1xxx : Standard error
    UNKNOWN_ERROR(1000, "Unknown error occurred"),
    INVALID_INPUT(1001, "Invalid input provided"),
    PERMISSION_DENIED(1002, "Permission denied"),

    // 2xxx : security error


    // 22xx : jwt error
    ACCESS_TOKEN_NOT_FOUND(2221, "access token not found"),
    REFRESH_TOKEN_NOT_FOUND(2222,"refresh token not found"),
    INVALID_ACCESS_TOKEN(2223,"invalid access token"),
    INVALID_REFRESH_TOKEN(2224,"invalid refresh token"),
    EXPIRED_ACCESS_TOKEN(2225,"access token expired"),
    EXPIRED_REFRESH_TOKEN(2226,"refresh token expired"),

    // 4xxx : Database error
    DATABASE_ERROR(4000, "General database error"),
    DATA_ACCESS_ERROR(4001, "Error accessing data"),
    DATA_SAVE_ERROR(4002, "Error saving data"),
    DATA_DELETE_ERROR(4003, "Error deleting data"),

    // 44xx : Not found errors
    MEMBER_NOT_FOUND(4400, "Member not found"),
    WEBTOON_NOT_FOUND(4410, "Webtoon not found"),
    AUTHOR_NOT_FOUND(4411, "Author not found"),
    GENRE_NOT_FOUND(4412, "Genre not found"),
    REVIEW_NOT_FOUND(4430, "Review not found"),
    COLLECTION_NOT_FOUND(4443, "Collection not found"),
    BOOKMARK_TOON_NOT_FOUND(4444, "Bookmark toon not found"),
    WATCHING_TOON_NOT_FOUND(4445, "Watching toon not found"),
    FAVORITE_TOON_NOT_FOUND(4446, "Favorite toon not found"),

    // 45xx : Already exists errors
    MEMBER_ALREADY_EXISTS(4500, "Member already exists"),
    WEBTOON_ALREADY_EXISTS(4510, "Webtoon already exists"),
    AUTHOR_ALREADY_EXISTS(4511, "Author already exists"),
    GENRE_ALREADY_EXISTS(4512, "Genre already exists"),
    REVIEW_ALREADY_EXISTS(4530, "Review already exists"),
    COLLECTION_ALREADY_EXISTS(4543, "Collection already exists"),
    BOOKMARK_TOON_ALREADY_EXISTS(4544, "Bookmark toon already exists"),
    WATCHING_TOON_ALREADY_EXISTS(4545, "Watching toon already exists"),
    FAVORITE_TOON_ALREADY_EXISTS(4546, "Favorite toon already exists"),

    // 45xx : Custom errors
    LIKE_COUNT_UPDATE_ERROR(4500, "Error updating like count for review");

    private final int code;
    private final String message;
}
