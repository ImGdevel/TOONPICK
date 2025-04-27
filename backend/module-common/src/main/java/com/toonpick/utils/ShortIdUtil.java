package com.toonpick.utils;

import java.security.SecureRandom;


public class ShortIdUtil {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();
    private static final int ID_LENGTH = 10;

     /**
     * 닉네임 생성시 랜덤 id값 생성
     */
    public static String generateRandomId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }
}
