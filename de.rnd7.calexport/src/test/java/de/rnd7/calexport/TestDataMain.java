package de.rnd7.calexport;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class TestDataMain extends TestData {

	@Override
	public List<Event> create() {
		return Arrays.asList(
				new Event("Lorem ipsum dolor")
				.setStart(of("2017-10-01"))
				.setDuration(Duration.ofDays(7)),

				new Event("Lorem ipsum dolor ")
				.setStart(of("2017-10-27"))
				.setDuration(Duration.ofDays(3)),

				new Event("Urlaub Lorem")
				.setStart(of("2017-10-23"))
				.setEnd(of("2017-11-05")),

				new Event ("Tag d.dt.Einheit")
				.setStart(of("2017-10-03"))
				,

				new Event ("onsectetur adipisicing elit")
				.setStart (of("2017-10-13"))
				.setDuration(Duration.ofDays(2))
				,

				new Event("tempoßr incididunt ut")
				.setStart(of("2017-10-11", "19:30")),

				new Event("Ök. quis nostrud exercitation")
				.setStart(of("2017-10-12", "16:00")),


				new Event("Herbstfereien")
				.setStart(of("2017-10-30")),

				new Event("500 Jahre Reformation")
				.setStart(of("2017-10-31"))
				);
	}

}
