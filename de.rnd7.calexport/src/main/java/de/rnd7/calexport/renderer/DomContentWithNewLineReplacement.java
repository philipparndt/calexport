package de.rnd7.calexport.renderer;

import j2html.tags.DomContent;

public class DomContentWithNewLineReplacement extends DomContent {

	private final String content;

	public DomContentWithNewLineReplacement(final String content) {
		this.content = content;
	}

	@Override
	public String render() {
		return this.content.replace("\n", "<br/>");
	}

}
