package de.rnd7.calexport;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.rnd7.calexport.renderer.ImageTags;

public class Event {
	private final String title;
	private boolean wholeDay;
	private LocalDateTime start;
	private LocalDateTime end;
	private final List<String> tags;

	public Event(final String title) {
		this.tags = ImageTags.getImageTags().keySet().stream()
				.filter(title.toLowerCase()::contains)
				.collect(Collectors.toList());

		this.title = this.cleanup(title);
	}

	private String cleanup(final String title) {
		final String joined = this.tags.stream().collect(Collectors.joining("|"));
		final Pattern pattern = Pattern.compile("(" + joined + ")", Pattern.CASE_INSENSITIVE);
		String replaced = pattern.matcher(title).replaceAll("");

		while (replaced.contains("  ")) {
			replaced = replaced.replaceAll("  ", " ");
		}
		return replaced;
	}

	public List<String> getTags() {
		return this.tags;
	}

	public Event setWholeDay(final boolean wholeDay) {
		this.wholeDay = wholeDay;
		return this;
	}

	public boolean isWholeDay() {
		return this.wholeDay;
	}

	public Event setStart(final LocalDateTime start) {
		this.start = start;
		this.setDuration(Duration.ofHours(2));
		return this;
	}

	public Event setStart(final LocalDate start) {
		this.setStart(start.atStartOfDay());
		this.setDuration(Duration.ofDays(1));
		return this.setWholeDay(true);
	}

	public LocalDateTime getStart() {
		return this.start;
	}

	public LocalDate getStartDayInclusive() {
		return this.start.toLocalDate();
	}

	public Event setEnd(final LocalDateTime end) {
		this.end = end;
		return this;
	}

	public Event setEnd(final LocalDate end) {
		this.end = end.atStartOfDay().plus(Duration.ofDays(1));
		return this;
	}

	public LocalDateTime getEnd() {
		return this.end;
	}

	public LocalDate getEndDayExclusive() {
		return this.end.toLocalDate();
	}

	public LocalDate getEndDayInclusive() {
		return this.getEndDayExclusive().atStartOfDay().minus(Duration.ofDays(1)).toLocalDate();
	}

	public Duration getDuration() {
		return Duration.between(this.getStart(), this.getEnd());
	}

	public Event setDuration(final Duration duration) {
		this.end = this.start.plus(duration);
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean conflictsInMonth(final Event event, final int year, final Month month) {
		if (!this.affects(year, month) || !event.affects(year, month)) {
			return false;
		}

		final LocalDate start1 = this.getTrimmedStart(this.getStartDayInclusive(), year, month);
		final LocalDate end1 = this.getTrimmedEnd(this.getEndDayInclusive(), year, month);

		final LocalDate start2= this.getTrimmedStart(event.getStartDayInclusive(), year, month);
		final LocalDate end2 = this.getTrimmedEnd(event.getEndDayInclusive(), year, month);

		return this.beforeOrEq(start1, end2) && this.beforeOrEq(start2, end1);
	}

	private boolean beforeOrEq(final LocalDate a, final LocalDate b) {
		return a.isBefore(b) || a.equals(b);
	}

	public boolean affects(final int year, final Month month) {
		final LocalDate monthStart = LocalDate.of(year, month.getValue(), 1);

		final int days = month.length(Year.isLeap(year));
		final LocalDate monthEnd = LocalDate.of(year, month.getValue(), days);

		final LocalDate startDay = this.getStartDayInclusive();
		final LocalDate endDay = this.getEndDayExclusive();

		return startDay.isAfter(monthStart) && startDay.isBefore(monthEnd)
				|| endDay.isAfter(monthStart) && endDay.isBefore(monthEnd)
				|| startDay.isBefore(monthStart) && endDay.isAfter(monthStart);
	}

	public boolean affects(final int year, final Month month, final int dayOfMonth) {
		final LocalDate date = LocalDate.of(year, month.getValue(), dayOfMonth);

		final LocalDate startDay = this.getStartDayInclusive();
		final LocalDate endDay = this.getEndDayExclusive();

		return (date.equals(startDay) || date.isAfter(startDay)) && date.isBefore(endDay);
	}

	public boolean isFirstInRange(final int year, final Month month, final int day) {
		final LocalDate date = LocalDate.of(year, month.getValue(), day);

		final LocalDate startDay = this.getTrimmedStart(this.getStartDayInclusive(), year, month);

		return date.equals(startDay);
	}

	public int remainingInMonthFrom(final int year, final Month month, final int dayOfMonth) {
		if (!this.affects(year, month, dayOfMonth)) {
			return 0;
		}

		final LocalDate startDay = LocalDate.of(year, month.getValue(), dayOfMonth);
		final LocalDate endDay = this.getTrimmedEnd(this.getEndDayExclusive(), year, month);

		return (int) Duration.between(startDay.atStartOfDay(), endDay.atStartOfDay()).toDays();
	}

	private LocalDate getTrimmedStart(final LocalDate startDay, final int year, final Month month) {
		final LocalDate monthStart = LocalDate.of(year, month.getValue(), 1);

		if (startDay.isBefore(monthStart)) {
			return monthStart;
		}
		return startDay;
	}

	private LocalDate getTrimmedEnd(final LocalDate endDay, final int year, final Month month) {
		final int days = month.length(Year.isLeap(year));
		final LocalDate monthEnd = LocalDate.of(year, month.getValue(), days);

		if (endDay.isAfter(monthEnd)) {
			return monthEnd.atStartOfDay().plus(Duration.ofDays(1)).toLocalDate();
		}
		return endDay;
	}


	@Override
	public String toString() {
		final Temporal startTemp = this.isWholeDay() ? this.getStartDayInclusive() : this.getStart();
		final Temporal endTemp = this.isWholeDay() ? this.getEndDayExclusive() : this.getEnd();

		return String.format("%s - %s: %s", startTemp, endTemp, this.title);
	}
}
