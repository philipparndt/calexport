package de.rnd7.calexport;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class EventFactoryTest {
	@Test(expected=EventParseException.class)
	public void test_invalid_ical() throws Exception {
		try (ByteArrayInputStream in = new ByteArrayInputStream("".getBytes())) {
			EventFactory.fromICal(in);
		}
	}
}
