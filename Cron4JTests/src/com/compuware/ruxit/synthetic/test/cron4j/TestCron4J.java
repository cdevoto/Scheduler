package com.compuware.ruxit.synthetic.test.cron4j;

import it.sauronsoftware.cron4j.Predictor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class TestCron4J {

	@Test
	public void test() {
		/*
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Predictor predictor = new Predictor("0 0 * * *");
		Date date = predictor.nextMatchingDate();
		System.out.println(df.format(date));
		*/
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date date = null;
		int n = 10;
		//String pattern = "*/5 * * * *";
		//String pattern = "2-59/5 * * * *";
		String pattern = "3-59/5 * * * *";
		Predictor p = new Predictor(pattern);
		for (int i = 0; i < n; i++) {
			date = p.nextMatchingDate();
			System.out.println(df.format(date));
		}

		/*
		n = 100;
        System.out.println();		
		//pattern = "2,7-59/5 * * * *";
        pattern = "0 0 * * FRI";
		p = new Predictor(pattern);
		//p.setTimeZone(TimeZone.);
		for (int i = 0; i < n; i++) {
			date = p.prevMatchingDate();
			System.out.println(df.format(date));
		}
		*/
	}

}
