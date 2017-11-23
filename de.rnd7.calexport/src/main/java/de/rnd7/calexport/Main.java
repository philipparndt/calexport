package de.rnd7.calexport;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rnd7.calexport.config.Calconfig;
import de.rnd7.calexport.config.Configuration;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		if (args.length != 1) {
			LOGGER.error("Expected 1 argument (configuration file)");

			return;
		}

		try (InputStream in = new FileInputStream(args[0])) {

			final Calconfig config = Configuration.loadFrom(in);

			Exporter.export(LocalDate.now(), config);
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
