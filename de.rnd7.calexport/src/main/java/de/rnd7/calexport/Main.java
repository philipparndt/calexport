package de.rnd7.calexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.Configuration;

public class Main {

	static final String CONFIG = "config";
	static final String TEMPLATE = "template";

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {

		// create Options object
		final Options options = buildOptions();

		// create the parser
		final CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			final CommandLine line = parser.parse(options, args);

			final String configFile = line.getOptionValue(CONFIG);
			final String templateFile = line.getOptionValue(TEMPLATE);

			LOGGER.info("Config file: {}", configFile);
			LOGGER.info("Template file: {}", templateFile);

			runExport(configFile, templateFile);
		}
		catch( final ParseException exp ) {
			LOGGER.error(exp.getMessage());
		}
	}

	private static void runExport(final String configFile, final String templateFile) {
		try (InputStream in = new FileInputStream(configFile)) {
			final Calconfig config = Configuration.loadFrom(in);
			final String template = loadTemplate(new File(templateFile));
			Exporter.export(LocalDate.now(), config, template);
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	static Options buildOptions() {
		final Options options = new Options();
		options.addOption(Option.builder(TEMPLATE)
				.hasArg()
				.required()
				.desc("Freemarker template file")
				.build());

		options.addOption(Option.builder(CONFIG)
				.hasArg()
				.required()
				.desc("Configuration file")
				.build());
		return options;
	}

	private static String loadTemplate(final File file) throws IOException {
		try (InputStream in = new FileInputStream(file)) {
			return IOUtils.toString(in, CharEncoding.UTF_8);
		}
	}

}
