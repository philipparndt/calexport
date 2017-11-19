package de.rnd7.calexport.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.rnd7.calexport.Event;

public final class EventParser {

	private static final String ISO_DATE = "(?:20\\d\\d-(?:0|1)\\d-(?:0|1|2|3)\\d)";
	private static final String GERMAN_DATE = "(?:(?:0|1|2|3)?\\d\\.(?:0|1)?\\d\\.20\\d\\d)";

	private static final String DATE = String.format("(%s|%s)", ISO_DATE, GERMAN_DATE);

	private static final String TIME = "((?:0|1|2)?\\d:\\d\\d)";
	private static final String WHITESPACES = "\\s+";

	private static final Pattern SINGLE_DAY = Pattern.compile(DATE + WHITESPACES + "(.*)");
	private static final Pattern MULTI_DAY = Pattern.compile(DATE + WHITESPACES + "-" + WHITESPACES + DATE + WHITESPACES + "(.*)");
	private static final Pattern WITH_START_TIME = Pattern.compile(DATE + WHITESPACES + TIME + WHITESPACES + "(.*)");
	private static final Pattern WITH_START_END_TIME = Pattern.compile(DATE + WHITESPACES + TIME + WHITESPACES + "-" + WHITESPACES + TIME + WHITESPACES + "(.*)");

	private EventParser() {
	}

	public static Optional<Event> parse(final String input) {
		final Matcher withStartEndTimeMatcher = WITH_START_END_TIME.matcher(input);
		if (withStartEndTimeMatcher.matches()) {
			return Optional.of(parseWithStartEndTime(withStartEndTimeMatcher.group(1), withStartEndTimeMatcher.group(2), withStartEndTimeMatcher.group(3), withStartEndTimeMatcher.group(4)));
		}

		final Matcher withStartTimeMatcher = WITH_START_TIME.matcher(input);
		if (withStartTimeMatcher.matches()) {
			return Optional.of(parseWithStartTime(withStartTimeMatcher.group(1), withStartTimeMatcher.group(2), withStartTimeMatcher.group(3)));
		}

		final Matcher multiDayMatcher = MULTI_DAY.matcher(input);
		if (multiDayMatcher.matches()) {
			return Optional.of(parseMultiDay(multiDayMatcher.group(1), multiDayMatcher.group(2), multiDayMatcher.group(3)));
		}

		final Matcher singleDayMatcher = SINGLE_DAY.matcher(input);
		if (singleDayMatcher.matches()) {
			return Optional.of(parseSingleDay(singleDayMatcher.group(1), singleDayMatcher.group(2)));
		}

		return Optional.empty();
	}

	private static Event parseWithStartEndTime(final String date, final String timeStart, final String timeEnd, final String title) {
		return new Event(title.trim())
				.setStart(parseDateTime(date, timeStart))
				.setEnd(parseDateTime(date, timeEnd));
	}

	private static Event parseWithStartTime(final String date, final String timeStart, final String title) {
		return new Event(title.trim())
				.setStart(parseDateTime(date, timeStart));
	}

	private static Event parseMultiDay(final String start, final String end, final String title) {
		return new Event(title.trim())
				.setStart(parseDate(start))
				.setEnd(parseDate(end));
	}

	private static Event parseSingleDay(final String date, final String title) {
		return new Event(title.trim())
				.setStart(parseDate(date));
	}

	private static LocalDateTime parseDateTime(final String date, final String time) {
		return LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"));
	}

	private static LocalDate parseDate(final String date) {
		try {
			return LocalDate.parse(date);
		}
		catch (final DateTimeParseException e) {
			return DateTimeFormatter.ofPattern("d.M.yyyy").parse(date, LocalDate::from);
		}
	}

}
