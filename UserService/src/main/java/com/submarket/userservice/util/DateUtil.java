package com.submarket.userservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getDateTime(String fm) {

        Date today = new Date();
        System.out.println(today);

        SimpleDateFormat date = new SimpleDateFormat(fm);

        return date.format(today);
    }

    public static String getDateTime() {
        return getDateTime("yyyy.MM.dd");

    }
}
