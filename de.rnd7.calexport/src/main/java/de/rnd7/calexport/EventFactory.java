package de.rnd7.calexport;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.parser.EventParser;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DateProperty;

public final class EventFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventFactory.class);

	private EventFactory() {
	}

	@SuppressWarnings("unchecked")
	public static List<Event> fromICal(final InputStream in) throws EventParseException {
		final CalendarBuilder builder = new CalendarBuilder();
		try {
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
		catch (ParserException | IOException e) {
			throw new EventParseException(e);
		}
	}

	public static List<Event> fromFlatFile(final InputStream in) throws IOException {
		return IOUtils.readLines(in, "utf-8").stream()
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.map(EventFactory::toEvent)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private static Event toEvent(final String line) {
		final Optional<Event> parse = EventParser.parse(line);
		if (!parse.isPresent()) {
			LOGGER.warn("cannot parse: {}", line);
			return null;
		}
		else {
			return parse.get();
		}
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
