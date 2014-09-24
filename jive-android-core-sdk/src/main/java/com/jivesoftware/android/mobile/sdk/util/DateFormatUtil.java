package com.jivesoftware.android.mobile.sdk.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormatUtil {
    private static final ThreadLocal<DateFormat> iso8601dateFormatThreadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat gmtIso8601dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            gmtIso8601dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return gmtIso8601dateFormat;
        }
    };

    public static DateFormat getGmtIso8601DateFormat() {
        return iso8601dateFormatThreadLocal.get();
    }
}
