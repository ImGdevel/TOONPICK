package com.toonpick.common.constants;

public class ValidationPatterns {

    private ValidationPatterns(){}

    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final String PHONE = "^01[016789][0-9]{7,8}$";

    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$";

}
