package de.rnd7.calexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.config.Calconfig.Calendar;

public class BackupCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackupCommand.class);
	private boolean addDate;
	private boolean skipDtStamp;
	
	private static final Pattern dtPattern = Pattern.compile("DTSTAMP:\\d{8}T\\d{6}Z");

	public BackupCommand(boolean addDate, boolean skipDtStamp) {
		this.addDate = addDate;
		this.skipDtStamp = skipDtStamp;
	}

	public void execute(final Calendar calendar) throws IOException{
		try (InputStream in = ICSDownloader.download(calendar.getUrl(), HttpClients::createDefault)) {
			createBackup(IOUtils.toString(in, StandardCharsets.UTF_8), this.getName(calendar));
		}
	}

	private String getName(final Calendar calendar) {
		final String id = calendar.getId();
		return id == null ? calendar.getName() : id;
	}

	private void createBackup(final String ics, final String name) throws IOException {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
		final String backupDate = this.addDate ? "_" + LocalDateTime.now().format(formatter) : "";
		final String sanitizeFilename = cleanFilename(name);

		final String fileName = String.format("backup%s_%s.ical", backupDate, sanitizeFilename);
		final File file = new File(fileName);
		LOGGER.info("Creating backup: {}", file);
		FileUtils.writeStringToFile(file, cleanup(ics), StandardCharsets.UTF_8);

	}

	private String cleanup(final String ics) {
		String result = ics;
		
		if (skipDtStamp) {
			Matcher matcher = dtPattern.matcher(ics);
			result = matcher.replaceAll("DTSTAMP:20190101T000000Z");
		}
		
		return result;
	}

	private static String cleanFilename(final String inputName) {
		return inputName.replaceAll("[^a-zA-Z0-9-_\\.öäüß]", "_");
	}
}
