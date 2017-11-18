package de.rnd7.calexport.renderer;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.td;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import de.rnd7.calexport.Event;
import j2html.tags.ContainerTag;

public class ColumnGeneratorDay extends ColumnGenerator {
	public ColumnGeneratorDay(final List<Event> events, final int year, final Month month) {
		super(events, toMonthName(year, month), year, month);
	}

	@Override
	public ContainerTag createHeader() {
		return this.colspanHeadFoot(td(attrs(".dayColumn .leftBorder"), new DomContentWithNewLineReplacement(this.getTitle())));
	}

	@Override
	protected ContainerTag addNormalEventClass(final ContainerTag normalEvent) {
		normalEvent.withClass("single-day");
		return normalEvent;
	}

	private static String toMonthName(final int year, final Month month) {
		return ColumnGenerator.MONTH_FORMATTER.format(LocalDate.of(year, month.getValue(), 1));
	}

	@Override
	protected void addDate(final ContainerTag first, final LocalDate date) {
		first.with(div(ColumnGenerator.DAY_FORMATTER.format(date)));
	}


}
