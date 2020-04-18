package de.rnd7.calexport.renderer;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.tfoot;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import de.rnd7.calexport.config.Calconfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import j2html.Config;
import j2html.tags.ContainerTag;

public class ToHtmlTransformator {

	private ToHtmlTransformator() {
	}

	public static String toHtml(final Calconfig config, final String ftltemplate, final List<ColumnGenerator> generators, final int year, final Month month) throws IOException {
		Config.closeEmptyTags = true;

		final Map<String, Object> data = initData(config, year, month);
		data.put("Table", processTemplate(data, buildTable(generators, year, month)));

		return processTemplate(data, ftltemplate);
	}

	private static final String processTemplate(final Map<String, Object> data, final String ftltemplate) throws IOException {
		try {
			final Configuration cfg = new Configuration();
			final Template template = new Template("templateName", new StringReader(ftltemplate), cfg);
			final StringWriter out = new StringWriter();
			template.process(data, out);
			out.flush();

			return out.toString();

		} catch (final TemplateException e) {
			throw new IOException(e);
		}
	}

	private static String buildTable(final List<ColumnGenerator> generators, final int year, final Month month) {
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

		return table(
				createHeader(generators),
				tbody,
				createFooter(generators))
				.renderFormatted();
	}

	private static Map<String, Object> initData(final Calconfig config, final int year, final Month month) {
		final Map<String, Object> data = new HashMap<>();
		final LocalDate date = LocalDate.of(year, month.getValue(), 1);
		data.put("Year", DateTimeFormatter.ofPattern("YYYY").format(date));
		data.put("Month", DateTimeFormatter.ofPattern("MM").format(date));
		data.put("MonthName", DateTimeFormatter.ofPattern("MMMM").format(date));
		data.put("Title", config.getTitle());
		Optional.ofNullable(System.getProperty("ExampleURL")).ifPresent(url -> data.put("ExampleURL", url));

		return data;
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

	private static String dayClass(final LocalDate date) {
		return "." + date.getDayOfWeek().name().toLowerCase();
	}

}
