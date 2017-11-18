package de.rnd7.calexport.renderer;

import static de.rnd7.calexport.renderer.MarkdownUtil.convertMarkdown;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import de.rnd7.calexport.Event;
import j2html.TagCreator;
import j2html.tags.ContainerTag;

public abstract class ColumnGenerator {

	private static final int MIN_EM_PER_ROW = 2;
	private static final double MAX_EM_PER_ROE = 4.5;
	protected static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
	protected static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd. EE");
	protected static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private final String title;

	private final Map<LocalDate, List<Event>> byDay;
	private final Map<LocalDate, List<Event>> byDaySingleDay;

	private final int year;
	private final Month month;
	private final MultiEventAggregator aggregator;

	public ColumnGenerator(final List<Event> events, final String title, final int year, final Month month) {
		this.title = title;
		this.year = year;
		this.month = month;

		this.byDay = events.stream()
				.filter(e -> !e.isWholeDay())
				.collect(Collectors.groupingBy(Event::getStartDayInclusive));

		this.byDaySingleDay = events.stream()
				.filter(Event::isWholeDay)
				.filter(event -> event.getDuration().toDays() == 1L)
				.collect(Collectors.groupingBy(Event::getStartDayInclusive));


		this.aggregator = new MultiEventAggregator(events, year, month);
	}

	public Month getMonth() {
		return this.month;
	}

	public int getYear() {
		return this.year;
	}

	public String getTitle() {
		return this.title;
	}


	protected ContainerTag colspanHeadFoot(final ContainerTag td) {
		final int colspan = this.aggregator.getColspan();
		if (colspan > 1) {
			td.attr("colspan", colspan);
		}

		return td;
	}

	public abstract ContainerTag createHeader();

	public ContainerTag createFooter() {
		final ContainerTag td = td();
		this.colspanHeadFoot(td);
		return td;
	}

	protected List<ContainerTag> collectSingleDayEvents(final Map<LocalDate, List<Event>> byDaySingleDay,
			final LocalDate date) {
		return this.stream(byDaySingleDay, date)
				.map(this::convertSingelDayEventToString)
				.sorted()
				.map(content -> div(attrs(".single-day"), content))
				.collect(Collectors.toList());
	}

	protected ContainerTag addNormalEventClass(final ContainerTag normalEvent) {
		return normalEvent;
	}

	protected Stream<Event> stream(final Map<LocalDate, List<Event>> byDay, final LocalDate date) {
		final List<Event> list = byDay.get(date);
		if (list != null) {
			return list.stream();
		}
		else {
			return Stream.empty();
		}
	}

	protected String convertToString(final Event event) {
		return String.format("%s %s",
				ColumnGenerator.HOUR_FORMATTER.format(event.getStart()),
				event.getTitle()
				);
	}

	protected String convertSingelDayEventToString(final Event event) {
		return String.format("%s", event.getTitle());
	}

	public void writeDay(final ContainerTag line, final int day) {
		final ContainerTag first = td(attrs(".leftBorder"));
		line.with(first);

		for (final List<Event> byColumn : this.aggregator.getEventsByColum()) {
			final Optional<Event> findFirst = byColumn.stream().filter(event -> event.affects(this.year, this.month, day)).findFirst();
			if (findFirst.isPresent()) {
				final Event event = findFirst.get();
				if (event.isFirstInRange(this.year, this.month, day)) {
					final int remainingInMonthFrom = event.remainingInMonthFrom(this.year, this.month, day);
					final ContainerTag multiDayParagraph = p(span(convertMarkdown(event.getTitle())));
					final int widthEm = StringExtend.getWidthEm(event.getTitle());
					final int em =  Math.max(remainingInMonthFrom * MIN_EM_PER_ROW, Math.min(new Double(remainingInMonthFrom * MAX_EM_PER_ROE).intValue(), widthEm));

					multiDayParagraph.attr("style", String.format("height: %dem;", em));

					final ContainerTag td = td(attrs(".multi-day"), multiDayParagraph);
					td.attr("rowspan", remainingInMonthFrom);

					line.with(td);
				}
			}
			else {
				line.with(td());
			}
		}

		final LocalDate date = LocalDate.of(this.year, this.month.getValue(), day);


		final Stream<Event> eventsOfDay = Streams.concat(this.stream(this.byDaySingleDay, date), this.stream(this.byDay, date));
		ContainerTag target=first;

		if (eventsOfDay.anyMatch(Event::isAbendmahl)) {

			final ContainerTag td = td(attrs(".image"));
			td.attr("width", "100%");
			target = td;

			final ContainerTag table = table(attrs(".image")).with(tr(attrs(".image")).with(td).with(td(attrs(".image")).with(ToHtmlTransformator.ABENDMAHL)));
			first.with(table);
		}

		this.addDate(target, date);
		this.addEventsWithStartTime(target, date);
		this.addWholeDayEvents(target, date);
	}

	protected void addDate(final ContainerTag first, final LocalDate date) {
	}

	private ContainerTag addEventsWithStartTime(final ContainerTag first, final LocalDate date) {
		return first.with(this.stream(this.byDay, date)
				.map(this::convertToString)
				.sorted()
				.map(MarkdownUtil::convertMarkdown)
				.map(TagCreator::div)
				.map(this::addNormalEventClass)
				.collect(Collectors.toList()));
	}

	private void addWholeDayEvents(final ContainerTag first, final LocalDate date) {
		first.with(this.collectSingleDayEvents(this.byDaySingleDay, date));
	}
}
