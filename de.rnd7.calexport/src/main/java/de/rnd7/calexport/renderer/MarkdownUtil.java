package de.rnd7.calexport.renderer;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import j2html.tags.DomContent;

public class MarkdownUtil {

	public static DomContent convertMarkdown(final String text) {
		final Parser parser = Parser.builder().build();
		final Node document = parser.parse(text);
		final HtmlRenderer renderer = HtmlRenderer.builder().build();
		final String rendered = renderer.render(document).trim();

		final String withoutP = rendered.substring(3, rendered.length()-4);

		return new DomContent() {
			@Override
			public String render() {
				return withoutP;
			}
		};
	}

}
