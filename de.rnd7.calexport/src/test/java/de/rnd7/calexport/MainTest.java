package de.rnd7.calexport;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Options;
import org.junit.Test;

public class MainTest {
	@Test
	public void test_options() throws Exception {
		final Options options = Main.buildOptions();
		assertNotNull(options.getOption(Main.CONFIG));
		assertTrue(options.getOption(Main.CONFIG).isRequired());
		assertNotNull(options.getOption(Main.TEMPLATE));
		assertTrue(options.getOption(Main.TEMPLATE).isRequired());
	}
}
