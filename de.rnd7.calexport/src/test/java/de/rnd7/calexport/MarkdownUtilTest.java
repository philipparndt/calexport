package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rnd7.calexport.renderer.MarkdownUtil;

public class MarkdownUtilTest {
	@Test
	public void test_bold() throws Exception {
		assertEquals("<strong>Hello</strong> world", MarkdownUtil.convertMarkdown("**Hello** world").render());
	}

	@Test
	public void test_multi_bold() throws Exception {
		assertEquals("<strong>Hello</strong> world <strong>foo</strong> bar", MarkdownUtil.convertMarkdown("**Hello** world **foo** bar").render());
	}
}
