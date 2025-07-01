package com.toonpick.internal.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CurrentUser(현재 로그인된 User 정보)
 * 1. 메서드 파라미터에만 붙일 수 있음
 * 2. 컴파일 이후에도 JVM에서 런타임 동안 어노테이션 정보를 유지
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
