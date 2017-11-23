package de.rnd7.calexport.renderer;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.img;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.tfoot;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;

import de.rnd7.calexport.config.Calconfig;
import j2html.Config;
import j2html.tags.ContainerTag;

public class ToHtmlTransformator {

	private ToHtmlTransformator() {
	}

	public static String toHtml(final Calconfig config, final List<ColumnGenerator> generators, final int year, final Month month) throws IOException {
		Config.closeEmptyTags = true;

		final int days = month.length(Year.isLeap(year));

		final ContainerTag tbody = tbody();

		IntStream.range(1, days + 1).forEach(day -> {
			final LocalDate date = LocalDate.of(year, month.getValue(), day);

			final ContainerTag line = tr(attrs(ToHtmlTransformator.dayClass(date)));
			tbody.with(line);

			for (final ColumnGenerator generator : generators) {
				generator.writeDay(line, day);
			}
		});

		final ContainerTag table = table(
				createHeader(generators),
				tbody,
				createFooter(generators));


		final ContainerTag titleDate = div(ColumnGenerator.MONTH_FORMATTER.format(LocalDate.of(year, month.getValue(), 1))).withClass("titleDate");

		final ContainerTag title = div(config.getTitle()).withClass("title");

		final ContainerTag html = html(
				head(
						meta().attr("charset", "utf-8"),
						style(ToHtmlTransformator.styles()),
						title(String.format("%04d-%02d (Dienstplan)", year, month.getValue()))
						),
				body().with(titleDate, title, table));


		html.attr("xmlns", "http://www.w3.org/1999/xhtml");
		html.attr("xml:lang", "de");
		html.attr("lang", "de");

		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n"
		+ "        \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"
		+ html.renderFormatted();
	}


	//
	//	private static String loadImage(final String imageName) {
	//		try (InputStream in = ToHtmlTransformator.class.getResourceAsStream(imageName)) {
	//			return "data:image/gif;base64," + new String(Base64.getEncoder().encode(IOUtils.toByteArray(in)));
	//		}
	//		catch (final IOException e) {
	//			throw new RendererRuntimeException("Error loading image " + imageName, e);
	//		}
	//	}

	private static ContainerTag createHeader(final List<ColumnGenerator> generators) {
		final ContainerTag thead = thead();
		final ContainerTag headTr = tr();
		generators.stream().map(ColumnGenerator::createHeader).forEach(headTr::with);
		thead.with(headTr);
		return thead;
	}

	private static ContainerTag createFooter(final List<ColumnGenerator> generators) {
		final ContainerTag tfoot = tfoot();
		final ContainerTag footTr = tr(attrs(".noBorder"));
		generators.stream().map(ColumnGenerator::createFooter).forEach(footTr::with);
		tfoot.with(footTr);
		return tfoot;
	}

	private static String styles() throws IOException {
		try (InputStream in = ToHtmlTransformator.class.getResourceAsStream("style.css")) {
			return IOUtils.toString(in, Charset.forName("utf8"));
		}
	}

	private static String dayClass(final LocalDate date) {
		return "." + date.getDayOfWeek().name().toLowerCase();
	}

}
