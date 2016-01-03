package com.brave.mystery.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dcohen on 7/22/15.
 */
public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    public static String getTodayDateString() {
        return format.format(new Date());
    }
}
