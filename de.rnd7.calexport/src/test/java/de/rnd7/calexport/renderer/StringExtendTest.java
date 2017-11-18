package de.rnd7.calexport.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringExtendTest {
	@Test
	public void test_extend() throws Exception {
		assertTrue(StringExtend.getWidth("Testeintrag für eine Woche") > 90);
		assertEquals(15, StringExtend.getWidth("Test"));

		assertTrue(StringExtend.getWidthEm("Testeintrag für eine Woche") > 10);
		assertEquals(2, StringExtend.getWidthEm("Test"));
	}
}
