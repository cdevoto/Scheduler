package com.compuware.ruxit.synthetic.scheduler.core.util;

import java.util.LinkedList;
import java.util.List;

public class BitwiseUtil {

	public static List<Long> getMasks(long bitMap) {
		String bitMaskString = Long.toBinaryString(bitMap);
		int length = bitMaskString.length();
		List<Long> masks = new LinkedList<>();
		for (int i = 0; i < length; i++) {
			long mask = 1 << i;
			if (mask == (bitMap & mask)) {
				masks.add(mask);
			}
		}
		return masks;
	}
	
}
