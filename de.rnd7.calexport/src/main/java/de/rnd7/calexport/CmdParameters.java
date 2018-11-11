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
	private static final String BACKUP = "backup";

	private static final Logger LOGGER = LoggerFactory.getLogger(CmdParameters.class);

	private final File configFile;
	private final File templateFile;
	private final boolean createBackup;

	private CmdParameters(final File config, final File template, final boolean createBackup) {
		this.configFile = config;
		this.templateFile = template;
		this.createBackup = createBackup;
	}

	public File getConfig() {
		return this.configFile;
	}

	public File getTemplate() {
		return this.templateFile;
	}

	public boolean isCreateBackup() {
		return this.createBackup;
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

		final boolean createBackup = line.hasOption(BACKUP);

		LOGGER.info("Config file: {}", configFile);
		LOGGER.info("Template file: {}", templateFile);

		return new CmdParameters(new File(configFile), new File(templateFile), createBackup);
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

		options.addOption(Option.builder(BACKUP)
				.desc("Create backup files")
				.build());

		return options;
	}
}
