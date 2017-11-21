package de.rnd7.calexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.renderer.ColumnGenerator;
import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class Exporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class);

	private Exporter() {
	}

	public static List<String> export(final LocalDate start, final String mainUrl, final String moessingenUrl, final String bodelshausenUrl, final String dusslingenUrl, final int monthCount) throws EventParseException, IOException {
		final List<Event> main = load(mainUrl);
		final List<Event> moessingen = load(moessingenUrl);
		final List<Event> bodelshausen = load(bodelshausenUrl);
		final List<Event> dusslingen = load(dusslingenUrl);

		final LocalDate month = LocalDate.of(start.getYear(), start.getMonth(), 1);

		final List<String> result = new ArrayList<>();

		for (int i = 0; i < monthCount; i++) {
			final LocalDate exportMonth = month.plusMonths(i);

			result.add(export(main, moessingen, bodelshausen, dusslingen, exportMonth));
		}

		return result;
	}

	private static String export(final List<Event> main, final List<Event> moessingen, final List<Event> bodelshausen,
			final List<Event> dusslingen, final LocalDate exportMonth) throws IOException {

		final List<ColumnGenerator> setup = ToHtmlTransformator.setup(main, moessingen, bodelshausen, dusslingen, exportMonth.getYear(), exportMonth.getMonth());
		final String html = ToHtmlTransformator.toHtml(setup);

		final File file = new File(String.format("%s.html", DateTimeFormatter.ofPattern("yyyy-MM").format(exportMonth)));
		LOGGER.info("Writing {}", file.getAbsolutePath());

		FileUtils.writeStringToFile(file, html, Charset.forName("utf8"));

		return html;
	}

	private static List<Event> load(final String surl) throws EventParseException {
		try (InputStream inputStream = ICSDownloader.download(surl, HttpClients::createDefault)) {
			return EventFactory.fromICal(inputStream);
		}
		catch (final IOException e) {
			throw new EventParseException(e);
		}
	}

}
