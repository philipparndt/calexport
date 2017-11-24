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
	private final String classes;

	public ColumnGeneratorDay(final List<Event> events, final String title, final int year, final Month month, final String classes) {
		super(events, title, year, month);
		this.classes = classes;
	}

	@Override
	public ContainerTag createHeader() {
		return this.colspanHeadFoot(td(attrs(".dayColumn .leftBorder " + this.classes), new DomContentWithNewLineReplacement(this.getTitle())));
	}

	@Override
	protected ContainerTag addNormalEventClass(final ContainerTag normalEvent) {
		normalEvent.withClass("single-day");
		return normalEvent;
	}

	@Override
	protected void addDate(final ContainerTag first, final LocalDate date) {
		first.with(div(ColumnGenerator.DAY_FORMATTER.format(date)));
	}


}
