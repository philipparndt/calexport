package de.rnd7.calexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.Calconfig.Calendar;
import de.rnd7.calexport.renderer.ColumnGenerator;
import de.rnd7.calexport.renderer.ColumnGeneratorDay;
import de.rnd7.calexport.renderer.ColumnGeneratorLocation;
import de.rnd7.calexport.renderer.ImageTags;
import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class Exporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class);

	private static LoadingCache<Calendar, List<Event>> eventCache = CacheBuilder.newBuilder()
			.build(CacheLoader.from(Exporter::load));

	private Exporter() {
	}

	public static String defaultTemplate() throws IOException {
		try (InputStream in = ToHtmlTransformator.class.getResourceAsStream("template.ftl")) {
			return IOUtils.toString(in, CharEncoding.UTF_8);
		}
	}

	public static List<String> export(final LocalDate start, final Calconfig config) throws IOException {
		return export(start, config, defaultTemplate());
	}

	public static List<String> export(final LocalDate start, final Calconfig config, final String template) throws IOException {
		ImageTags.initTags(config);

		final LocalDate month = LocalDate.of(start.getYear(), start.getMonth(), 1);

		final List<String> result = new ArrayList<>();

		for (int i = 0; i < config.getExportmonths(); i++) {
			final LocalDate exportMonth = month.plusMonths(i);

			final List<ColumnGenerator> generators = config.getCalendar().stream()
					.map(calendar -> init(calendar, exportMonth.getYear(), exportMonth.getMonth()))
					.collect(Collectors.toList());


			result.add(export(config, template, generators, exportMonth));
		}

		return result;
	}

	private static final List<Event> load(final Calendar calendar) {
		try {
			return load(calendar.getUrl())
					.stream()
					.filter(event -> event.locationMatches(calendar.getLocationFilter()))
					.collect(Collectors.toList());
		} catch (final EventParseException e) {
			throw new EventParseRuntimeException(e);
		}
	}

	private static ColumnGenerator init(final Calendar calendar, final int year, final Month month) {
		try {
			final String classes = calendar.getClasses();
			final List<Event> events = eventCache.get(calendar);
			switch (calendar.getType()) {
			case LOCATION:
				return new ColumnGeneratorLocation(events, calendar.getName(), year, month, classes);
			case DATE:
				return new ColumnGeneratorDay(events, calendar.getName(), year, month, classes);
			default:
				throw new EventParseException("Unknown type: " + calendar.getType());
			}
		} catch (final Exception e) {
			throw new EventParseRuntimeException(e);
		}

	}

	private static String export(final Calconfig config, final String template, final List<ColumnGenerator> setup, final LocalDate exportMonth) throws IOException {
		final String html = ToHtmlTransformator.toHtml(config, template, setup, exportMonth.getYear(), exportMonth.getMonth());

		final File file = new File(String.format("%s.html", DateTimeFormatter.ofPattern("yyyy-MM").format(exportMonth)));
		LOGGER.info("Writing {}", file.getAbsolutePath());

		FileUtils.writeStringToFile(file, html, CharEncoding.UTF_8);

		return html;
	}

	private static List<Event> load(final String surl) throws EventParseException {
		String url = surl;
		if ("${ExampleURL}".equals(surl)) {
			url = System.getProperty("ExampleURL");
		}

		try (InputStream inputStream = ICSDownloader.download(url, HttpClients::createDefault)) {
			return EventFactory.fromICal(inputStream);
		}
		catch (final IOException e) {
			throw new EventParseException(e);
		}
	}


}
