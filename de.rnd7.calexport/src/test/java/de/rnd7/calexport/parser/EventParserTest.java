package de.rnd7.calexport.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.format.DateTimeFormatter;

import org.junit.Test;

import de.rnd7.calexport.Event;

public class EventParserTest {

	@Test
	public void test_single_day_event() throws Exception {
		Event event = EventParser.parse("2018-01-01 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());

		assertEquals("2018-01-01", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getStartDayInclusive()));
		assertEquals("2018-01-02", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getEndDayExclusive()));
		assertTrue(event.isWholeDay());

		event = EventParser.parse("2018-01-01		Testtitle").get();
		assertEquals("Testtitle", event.getTitle());

		assertEquals("2018-01-01", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getStartDayInclusive()));
		assertEquals("2018-01-02", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getEndDayExclusive()));
		assertTrue(event.isWholeDay());
	}

	@Test
	public void test_multi_day_event() throws Exception {
		final Event event = EventParser.parse("2018-01-01 - 2018-01-15 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());

		assertEquals("2018-01-01", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getStartDayInclusive()));
		assertEquals("2018-01-16", DateTimeFormatter.ISO_LOCAL_DATE.format(event.getEndDayExclusive()));
		assertTrue(event.isWholeDay());
	}

	@Test
	public void test_event_with_start_time() throws Exception {
		Event event = EventParser.parse("2018-01-01 09:00 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());
		assertEquals("2018-01-01T09:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getStart()));
		assertEquals("2018-01-01T11:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getEnd()));
		assertFalse(event.isWholeDay());

		event = EventParser.parse("2018-01-01 9:00 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());
		assertEquals("2018-01-01T09:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getStart()));
		assertEquals("2018-01-01T11:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getEnd()));
		assertFalse(event.isWholeDay());

		event = EventParser.parse("2018-01-01 21:00 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());
		assertEquals("2018-01-01T21:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getStart()));
		assertEquals("2018-01-01T23:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getEnd()));
		assertFalse(event.isWholeDay());
	}

	@Test
	public void test_event_with_start_end_time() throws Exception {
		final Event event = EventParser.parse("2018-01-01 09:00 - 21:00 Testtitle").get();
		assertEquals("Testtitle", event.getTitle());
		assertEquals("2018-01-01T09:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getStart()));
		assertEquals("2018-01-01T21:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(event.getEnd()));
		assertFalse(event.isWholeDay());
	}

	@Test
	public void test_invalid_event() throws Exception {
		assertFalse(EventParser.parse("Testtitle").isPresent());
	}
}
