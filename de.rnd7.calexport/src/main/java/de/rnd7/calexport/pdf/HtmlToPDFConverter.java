package de.rnd7.calexport.pdf;

import java.io.OutputStream;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class HtmlToPDFConverter {

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
			e.printStackTrace();
			// LOG exception
		}
	}
}