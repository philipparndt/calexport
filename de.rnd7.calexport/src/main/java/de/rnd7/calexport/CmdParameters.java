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
	private static final String BACKUP_DATE = "backupDate";
	private static final String BACKUP_SKIP_DTSTAMP = "skipDTSTAMP";

	private static final Logger LOGGER = LoggerFactory.getLogger(CmdParameters.class);

	private final File configFile;
	private final File templateFile;
	private final boolean createBackup;
	private boolean addBackupDate;
	private boolean skipDtStamp;

	private CmdParameters(final File config, final File template, final boolean createBackup, final boolean addBackupDate, boolean skipDtStamp) {
		this.configFile = config;
		this.templateFile = template;
		this.createBackup = createBackup;
		this.addBackupDate = addBackupDate;
		this.skipDtStamp = skipDtStamp;
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

	public boolean isAddBackupDate() {
		return addBackupDate;
	}
	
	public boolean isSkipDtStamp() {
		return skipDtStamp;
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
		final boolean addBackupDate = line.hasOption(BACKUP_DATE);
		final boolean skipDtStamp = line.hasOption(BACKUP_SKIP_DTSTAMP);
		
		
		LOGGER.info("Config file: {}", configFile);
		LOGGER.info("Template file: {}", templateFile);

		return new CmdParameters(new File(configFile), new File(templateFile), createBackup, addBackupDate, skipDtStamp);
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

		options.addOption(Option.builder(BACKUP_DATE)
				.desc("Create backup files with backup date in file names")
				.build());

		options.addOption(Option.builder(BACKUP_SKIP_DTSTAMP)
				.desc("Normalize the dtstamp in the calendar to avoid every element to be changed")
				.build());
		
		return options;
	}
}
