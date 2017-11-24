package de.rnd7.calexport.config;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.Test;

import de.rnd7.calexport.config.Calconfig.Calendar;
import de.rnd7.calexport.config.Calconfig.Hashtag;

public class ConfigurationTest {
	@Test
	public void test_write() throws Exception {
		final Calconfig config = new Calconfig();
		config.setTitle("Title");
		config.setFooter("Footer");
		config.setExportmonths(15);

		final List<Calendar> calendars = config.getCalendar();
		final Calendar calendar = new Calendar();
		calendar.setName("Main");
		calendar.setUrl("http://example.org/webal.ics");
		calendar.setType(ColumnType.DATE);

		calendars.add(calendar);

		final List<Hashtag> tags = config.getHashtag();
		final Hashtag hashtag = new Hashtag();
		hashtag.setImage("examplebase64image");
		hashtag.setName("foobar");
		tags.add(hashtag);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Configuration.writeTo(config, out);

			assertEquals(this.load("example.xml"), out.toString(CharEncoding.UTF_8));
		}
	}

	@Test
	public void test_read() throws Exception {
		final String xml = this.load("example.xml");
		try (final InputStream in = new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8))) {
			final Calconfig calconfig = Configuration.loadFrom(in);

			assertEquals("Title", calconfig.getTitle());
			assertEquals("Footer", calconfig.getFooter());
			assertEquals(15, calconfig.getExportmonths());
			assertEquals(1, calconfig.getCalendar().size());
			assertEquals(1, calconfig.getHashtag().size());
		}
	}

	@Test(expected=ConfigurationRuntimeException.class)
	public void test_invalid_config() throws Exception {
		try (final InputStream in = new ByteArrayInputStream("foo".getBytes(CharEncoding.UTF_8))) {
			Configuration.loadFrom(in);
		}
	}

	private String load(final String name) throws IOException {
		try(InputStream in = ConfigurationTest.class.getResourceAsStream(name)) {
			return IOUtils.toString(in, CharEncoding.UTF_8);
		}
	}
}
