package de.rnd7.calexport;

import org.junit.Test;

public class EventParseExceptionTest {
	@Test(expected=EventParseException.class)
	public void test_instance() throws Exception {
		throw new EventParseException(new Throwable());
	}
}
