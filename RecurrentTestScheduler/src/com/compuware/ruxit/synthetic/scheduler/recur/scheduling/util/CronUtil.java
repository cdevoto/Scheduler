package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util;

import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.newRecurrenceRule;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.parseRecurrenceRule;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.DAY_OF_MONTH;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.DAY_OF_WEEK;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.HOURS;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.MINUTES;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.MONTH;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.SECONDS;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Field.YEAR;
import it.sauronsoftware.cron4j.Predictor;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.RecurrenceRule.Builder;

/**
 * Transforms cron expressions from standard format to Quartz cron format
 * 
 * @author Carlos Devoto
 *
 */
public class CronUtil {
	
	public static String translate (String cron) {
		return translate(cron, null);
	}

	public static String translate (String cron, Integer seconds) {
		RecurrenceRule rrule = parseRecurrenceRule(cron, seconds);
		Builder builder = newRecurrenceRule(rrule);
		if (!"*".equals(rrule.get(DAY_OF_MONTH)) && "*".equals(rrule.get(DAY_OF_WEEK))) {
			builder.withDayOfWeek("?");
		} else if ("*".equals(rrule.get(DAY_OF_MONTH))) {
			builder.withDayOfMonth("?");
		}
		rrule = builder.build();
		return rrule.toString();
	}
	
	public static long getPrevRecurrenceEnd (MaintScheduleView schedule) {
		RecurrenceRule rrule = parseRecurrenceRule(schedule.getRecurrenceRule(), 0);
		TimeZone timeZone = TimeZone.getTimeZone(schedule.getTimeZone().getName());
		if (rrule.isInteger(YEAR) && rrule.isInteger(MONTH) && rrule.isInteger(DAY_OF_MONTH) && rrule.isInteger(HOURS) && rrule.isInteger(MINUTES) && rrule.isInteger(SECONDS)) {
			GregorianCalendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, rrule.getInt(SECONDS));
            c.set(Calendar.MINUTE, rrule.getInt(MINUTES));
            c.set(Calendar.HOUR_OF_DAY, rrule.getInt(HOURS));
            c.set(Calendar.DAY_OF_MONTH, rrule.getInt(DAY_OF_MONTH));
            c.set(Calendar.MONTH, rrule.getInt(MONTH) - 1);
            c.set(Calendar.YEAR, rrule.getInt(YEAR));
            long prevStart = c.getTimeInMillis();
            if (prevStart > System.currentTimeMillis()) {
            	return -1;
            } else {
            	long prevEnd = prevStart + (schedule.getDuration() * 60 * 1000);
            	return prevEnd;
            }
		} else if (rrule.get(YEAR) != null) {
			throw new IllegalArgumentException("The year field should only be used to specify precise dates.");
		}
		long now = System.currentTimeMillis() + (60 * 1000);
		Predictor p = new Predictor(schedule.getRecurrenceRule(), now);
		p.setTimeZone(timeZone);
		long prevStart = p.prevMatchingTime();
		long prevEnd = prevStart + (schedule.getDuration() * 60 * 1000);
		return prevEnd;
	}
	
	private CronUtil () {}
	
	
}
