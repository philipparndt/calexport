package de.rnd7.calexport;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class TestDataBodelshausen extends TestData {

	@Override
	public List<Event> create() {
		return Arrays.asList(


				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-01","09:30"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-01", "18:00"))
				,

				new Event ("Lorem ipsum dolor - Lorem ipsum dolor (Lorem/ipsum)")
				.setStart (of("2017-10-05","13:50"))
				,

				new Event ("**Lorem ipsum dolor** Lorem ipsum dolor")
				.setStart(of("2017-10-08","09:30"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-08", "18:00"))
				,

				new Event ("Lorem ipsum dolor Lorem ipsum dolor Lorem ipsum dolor")
				.setStart(of("2017-10-09","19:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-10","10:15"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-10","11:00"))
				,


				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-10","19:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-11"))
				,

				new Event ("Lorem ipsum dolorLorem ipsum dolor \"Lorem ipsum dolor\"")
				.setStart(of("2017-10-13","19:30"))
				,

				new Event ("Lorem ipsum dolorLorem ipsum dolorLorem ipsum dolorLorem ipsum")
				.setStart(of("2017-10-14","12:30"))
				,

				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-15","09:30"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-15", "18:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-17","19:30"))
				,

				new Event ("Lorem ipsum dolor (Lorem ipsum dolor)")
				.setStart(of("2017-10-17","19:30"))
				,

				new Event ("Lorem ipsum dolor (WD)")
				.setStart(of("2017-10-18","20:00"))
				,

				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-22","09:30"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-22", "18:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-25","20:00"))
				,

				new Event ("**Lorem ipsum dolorLorem ipsum dolor** mit **Lorem ipsum dolor**, Lorem ipsum dolor")
				.setStart(of("2017-10-29","09:30"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-29", "18:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-30","19:30"))
				,

				new Event ("ÖLorem ipsum dolor (Lorem ipsum dolor)")
				.setStart(of("2017-10-31","10:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-31","19:30")),

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-29"))
				.setDuration(Duration.ofDays(3))


				);
	}

}
