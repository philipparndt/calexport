package de.rnd7.calexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.Configuration;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			final CmdParameters parameters = CmdParameters.parse(args);
			runExport(parameters.getConfig(), parameters.getTemplate());
		}
		catch( final ParseException exp ) {
			LOGGER.error(exp.getMessage());
		}
	}

	private static void runExport(final File configFile, final File templateFile) {
		try (InputStream in = new FileInputStream(configFile)) {
			final Calconfig config = Configuration.loadFrom(in);
			final String template = loadTemplate(templateFile);
			Exporter.export(LocalDate.now(), config, template);
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static String loadTemplate(final File file) throws IOException {
		try (InputStream in = new FileInputStream(file)) {
			return IOUtils.toString(in, CharEncoding.UTF_8);
		}
	}

}
