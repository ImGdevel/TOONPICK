package toonpick.app.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error 코드 정의
 * 유의 : Error 코드 작성시 [타깃] [문제/원인] 컨벤션을 따를 것
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 1xxx : Standard error
    UNKNOWN_ERROR(1000, "Unknown error occurred"),
    INVALID_INPUT(1001, "Invalid input provided"),
    PERMISSION_DENIED(1002, "Permission denied"),

    // 2xxx : Security error
    AUTHENTICATION_FAILED(2000, "Authentication failed"),
    INVALID_CREDENTIALS(2001, "Invalid username or password"),
    INVALID_JSON_FORMAT(2002, "Invalid JSON format in request"),
    REQUEST_BODY_READ_ERROR(2003, "Failed to read authentication request body"),

    // 21xx : authorization error
    USER_ALREADY_REGISTERED(2103, "User is already registered"),
    USER_NOT_AUTHENTICATED(2110, "User is not authenticated"),

    // 22xx : JWT error
    ACCESS_TOKEN_MISSING(2221, "Access token is missing"),
    REFRESH_TOKEN_MISSING(2222, "Refresh token is missing"),
    ACCESS_TOKEN_INVALID(2223, "Access token is invalid"),
    REFRESH_TOKEN_INVALID(2224, "Refresh token is invalid"),
    ACCESS_TOKEN_EXPIRED(2225, "Access token has expired"),
    EXPIRED_REFRESH_TOKEN(2226, "Refresh token has expired"),
    REFRESH_TOKEN_NOT_FOUND(2227, "Refresh token not found in cache"),

    // 3xxx : API/Service error

    // 32xx : image
    IMAGE_UPLOAD_FAILED( 3230,"Failed to upload file"),
    IMAGE_UPLOAD_FAILED_TO_S3( 3231,"Failed to upload file to S3"),

    // 4xxx : Database error
    DATABASE_ERROR(4000, "Database error occurred"),
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
