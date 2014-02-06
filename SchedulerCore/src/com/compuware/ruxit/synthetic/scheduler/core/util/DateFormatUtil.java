package com.compuware.ruxit.synthetic.scheduler.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    private static final ThreadLocal<DateFormat> DATE_FORMAT =
            new ThreadLocal<DateFormat>() {
                @Override protected DateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            }
        };

    private static final ThreadLocal<DateFormat> DATE_FORMAT_MILLIS =
            new ThreadLocal<DateFormat>() {
                @Override protected DateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
            }
        };

    public static String format (Date date) {
       DateFormat df = DATE_FORMAT.get();
       return df.format(date);
    }
	
    public static String formatMillis (Date date) {
        DateFormat df = DATE_FORMAT_MILLIS.get();
        return df.format(date);
     }

    public static String format (long time) {
       DateFormat df = DATE_FORMAT.get();
       return df.format(new Date(time));
    }

    public static String formatMillis (long time) {
        DateFormat df = DATE_FORMAT_MILLIS.get();
        return df.format(new Date(time));
     }

    public static String formatNow () {
	       DateFormat df = DATE_FORMAT.get();
	       return df.format(new Date());
	}
	
    public static String formatNowMillis () {
	       DateFormat df = DATE_FORMAT_MILLIS.get();
	       return df.format(new Date());
	}

    private DateFormatUtil () {}
	
}
