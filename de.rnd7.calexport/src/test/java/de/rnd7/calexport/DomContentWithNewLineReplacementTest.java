package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rnd7.calexport.renderer.DomContentWithNewLineReplacement;

public class DomContentWithNewLineReplacementTest {
	@Test
	public void test_replace() throws Exception {
		final DomContentWithNewLineReplacement content = new DomContentWithNewLineReplacement("foo\nbar");

		assertEquals("foo<br/>bar", content.toString());
	}
}
