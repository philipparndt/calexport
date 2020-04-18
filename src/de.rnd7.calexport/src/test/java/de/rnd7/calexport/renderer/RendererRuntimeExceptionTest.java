package de.rnd7.calexport.renderer;

import org.junit.Test;

public class RendererRuntimeExceptionTest {
	@Test(expected=RendererRuntimeException.class)
	public void test_instance() throws Exception {
		throw new RendererRuntimeException("foo", new Throwable());
	}
}
