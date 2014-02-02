package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util;

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

	public static String format (Date date) {
       DateFormat df = DATE_FORMAT.get();
       return df.format(date);
    }
	
	public static String format (long time) {
       DateFormat df = DATE_FORMAT.get();
       return df.format(new Date(time));
    }

	public static String formatNow () {
	       DateFormat df = DATE_FORMAT.get();
	       return df.format(new Date());
	}
	
	private DateFormatUtil () {}
	
}
