package com.xaxc.teqin.common.util;


import java.util.Calendar;

public class HSSFDateUtil extends DateUtil {
    public HSSFDateUtil() {
    }

    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}
