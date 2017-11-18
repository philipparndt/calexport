package de.rnd7.calexport;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

public class EventTest {

	@Test
	public void test_title_clean() throws Exception {
		assertEquals("hello world", new Event("hello   world").getTitle());
		assertEquals("hello world", new Event("hello world").getTitle());
		assertEquals("hello world", new Event("hello #Abendmahl world").getTitle());
	}

	@Test
	public void test_affects() throws Exception {
		final Event event = new Event("")
				.setStart(LocalDate.of(2017, 10, 5))
				.setDuration(Duration.ofDays(5));

		assertFalse(event.affects(2017, Month.OCTOBER, 4));
		assertFalse(event.affects(2017, Month.OCTOBER, 10));
		assertTrue(event.affects(2017, Month.OCTOBER, 5));
		assertTrue(event.affects(2017, Month.OCTOBER, 9));
	}

	@Test
	public void test_first_in_range() throws Exception {
		final Event event = new Event("")
				.setStart(LocalDate.of(2017, 10, 5))
				.setDuration(Duration.ofDays(5));

		assertFalse(event.isFirstInRange(2017, Month.OCTOBER, 4));
		assertFalse(event.isFirstInRange(2017, Month.OCTOBER, 10));
		assertFalse(event.isFirstInRange(2017, Month.OCTOBER, 9));
		assertTrue(event.isFirstInRange(2017, Month.OCTOBER, 5));
	}

	@Test
	public void test_first_in_range__for_multi_month_event() throws Exception {
		final Event event = new Event("")
				.setStart(LocalDate.of(2017, 9, 25))
				.setDuration(Duration.ofDays(30));

		assertFalse(event.isFirstInRange(2017, Month.SEPTEMBER, 24));
		assertTrue(event.isFirstInRange(2017, Month.SEPTEMBER, 25));
		assertFalse(event.isFirstInRange(2017, Month.SEPTEMBER, 26));

		assertTrue(event.isFirstInRange(2017, Month.OCTOBER, 1));
		assertFalse(event.isFirstInRange(2017, Month.OCTOBER, 2));
	}

	@Test
	public void test_first_in_range__for_multi_month_event2() throws Exception {
		final Event event = new Event("Multi month event")
				.setStart(LocalDate.of(2017, 9, 15))
				.setWholeDay(true)
				.setDuration(Duration.ofDays(60));

		assertTrue(event.affects(2017, Month.SEPTEMBER));
		assertTrue(event.affects(2017, Month.OCTOBER));
		assertTrue(event.affects(2017, Month.NOVEMBER));
	}

	@Test
	public void test_count_remain() throws Exception {
		final Event event = new Event("")
				.setStart(LocalDate.of(2017, 10, 5))
				.setDuration(Duration.ofDays(5));

		assertEquals(5, event.remainingInMonthFrom(2017, Month.OCTOBER, 5));
		assertEquals(4, event.remainingInMonthFrom(2017, Month.OCTOBER, 6));
		assertEquals(0, event.remainingInMonthFrom(2017, Month.OCTOBER, 10));
	}

	@Test
	public void test_count_remain_offbyone_bug() throws Exception {
		final Event event = new Event("Multi month event")
				.setStart(LocalDate.of(2017, 10, 27))
				.setDuration(Duration.ofDays(8));

		assertEquals(5, event.remainingInMonthFrom(2017, Month.OCTOBER, 27));
	}

	@Test
	public void test_count_remain__for_multi_month_event() throws Exception {
		final Event event = new Event("")
				.setStart(LocalDate.of(2017, 9, 25))
				.setDuration(Duration.ofDays(60));

		assertEquals(6, event.remainingInMonthFrom(2017, Month.SEPTEMBER, 25));
		assertEquals(31, event.remainingInMonthFrom(2017, Month.OCTOBER, 1));
	}

	@Test
	public void test_conflict() throws Exception {
		final Event event1 = new Event("")
				.setStart(LocalDate.of(2017, 9, 25))
				.setDuration(Duration.ofDays(3));

		final Event event2 = new Event("")
				.setStart(LocalDate.of(2017, 9, 27))
				.setDuration(Duration.ofDays(15));

		assertTrue(event1.conflictsInMonth(event1, 2017, Month.SEPTEMBER));
		assertFalse(event1.conflictsInMonth(event1, 2017, Month.OCTOBER));

		assertTrue(event2.conflictsInMonth(event2, 2017, Month.SEPTEMBER));
		assertTrue(event2.conflictsInMonth(event2, 2017, Month.OCTOBER));

		assertTrue(event1.conflictsInMonth(event2, 2017, Month.SEPTEMBER));
		assertFalse(event1.conflictsInMonth(event2, 2017, Month.OCTOBER));

		assertTrue(create(1, 5).conflictsInMonth(create(5, 5), 2017, Month.JANUARY));
		assertTrue(create(5, 5).conflictsInMonth(create(1, 5), 2017, Month.JANUARY));

		assertFalse(create(1, 5).conflictsInMonth(create(6, 5), 2017, Month.JANUARY));
		assertFalse(create(6, 5).conflictsInMonth(create(1, 5), 2017, Month.JANUARY));
	}

	@Test
	public void test_conflict_regression() throws Exception {
		assertTrue(create(27, 3).conflictsInMonth(create(23, 15), 2017, Month.JANUARY));
		assertTrue(create(23, 15).conflictsInMonth(create(27, 3), 2017, Month.JANUARY));
	}

	private static Event create(final int day, final int durationDays) {
		return new Event("")
				.setStart(LocalDate.of(2017, 1, day))
				.setDuration(Duration.ofDays(durationDays));
	}
}
