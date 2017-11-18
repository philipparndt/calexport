package de.rnd7.calexport;

import java.util.Arrays;
import java.util.List;

public class TestDataDusslingen extends TestData {

	@Override
	public List<Event> create() {
		return Arrays.asList(


				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-01","10:15"))
				,

				new Event ("Lorem ipsum dolor (FM)")
				.setStart(of("2017-10-04","20:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-06","19:30"))
				,

				new Event ("**Lorem ipsum dolor** Lorem ipsum dolorLorem ipsum dolorLorem ipsum dolor")
				.setStart (of("2017-10-08","10:15"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-13","19:30"))
				,

				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-15","10:15"))
				,

				new Event ("Lorem ipsum dolor (Lorem ipsum dolor")
				.setStart(of("2017-10-17","19:30"))
				,

				new Event ("Lorem ipsum dolor (FM)")
				.setStart(of("2017-10-18","20:00"))
				,


				new Event ("Lorem ipsum dolor")
				.setStart (of("2017-10-18","20:00"))
				,

				new Event ("Lorem ipsum dolor")
				.setStart(of("2017-10-19","19:30"))
				,

				new Event ("Lorem ipsum dolor (Lor??)")
				.setStart(of("2017-10-21","09:00"))
				.setEnd(of("2017-10-21","15:00"))
				,

				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-22","10:15"))
				,

				new Event ("Lorem: \"Lorem ipsum dolor\"(Lorer)")
				.setStart(of("2017-10-22"))
				,

				new Event ("Lorem ipsum dolorLorem ipsum dolorLorem ipsum dolor")
				.setStart (of("2017-10-27","19:30"))
				,

				new Event ("**Lorem ipsum dolor**")
				.setStart(of("2017-10-29","10:15"))
				,

				new Event ("Lorem ipsum dolorLorem ipsum dolor (WD)")
				.setStart(of("2017-10-29","11:00"))

				);
	}

}
