package com.toonpick.type;

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

    // === 100 : Standard Error ===
    UNKNOWN_ERROR(100, "알 수 없는 오류가 발생했습니다"),
    INVALID_INPUT(101, "잘못된 입력 값입니다"),
    PERMISSION_DENIED(102, "권한이 없습니다"),
    MISSING_REQUIRED_FIELD(103, "필수 입력 항목이 누락되었습니다"),
    INVALID_INPUT_LENGTH(104, "입력 길이가 허용 범위를 초과했습니다"),
    REQUEST_BODY_READ_ERROR(105, "요청 본문을 읽는 데 실패했습니다"),

    // === 200 : Security Error ===
    AUTHENTICATION_FAILED(200, "인증에 실패했습니다"),
    INVALID_CREDENTIALS(201, "아이디 또는 비밀번호가 올바르지 않습니다"),
    INVALID_JSON_FORMAT(202, "요청 본문의 JSON 형식이 올바르지 않습니다"),
    INVALID_OAUTH_PROVIDER(203, "지원하지 않는 OAuth2 제공자입니다."),
    MISMATCH_OAUTH_PROVIDER_ID(204, "OAuth 제공자 ID가 일치하지 않습니다"),

    // === 210 : authorization Error ===
    USER_NOT_AUTHENTICATED(210, "사용자가 인증되지 않았습니다"),
    LOGIN_FAILED(211, "아이디 또는 비밀번호가 일치하지 않습니다."),
    OAUTH2_LOGIN_FAILED(212, "OAuth2 로그인에 실패했습니다."),
    USER_ALREADY_REGISTERED(213, "이미 가입된 사용자입니다"),
    EMAIL_NOT_VERIFIED(214, "이메일 인증이 완료되지 않았습니다"),
    ADULT_VERIFICATION_REQUIRED(215, "성인 인증이 필요합니다"),

    // === 220 : Token Error ===
    INVALID_AUTH_TOKEN(220, "유효하지 않은 인증 토큰입니다."),
    ACCESS_TOKEN_MISSING(221, "액세스 토큰이 존재하지 않습니다"),
    REFRESH_TOKEN_MISSING(222, "리프레시 토큰이 존재하지 않습니다"),
    ACCESS_TOKEN_INVALID(223, "액세스 토큰이 유효하지 않습니다"),
    REFRESH_TOKEN_INVALID(224, "리프레시 토큰이 유효하지 않습니다"),
    ACCESS_TOKEN_EXPIRED(225, "액세스 토큰이 만료되었습니다"),
    EXPIRED_REFRESH_TOKEN(226, "리프레시 토큰이 만료되었습니다"),
    REFRESH_TOKEN_NOT_FOUND(227, "리프레시 토큰을 찾을 수 없습니다"),

    // === 230 : Registration Error ===
    DUPLICATE_EMAIL(220, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(221, "이미 존재하는 닉네임입니다."),
    PASSWORD_TOO_WEAK(222, "비밀번호가 보안 정책에 부합하지 않습니다."),
    EMAIL_VERIFICATION_FAILED(223, "이메일 인증에 실패했습니다."),

    // === 240-250 : Validation error ===
    INVALID_EMAIL_FORMAT(240, "이메일 형식이 올바르지 않습니다"),
    INVALID_PASSWORD_FORMAT(241, "비밀번호는 8자 이상이며 문자, 숫자, 특수문자를 포함해야 합니다"),
    UNSUPPORTED_PASSWORD_CHARACTER(242, "지원하지 않는 문자가 비밀번호에 포함되어 있습니다"),
    PASSWORD_TOO_SHORT(243, "비밀번호는 최소 8자 이상이어야 합니다"),
    PASSWORD_TOO_LONG(245, "비밀번호는 최대 30자까지 가능합니다"),
    PASSWORD_MISSING_UPPERCASE(246, "비밀번호에 대문자가 포함되어야 합니다"),
    PASSWORD_MISSING_LOWERCASE(247, "비밀번호에 소문자가 포함되어야 합니다"),
    PASSWORD_MISSING_DIGIT(248, "비밀번호에 숫자가 포함되어야 합니다"),
    PASSWORD_MISSING_SYMBOL(249, "비밀번호에 특수문자가 포함되어야 합니다"),
    PASSWORD_SAME_AS_PREVIOUS(250, "이전 비밀번호와 다른 비밀번호를 사용해야 합니다"),
    INVALID_NICKNAME_FORMAT(251, "닉네임 형식이 올바르지 않습니다"),
    NICKNAME_TOO_SHORT(252, "닉네임은 최소 2자 이상이어야 합니다"),
    NICKNAME_TOO_LONG(253, "닉네임은 최대 30자까지 가능합니다"),
    NICKNAME_CONTAINS_FORBIDDEN_WORD(254, "부적절한 단어가 포함된 닉네임입니다"),


    // === 300 : Infra/Client/API/Service error ===

    // === 310 : Slack Webhook
    SLACK_INVALID_URL(310, "Slack Webhook URL이 유효하지 않습니다"),
    SLACK_SEND_FAIL(311, "Slack 메시지 전송에 실패했습니다"),
    SLACK_PAYLOAD_FORMAT_ERROR(312, "Slack 메시지 페이로드 형식이 올바르지 않습니다"),
    SLACK_NOT_CONFIGURED(313, "Slack 설정이 존재하지 않거나 누락되었습니다"),
    SLACK_RESPONSE_ERROR(314, "Slack 서버에서 오류 응답을 반환했습니다"),

    // === 320 : S3 Error ===
    INVALID_IMAGE_FILE(320, "잘못된 이미지 파일입니다"),
    IMAGE_UPLOAD_FAILED(321, "이미지 업로드에 실패했습니다"),
    IMAGE_UPLOAD_FAILED_TO_S3(322, "S3 업로드에 실패했습니다"),

    // === 400 : Database error ===
    DATABASE_ERROR(400, "데이터베이스 오류가 발생했습니다"),
    DATA_ACCESS_ERROR(401, "데이터 접근 중 오류가 발생했습니다"),
    DATA_SAVE_ERROR(402, "데이터 저장 중 오류가 발생했습니다"),
    DATA_DELETE_ERROR(403, "데이터 삭제 중 오류가 발생했습니다"),

    // 440 : Not found errors
    MEMBER_NOT_FOUND(440, "회원을 찾을 수 없습니다"),
    WEBTOON_NOT_FOUND(441, "웹툰을 찾을 수 없습니다"),
    AUTHOR_NOT_FOUND(412, "작가를 찾을 수 없습니다"),
    GENRE_NOT_FOUND(413, "장르를 찾을 수 없습니다"),
    REVIEW_NOT_FOUND(444, "리뷰를 찾을 수 없습니다"),
    COLLECTION_NOT_FOUND(445, "컬렉션을 찾을 수 없습니다"),
    BOOKMARK_TOON_NOT_FOUND(446, "북마크한 웹툰을 찾을 수 없습니다"),
    WATCHING_TOON_NOT_FOUND(447, "시청 중인 웹툰을 찾을 수 없습니다"),
    FAVORITE_TOON_NOT_FOUND(448, "즐겨찾는 웹툰을 찾을 수 없습니다"),

    // 450 : Already exists errors
    MEMBER_ALREADY_EXISTS(450, "이미 존재하는 회원입니다"),
    WEBTOON_ALREADY_EXISTS(451, "이미 존재하는 웹툰입니다"),
    AUTHOR_ALREADY_EXISTS(452, "이미 존재하는 작가입니다"),
    GENRE_ALREADY_EXISTS(453, "이미 존재하는 장르입니다"),
    REVIEW_ALREADY_EXISTS(454, "이미 작성된 리뷰입니다"),
    COLLECTION_ALREADY_EXISTS(455, "이미 존재하는 컬렉션입니다"),
    BOOKMARK_TOON_ALREADY_EXISTS(456, "이미 북마크한 웹툰입니다"),
    WATCHING_TOON_ALREADY_EXISTS(457, "이미 시청 중인 웹툰입니다"),
    FAVORITE_TOON_ALREADY_EXISTS(458, "이미 즐겨찾기에 추가된 웹툰입니다"),

    // 46xx : Custom errors
    LIKE_COUNT_UPDATE_ERROR(460, "리뷰 좋아요 수 업데이트에 실패했습니다"),

    ;

    private final int code;
    private final String message;
}
