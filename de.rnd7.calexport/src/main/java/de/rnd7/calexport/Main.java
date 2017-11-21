package de.rnd7.calexport;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			if (args.length != 4) {
				LOGGER.error("Expected 4 arguments (main, moessingen, bodelshausen, dusslingen)");

				return;
			}

			Exporter.export(LocalDate.now(), args[0], args[1], args[2], args[3], 16);
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
