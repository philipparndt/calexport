package de.rnd7.calexport.renderer;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringExtendTest {
	@Test
	public void test_extend() throws Exception {
		System.out.println(StringExtend.getWidth("Testeintrag für eine Woche"));
		System.out.println(StringExtend.getWidth("Test"));

		assertTrue(StringExtend.getWidth("Testeintrag für eine Woche") > 90);
		assertTrue(StringExtend.getWidth("Test") > 12);
		assertTrue(StringExtend.getWidth("Test") < 30);

		assertTrue(StringExtend.getWidthEm("Testeintrag für eine Woche") > 10);
		assertTrue(StringExtend.getWidthEm("Test") > 1);
		assertTrue(StringExtend.getWidthEm("Test") < 5);
	}
}