package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.ColumnType;
import de.rnd7.calexport.config.Calconfig.Calendar;

public class ExporterTest {
	@Test
	public void test_integration() throws Exception {
		final URL url = ExporterTest.class.getResource("simple.ics");
		final Calconfig calconfig = new Calconfig();
		calconfig.setExportmonths(2);
		calconfig.setTitle("Title");

		final List<Calendar> calendars = calconfig.getCalendar();
		calendars.add(this.createCalendar(url, ColumnType.DATE, "Main"));
		calendars.add(this.createCalendar(url, ColumnType.LOCATION, "A"));

		final List<String> files = Exporter.export(LocalDate.of(2017,  10,  1), calconfig);

		assertEquals(2, files.size());
		final String october = files.get(0);
		assertTrue(october.contains("Whole day event"));
		assertTrue(october.contains("Das ist ein Test"));

		final String november = files.get(1);
		assertFalse(november.contains("Whole day event"));
		assertFalse(november.contains("Das ist ein Test"));
	}

	private Calendar createCalendar(final URL url, final ColumnType type, final String name) {
		final Calendar calendar = new Calendar();
		calendar.setUrl(url.toExternalForm());
		calendar.setType(type);
		calendar.setName(name);
		return calendar;
	}
}
