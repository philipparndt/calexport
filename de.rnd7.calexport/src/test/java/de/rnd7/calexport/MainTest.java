package de.rnd7.calexport;

import java.net.URL;

import org.junit.Test;

import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class MainTest {
	@Test
	public void test_integration() throws Exception {
		final String template = ToHtmlTransformator.class.getResource("template.ftl").getFile();
		final String config = MainTest.class.getResource("exampleConfig.xml").getFile();

		final URL url = MainTest.class.getResource("simple.ics");
		System.setProperty("ExampleURL", url.toExternalForm());
		Main.main(new String[]{"-template", template, "-config", config});
	}
}
