package de.rnd7.calexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Month;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.rnd7.calexport.pdf.HtmlToPDFConverter;
import de.rnd7.calexport.renderer.ColumnGenerator;
import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class ToHtmlTest {
	@Test
	public void test_days() throws Exception {
		final List<Event> main = new TestDataMain().create();
		final List<Event> moessingen = new TestDataMoessingen().create();
		final List<Event> bodelshausen = new TestDataBodelshausen().create();
		final List<Event> dusslingen = new TestDataDusslingen().create();

		final List<ColumnGenerator> setup = ToHtmlTransformator.setup(main, moessingen, bodelshausen, dusslingen, 2017, Month.OCTOBER);
		final String html = ToHtmlTransformator.toHtml(setup);
		FileUtils.writeStringToFile(new File("test.html"), html, Charset.forName("utf8"));

		try(OutputStream out = new FileOutputStream(new File("test.pdf"))) {
			HtmlToPDFConverter.exportToPdfBox(html, out);
		}
	}
}
