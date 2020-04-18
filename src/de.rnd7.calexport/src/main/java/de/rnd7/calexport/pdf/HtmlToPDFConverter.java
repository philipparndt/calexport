package de.rnd7.calexport.pdf;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class HtmlToPDFConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlToPDFConverter.class);

	private HtmlToPDFConverter() {
	}

	public static void exportToPdfBox(final String html, final OutputStream out) {
		try {
			// There are more options on the builder than shown below.
			final PdfRendererBuilder builder = new PdfRendererBuilder();

			builder.withHtmlContent(html, "localhost");
			builder.toStream(out);
			builder.run();

		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}