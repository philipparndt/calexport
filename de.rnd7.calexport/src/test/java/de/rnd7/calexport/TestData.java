package de.rnd7.calexport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class TestData {
	public abstract List<Event> create();
	

	static LocalDate of(final String date) {
		return LocalDate.parse(date);
	}

	static LocalDateTime of(final String date, final String time) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(date + " " +  time, formatter);
	}

}
