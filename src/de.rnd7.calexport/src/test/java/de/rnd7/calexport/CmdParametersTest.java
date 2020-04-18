package de.rnd7.calexport;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class CmdParametersTest {
	@Test(expected=ParseException.class)
	public void test_missing_template() throws Exception {
		CmdParameters.parse(new String[]{ "-config", "B"});
	}

	@Test(expected=ParseException.class)
	public void test_missing_config() throws Exception {
		CmdParameters.parse(new String[]{"-config", "B"});
	}

	@Test
	public void test_ok() throws Exception {
		final CmdParameters parameters = CmdParameters.parse(new String[]{"-template", "A", "-config", "B"});
		assertEquals("A", parameters.getTemplate().getName());
		assertEquals("B", parameters.getConfig().getName());
	}

}
