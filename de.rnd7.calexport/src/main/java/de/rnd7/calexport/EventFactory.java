package de.rnd7.calexport;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DateProperty;

public final class EventFactory {
	private EventFactory() {
	}

	@SuppressWarnings("unchecked")
	public static List<Event> parseEvents(final InputStream in) throws Exception {
		final CalendarBuilder builder = new CalendarBuilder();
		final Calendar calendar = builder.build(in);

		final Stream<Component> components = calendar.getComponents()
				.stream()
				.filter(Component.class::isInstance)
				.map(Component.class::cast);

		return components
				.filter(EventFactory::isEvent)
				.map(EventFactory::parse)
				.collect(Collectors.toList());
	}

	private static boolean isEvent(final Component component) {
		return "VEVENT".equals(component.getName()) && component.getProperty("SUMMARY") != null;
	}

	private static Event parse(final Component component) {
		final Property property = component.getProperty("SUMMARY");

		final VEvent vEvent = new VEvent(component.getProperties());

		return new Event(property.getValue())
				.setWholeDay(isWholeDay(vEvent))
				.setStart(parseDate(vEvent.getStartDate()))
				.setEnd(parseDate(vEvent.getEndDate()));
	}

	private static boolean isWholeDay(final VEvent vEvent) {
		return vEvent.getStartDate().getValue().length() == 8;
	}

	private static LocalDateTime parseDate(final DateProperty date) {
		date.getValue();
		final String zoneId = Optional.ofNullable(date.getTimeZone()).map(TimeZone::getID)
				.orElseGet(ZoneId.systemDefault()::getId);

		return date.getDate().toInstant().atZone(ZoneId.of(zoneId)).toLocalDateTime();
	}
}
