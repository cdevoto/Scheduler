package com.compuware.ruxit.synthetic.test.dao.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;

public class TestAbilityFlag {
	private static AbilityFlag IPV4;
	private static AbilityFlag IPV6;
	private static AbilityFlag SMS;
	
	@BeforeClass
	public static void beforeClass () {
		IPV4 = AbilityFlag.create()
			.withId(1L)
			.withLevel(2L)
			.withMask(1L)
			.withDescription("IPv4")
			.build();

		IPV6 = AbilityFlag.create()
				.withId(2L)
				.withLevel(2L)
				.withMask(2L)
				.withDescription("IPv6")
				.build();
	
		SMS = AbilityFlag.create()
				.withId(3L)
				.withLevel(1L)
				.withMask(4L)
				.withDescription("SMS")
				.build();
	}

	@Test
	public void testSetFlag() {
		long bitMask = IPV4.setFlag(0L);
        assertThat(bitMask, is(1L));
        
        bitMask = IPV4.setFlag(1L);
        assertThat(bitMask, is(1L));

        bitMask = IPV4.unsetFlag(1L);
        assertThat(bitMask, is(0L));

        bitMask = IPV4.unsetFlag(0L);
        assertThat(bitMask, is(0L));
        
        bitMask = IPV6.setFlag(0L);
        assertThat(bitMask, is(2L));
        
        bitMask = IPV6.setFlag(2L);
        assertThat(bitMask, is(2L));

        bitMask = IPV6.unsetFlag(2L);
        assertThat(bitMask, is(0L));

        bitMask = IPV6.unsetFlag(0L);
        assertThat(bitMask, is(0L));

        bitMask = SMS.setFlag(0L);
        assertThat(bitMask, is(4L));
        
        bitMask = SMS.setFlag(4L);
        assertThat(bitMask, is(4L));

        bitMask = SMS.unsetFlag(4L);
        assertThat(bitMask, is(0L));

        bitMask = SMS.unsetFlag(0L);
        assertThat(bitMask, is(0L));
        
        bitMask = IPV6.setFlag(1L);
        assertThat(bitMask, is(3L));
        
        bitMask = SMS.setFlag(3L);
        assertThat(bitMask, is(7L));
        
        bitMask = IPV4.unsetFlag(7L);
        assertThat(bitMask, is(6L));
	
        bitMask = SMS.unsetFlag(6L);
        assertThat(bitMask, is(2L));
	}

}
