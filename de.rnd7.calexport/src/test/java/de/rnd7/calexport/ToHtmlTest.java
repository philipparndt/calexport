package de.rnd7.calexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Month;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.Calconfig.Hashtag;
import de.rnd7.calexport.pdf.HtmlToPDFConverter;
import de.rnd7.calexport.renderer.ColumnGenerator;
import de.rnd7.calexport.renderer.ColumnGeneratorDay;
import de.rnd7.calexport.renderer.ColumnGeneratorLocation;
import de.rnd7.calexport.renderer.ImageTags;
import de.rnd7.calexport.renderer.ToHtmlTransformator;

public class ToHtmlTest {
	@Test
	public void test_days() throws Exception {
		final Calconfig config = new Calconfig();
		config.setTitle("Title");

		final Hashtag tag = new Hashtag();
		tag.setWidth(35);
		tag.setHeight(35);
		tag.setName("A");
		tag.setImage("data:image/gif;base64,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");
		config.getHashtag().add(tag);
		ImageTags.initTags(config);


		final List<Event> main = this.read("main.txt");
		final List<Event> a = this.read("a.txt");
		final List<Event> b = this.read("b.txt");
		final List<Event> c = this.read("c.txt");
		final List<ColumnGenerator> setup =
				ImmutableList.of(
						new ColumnGeneratorDay(main, 2017, Month.OCTOBER, ".date"),
						new ColumnGeneratorLocation(a, "A\nline2", 2017, Month.OCTOBER, ".a"),
						new ColumnGeneratorLocation(b, "B\nline2", 2017, Month.OCTOBER, ".b"),
						new ColumnGeneratorLocation(c, "C\nline2", 2017, Month.OCTOBER, ".c"));


		final String html = ToHtmlTransformator.toHtml(config, setup, 2017, Month.OCTOBER);
		FileUtils.writeStringToFile(new File("test.html"), html, Charset.forName("utf8"));

		try(OutputStream out = new FileOutputStream(new File("test.pdf"))) {
			HtmlToPDFConverter.exportToPdfBox(html, out);
		}
	}

	private List<Event> read(final String name) throws IOException {
		try(InputStream in = ToHtmlTest.class.getResourceAsStream(name)) {
			return EventFactory.fromFlatFile(in);
		}
	}
}
