package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

public class ExporterTest {
	@Test
	public void test_integration() throws Exception {

		final URL url = ExporterTest.class.getResource("simple.ics");
		final String surl = url.toExternalForm();

		final List<String> files = Exporter.export(LocalDate.of(2017,  10,  1), surl, surl, surl, surl, 2);
		assertEquals(2, files.size());
		final String october = files.get(0);
		assertTrue(october.contains("Whole day event"));
		assertTrue(october.contains("Das ist ein Test"));

		final String november = files.get(1);
		assertFalse(november.contains("Whole day event"));
		assertFalse(november.contains("Das ist ein Test"));
	}
}
