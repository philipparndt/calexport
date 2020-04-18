package de.rnd7.calexport;

import org.junit.Test;

public class EventParseRuntimeExceptionTest {
	@Test(expected=EventParseRuntimeException.class)
	public void test_instance() throws Exception {
		throw new EventParseRuntimeException(new Throwable());
	}
}
