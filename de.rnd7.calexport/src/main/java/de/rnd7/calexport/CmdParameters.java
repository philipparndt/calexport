package de.rnd7.calexport;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdParameters {
	private static final String CONFIG = "config";
	private static final String TEMPLATE = "template";
	private static final Logger LOGGER = LoggerFactory.getLogger(CmdParameters.class);

	private final File config;
	private final File template;

	private CmdParameters(final File config, final File template) {
		this.config = config;
		this.template = template;
	}

	public File getConfig() {
		return this.config;
	}

	public File getTemplate() {
		return this.template;
	}

	static CmdParameters parse(final String[] args) throws ParseException{
		// create Options object
		final Options options = buildOptions();

		// create the parser
		final CommandLineParser parser = new DefaultParser();
		// parse the command line arguments
		final CommandLine line = parser.parse(options, args);

		final String configFile = line.getOptionValue(CONFIG);
		final String templateFile = line.getOptionValue(TEMPLATE);

		LOGGER.info("Config file: {}", configFile);
		LOGGER.info("Template file: {}", templateFile);

		return new CmdParameters(new File(configFile), new File(templateFile));
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
}
