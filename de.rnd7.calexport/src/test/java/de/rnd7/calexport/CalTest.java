package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

public class CalTest {
	@Test
	public void testSimple() throws Exception {
		try (final InputStream in = CalTest.class.getResourceAsStream("simple.ics")) {
			final List<Event> events = EventFactory.fromICal(in);
			assertEquals(2, events.size());

			final Event event1 = events.get(0);
			final Event event2 = events.get(1);

			assertFalse(event1.isWholeDay());
			assertEquals("Das ist ein Test", event1.getTitle());
			assertEquals(90, event1.getDuration().toMinutes());

			assertTrue(event2.isWholeDay());
			assertEquals("Whole day event", event2.getTitle());
			assertEquals(3, event2.getDuration().toDays());
		}
	}

	@Test
	public void test_with_location() throws Exception {
		try (final InputStream in = CalTest.class.getResourceAsStream("with_location.ics")) {
			final List<Event> events = EventFactory.fromICal(in);
			assertEquals(2, events.size());

			final Event event1 = events.get(0);
			final Event event2 = events.get(1);

			assertFalse(event1.isWholeDay());
			assertEquals("event1", event1.getTitle());
			assertEquals("foo", event1.getLocation());

			assertEquals("event2", event2.getTitle());
			assertEquals("bar", event2.getLocation());
		}
	}
}
