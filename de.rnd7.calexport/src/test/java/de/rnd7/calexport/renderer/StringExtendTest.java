package de.rnd7.calexport.renderer;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringExtendTest {
	@Test
	public void test_extend() throws Exception {
		assertTrue(StringExtend.getWidth("Testeintrag für eine Woche") > 90);
		assertTrue(StringExtend.getWidth("Test") > 12);
		assertTrue(StringExtend.getWidth("Test") < 20);

		assertTrue(StringExtend.getWidthEm("Testeintrag für eine Woche") > 10);
		assertTrue(StringExtend.getWidth("Test") > 1);
		assertTrue(StringExtend.getWidth("Test") < 8);
	}
}