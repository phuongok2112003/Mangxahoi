package com.example.Mangxahoi.constans.enums;

import com.example.Mangxahoi.services.DateTimeService;

/**
 * Các hằng số
 * */
public class Variables {
    /*REGEX*/

    public static final String SECRET_KEY = "m4Fq0@#";
    public static final long ACCESS_TOKEN_TIME = 60L * 60 * 1000;
    public static final long ACCESS_CODE= 5L * 60 * 1000;
    public static final long REFRESH_TOKEN_TIME = 30L * 24 * 60 * 60 * 1000;
    public static final DateTimeService dateTimeService = new DateTimeService();

}
