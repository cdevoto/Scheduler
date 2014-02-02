package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class RecurrenceRule {
	public enum Field {
		SECONDS,
		MINUTES,
		HOURS,
		DAY_OF_MONTH,
		MONTH,
		DAY_OF_WEEK,
		YEAR
	}
	
	private Map<Field, String> values = new LinkedHashMap<>();
	private String expression;
	
	public static Builder newRecurrenceRule () {
		return new Builder();
	}
	
	public static Builder newRecurrenceRule (RecurrenceRule rrule) {
		return new Builder(rrule);
	}

	public static RecurrenceRule parseRecurrenceRule (String cron, Integer seconds) {
		Builder builder = newRecurrenceRule();
		StringTokenizer tokenizer = new StringTokenizer(cron, " \t");
		List<String> tokens = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}
		if (tokens.size() < 5 || tokens.size() > 6) {
			throw new IllegalArgumentException(String.format("Invalid cron syntax: %s", cron));
		}
		builder.withMinutes(tokens.get(0));
		builder.withHours(tokens.get(1));
		builder.withDayOfMonth(tokens.get(2));
		builder.withMonth(tokens.get(3));
		builder.withDayOfWeek(tokens.get(4));
		if (tokens.size() == 6) {
			builder.withYear(tokens.get(5));
		}
		if (seconds == null) {
		   seconds = (int)(Math.random() * 60);
		}
		builder.withSeconds(String.valueOf(seconds));
		RecurrenceRule result = builder.build();
		return result;
	}
	
	private RecurrenceRule(Builder builder) {
		values.put(Field.SECONDS, builder.seconds);
		values.put(Field.MINUTES, builder.minutes);
		values.put(Field.HOURS, builder.hours);
		values.put(Field.DAY_OF_MONTH, builder.dayOfMonth);
		values.put(Field.MONTH, builder.month);
		values.put(Field.DAY_OF_WEEK, builder.dayOfWeek);
		values.put(Field.YEAR, builder.year);
		expression = builder.seconds + " " + builder.minutes + " " + builder.hours + " " + builder.dayOfMonth + " " + builder.month + " " + builder.dayOfWeek;
		if (builder.year != null) {
			expression += " " + builder.year;
		}
	}
	
	public String get(Field field) {
		return values.get(field);
	}
	
	public boolean isInteger(Field field) {
		String value = get(field);
		if (value == null) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	public int getInt(Field field) {
		return Integer.parseInt(get(field));
	}
	public String toString () {
		return expression;
	}
	public static class Builder {
		private String seconds;
		private String minutes;
		private String hours;
		private String dayOfMonth;
		private String month;
		private String dayOfWeek;
		private String year;
		
		private Builder () {}

		public Builder(RecurrenceRule rrule) {
			this.seconds = rrule.get(Field.SECONDS);
			this.minutes = rrule.get(Field.MINUTES);
			this.hours = rrule.get(Field.HOURS);
			this.dayOfMonth = rrule.get(Field.DAY_OF_MONTH);
			this.month = rrule.get(Field.MONTH);
			this.dayOfWeek = rrule.get(Field.DAY_OF_WEEK);
			this.year = rrule.get(Field.YEAR);
		}

		public Builder withSeconds(String seconds) {
			this.seconds = seconds;
			return this;
		}

		public Builder withMinutes(String minutes) {
			this.minutes = minutes;
			return this;
		}

		public Builder withHours(String hours) {
			this.hours = hours;
			return this;
		}

		public Builder withDayOfMonth(String dayOfMonth) {
			this.dayOfMonth = dayOfMonth;
			return this;
		}

		public Builder withMonth(String month) {
			this.month = month;
			return this;
		}

		public Builder withDayOfWeek(String dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			return this;
		}

		public Builder withYear(String year) {
			this.year = year;
			return this;
		}
		
		public Builder withField(Field field, String value) {
			if (field == null) {
				throw new NullPointerException("Expected a 'field' value.");
			}
			switch (field) {
			case SECONDS:
				return withSeconds(value);
			case MINUTES:
				return withMinutes(value);
			case HOURS:
				return withHours(value);
			case DAY_OF_MONTH:
				return withDayOfMonth(value);
			case MONTH:
				return withMonth(value);
			case DAY_OF_WEEK:
				return withDayOfWeek(value);
			case YEAR:
				return withYear(value);
			default:
				throw new AssertionError();
			}
		}
		
		public RecurrenceRule build () {
			validateNotNull("seconds", seconds);
			validateNotNull("minutes", minutes);
			validateNotNull("hours", hours);
			validateNotNull("dayOfMonth", dayOfMonth);
			validateNotNull("month", month);
			validateNotNull("dayOfWeek", dayOfWeek);
			
			return new RecurrenceRule(this);
			
		}
	}

}
