package de.rnd7.calexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.renderer.ColumnGenerator;
import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			if (args.length != 4) {
				LOGGER.error("Expected 4 arguments (main, moessingen, bodelshausen, dusslingen)");

				return;
			}

			final List<Event> main = load(args[0]);
			final List<Event> moessingen = load(args[1]);
			final List<Event> bodelshausen = load(args[2]);
			final List<Event> dusslingen = load(args[3]);

			final LocalDate month = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

			for (int i = 0; i < 12; i++) {
				final LocalDate exportMonth = month.plusMonths(i);

				export(main, moessingen, bodelshausen, dusslingen, exportMonth);
			}
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static void export(final List<Event> main, final List<Event> moessingen, final List<Event> bodelshausen,
			final List<Event> dusslingen, final LocalDate exportMonth) throws IOException {

		final List<ColumnGenerator> setup = ToHtmlTransformator.setup(main, moessingen, bodelshausen, dusslingen, exportMonth.getYear(), exportMonth.getMonth());
		final String html = ToHtmlTransformator.toHtml(setup);

		final File file = new File(String.format("%s.html", DateTimeFormatter.ofPattern("yyyy-MM").format(exportMonth)));
		LOGGER.info("Writing {}", file.getAbsolutePath());

		FileUtils.writeStringToFile(file, html, Charset.forName("utf8"));
	}

	private static List<Event> load(final String surl) throws Exception {
		try (InputStream inputStream = ICSDownloader.download(surl, HttpClients::createDefault)) {
			return EventFactory.fromICal(inputStream);
		}
	}

}
