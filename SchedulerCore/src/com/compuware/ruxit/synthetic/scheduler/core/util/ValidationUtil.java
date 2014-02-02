package com.compuware.ruxit.synthetic.scheduler.core.util;

import java.util.Collection;

public final class ValidationUtil {
	
	public static void validateNotNull (String name, Object value) {
		if (value == null) {
			throw new IllegalStateException(String.format("Expected a value for the '%s' field.", name));
		}
	}
	
	public static <T> void validateNotEmpty (String name, Collection<T> c) {
		if (c == null || c.isEmpty()) {
			throw new IllegalStateException(String.format("Expected at least one value for the '%s' field.", name));
		}
	}

	private ValidationUtil () {}

}
