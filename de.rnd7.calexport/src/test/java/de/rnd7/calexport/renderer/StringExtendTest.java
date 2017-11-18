package de.rnd7.calexport.renderer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringExtendTest {
	@Test
	public void test_extend() throws Exception {
		assertEquals(96, StringExtend.getWidth("Testeintrag für eine Woche"));
		assertEquals(15, StringExtend.getWidth("Test"));

		assertEquals(12, StringExtend.getWidthEm("Testeintrag für eine Woche"));
		assertEquals(2, StringExtend.getWidthEm("Test"));
	}
}
