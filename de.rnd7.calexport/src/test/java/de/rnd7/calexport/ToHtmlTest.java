package de.rnd7.calexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Month;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
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
		tag.setWidth(16);
		tag.setHeight(16);
		tag.setName("A");
		tag.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABG2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS41LjAiPgogPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIi8+CiA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgo8P3hwYWNrZXQgZW5kPSJyIj8+Gkqr6gAAAYFpQ0NQc1JHQiBJRUM2MTk2Ni0yLjEAACiRdZHLS0JBFIc/tTDSKLCFixYS1qqngdQmSAkLJMQMem305iPwcblXCWkbtBUKoja9FvUX1DZoHQRFEUTQrnVRm4rbuRkYkWc4c775zZzDzBmwxrJKTm8YgFy+qEVDAc/s3LzH/ogDN3b6ccUVXR2LRMLUtbcbLGa86jVr1T/3rzmWkroClibhUUXVisITwuGVomrypnC7kokvCR8L92hyQeFrU09U+cnkdJU/TNZi0SBY24Q96V+c+MVKRssJy8vx5rIl5ec+5kucyfzMtMRO8Q50ooQI4GGScYL4GWREZj+9+OiTFXXyB77zpyhIriKzShmNZdJkKNIjakmqJyWmRE/KyFI2+/+3r3pqyFet7gxA44NhvHSBfQM+K4bxvm8Ynwdgu4ezfC2/sAfDr6JXapp3F1rX4OS8piW24HQd3HdqXIt/SzZxayoFz0fQMgeuS2heqPbsZ5/DW4itylddwPYOdMv51sUvWo1n4AHZgzEAAAAJcEhZcwAACxMAAAsTAQCanBgAAACTSURBVDiNpZPBDYMwDEUf3JtpeuCcrgMLwBbtUjlkiPYCAzAAXIIUaByI+dK/WPovlmPDTVWJmgE6oAGeoeYBB7yBOQd8AT9gEfwFbC4sBY/+g5iTl1OdmBgwFIQ39wB1ADS5wQjaZSZFB2PcgVobwCuyPgY4BWCX0Xzj40i0BQBxG22gX15l6Zha0sf04eSYirUCp/1jZXZBmZEAAAAASUVORK5CYII=");
		config.getHashtag().add(tag);
		ImageTags.initTags(config);


		final List<Event> main = this.read("main.txt");
		final List<Event> a = this.read("a.txt");
		final List<Event> b = this.read("b.txt");
		final List<Event> c = this.read("c.txt");
		final List<ColumnGenerator> setup =
				ImmutableList.of(
						new ColumnGeneratorDay(main, "Main", 2017, Month.OCTOBER, ".date"),
						new ColumnGeneratorLocation(a, "A\nline2", 2017, Month.OCTOBER, ".a"),
						new ColumnGeneratorLocation(b, "B\nline2", 2017, Month.OCTOBER, ".b"),
						new ColumnGeneratorLocation(c, "C\nline2", 2017, Month.OCTOBER, ".c"));


		final String html = ToHtmlTransformator.toHtml(config, Exporter.defaultTemplate(), setup, 2017, Month.OCTOBER);
		FileUtils.writeStringToFile(new File("test.html"), html, CharEncoding.UTF_8);

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
