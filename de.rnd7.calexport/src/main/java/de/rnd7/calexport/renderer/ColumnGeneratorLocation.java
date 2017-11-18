package de.rnd7.calexport.renderer;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.td;

import java.time.Month;
import java.util.List;

import de.rnd7.calexport.Event;
import j2html.tags.ContainerTag;

public class ColumnGeneratorLocation extends ColumnGenerator {
	public ColumnGeneratorLocation(final List<Event> events, final String title, final int year, final Month month) {
		super(events, title, year, month);
	}

	@Override
	public ContainerTag createHeader() {
		return this.colspanHeadFoot(td(attrs(".eventColumn .leftBorder"), new DomContentWithNewLineReplacement(this.getTitle())));
	}
}
