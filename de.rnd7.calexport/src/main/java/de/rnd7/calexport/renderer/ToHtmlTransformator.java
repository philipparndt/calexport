package de.rnd7.calexport.renderer;

import static j2html.TagCreator.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableList;

import de.rnd7.calexport.Event;
import j2html.Config;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

public class ToHtmlTransformator {

	static final DomContent ABENDMAHL = img()
			.attr("width", 35)
			.attr("height", 35)
			.attr("alt", "Abendmahl")
			.attr("src", loadImage("abendmahl.png"));

	private ToHtmlTransformator() {
	}

	public static List<ColumnGenerator> setup(final List<Event> main, final List<Event> moessingen, final List<Event> bodelshausen, final List<Event> dusslingen, final int year, final Month month) {
		return ImmutableList.of(
				new ColumnGeneratorDay(main, year, month, ".date"),
				new ColumnGeneratorLocation(moessingen, "Mössingen\nChristuskirche", year, month, ".moessingen"),
				new ColumnGeneratorLocation(bodelshausen, "Bodelshausen\nFriedenskirche", year, month, ".bodelshausen"),
				new ColumnGeneratorLocation(dusslingen, "Dusslingen\nFriedenskirche", year, month, ".dusslingen"));

	}

	public static String toHtml(final List<ColumnGenerator> generators) throws IOException {
		Config.closeEmptyTags = true;

		final int year = generators.iterator().next().getYear();
		final Month month = generators.iterator().next().getMonth();

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

		final ContainerTag title = div("Evangelisch-methodistische Kirche, Bezirk Mössingen").withClass("title");

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

	private static String loadImage(final String imageName) {
		try (InputStream in = ToHtmlTransformator.class.getResourceAsStream(imageName)) {
			return "data:image/gif;base64," + new String(Base64.getEncoder().encode(IOUtils.toByteArray(in)));
		}
		catch (final IOException e) {
			throw new RuntimeException("Error loading image " + imageName, e);
		}
	}

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
