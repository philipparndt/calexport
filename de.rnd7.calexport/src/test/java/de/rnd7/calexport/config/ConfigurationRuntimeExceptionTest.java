package de.rnd7.calexport.config;

import org.junit.Test;

public class ConfigurationRuntimeExceptionTest {
	@Test(expected=ConfigurationRuntimeException.class)
	public void test_instance() throws Exception {
		throw new ConfigurationRuntimeException(new Throwable());
	}
}
