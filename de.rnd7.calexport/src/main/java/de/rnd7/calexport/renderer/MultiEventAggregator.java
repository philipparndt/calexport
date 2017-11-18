package de.rnd7.calexport.renderer;

import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import de.rnd7.calexport.Event;

public class MultiEventAggregator {
	final List<List<Event>> eventsByColum = new ArrayList<>();
	private final int year;
	private final Month month;

	public MultiEventAggregator(final List<Event> events, final int year, final Month month) {
		this.year = year;
		this.month = month;

		final Stream<Event> es = this.findMultiDayEventsInRange(events, year, month);
		final List<Event> collect = es.collect(Collectors.toList());

		collect.stream().sorted(Comparator.comparing(Event::getStart).thenComparing(Event::getTitle))
		.forEach(this::putEvent);
	}

	private void putEvent(final Event event) {
		List<Event> targetList = null;

		for (final List<Event> list : this.eventsByColum) {
			if (!list.stream().anyMatch(e -> e.conflictsInMonth(event, this.year, this.month))) {
				targetList = list;
				break;
			}
		}

		if (targetList == null) {
			this.newGroup(this.eventsByColum, event);
		}
		else {
			targetList.add(event);
		}
	}

	public List<List<Event>> getEventsByColum() {
		final ImmutableList<List<Event>> list = ImmutableList.copyOf(this.eventsByColum);
		return list.reverse();
	}

	private void newGroup(final List<List<Event>> eventsByColum, final Event event) {
		final List<Event> grouped = new ArrayList<>();
		grouped.add(event);
		eventsByColum.add(grouped);
	}

	public int getColspan() {
		return this.eventsByColum.size() + 1;
	}

	private Stream<Event> findMultiDayEventsInRange(final List<Event> events, final int year, final Month month) {
		return events.stream()
				.filter(Event::isWholeDay)
				.filter(event -> event.getDuration().toDays() > 1L)
				.filter(event -> event.affects(year, month));
	}

}
