package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.rnd7.calexport.renderer.MultiEventAggregator;

public class MultiEventAggregatorTest {
	@Test
	public void test_aggregation1() throws Exception {
		final Event event2 = create("2", 20, 5);
		final Event event3 = create("3", 15, 5);
		final List<Event> events = ImmutableList.of(event2, event3);
		final MultiEventAggregator aggregator = new MultiEventAggregator(events, 2017, Month.JANUARY);
		assertEquals(1, aggregator.getEventsByColum().size());
	}

	@Test
	public void test_aggregation2() throws Exception {
		final Event event1 = create("1", 1, 5);
		final Event event2 = create("2", 20, 5);
		final Event event3 = create("3", 15, 5);
		final List<Event> events = ImmutableList.of(event1, event2, event3);
		final MultiEventAggregator aggregator = new MultiEventAggregator(events, 2017, Month.JANUARY);
		assertEquals(1, aggregator.getEventsByColum().size());
	}


	private static Event create(final String title, final int day, final int durationDays) {
		return new Event(title)
				.setStart(LocalDate.of(2017, 1, day))
				.setDuration(Duration.ofDays(durationDays));
	}
}
