package com.toonpick.common.utils;

import java.util.UUID;

public class UUIDUtils {

    private UUIDUtils(){}

    /**
     * 랜덤 UUID 문자열 생성
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * dashes 없는 UUID
     */
    public static String generateCompact() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
