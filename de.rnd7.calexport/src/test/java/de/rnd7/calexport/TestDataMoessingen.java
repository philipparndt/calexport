package de.rnd7.calexport;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestDataMoessingen extends TestData {

	@Override
	public List<Event> create() {
		return Arrays.asList(
				new Event("proident, sunt in culpa qui officia deserunt mollit anim #Abendmahl")
				.setStart(of("2017-10-01", "10:00"))
				.setDuration(Duration.ofHours(2)),
				new Event("Lorem ipsum dolor s")
				.setStart(of("2017-10-01")),

				new Event ("t amet, consectetur adip" )
				.setStart(of("2017-10-05", "19:30"))
				,

				new Event ("sicing elit, sed do eiusmo")
				.setStart (of("2017-10-07" , "09:30"))
				.setEnd(of("2017-10-07", "16:00"))
				,

				new Event ("tempor incididunt ut labore et dol")
				.setStart(of("2017-10-10","19:00" ))
				,

				new Event ("Öre magna aliqua. \"Ut enim ad minim\" liquip ex ea comm occaecat")
				.setStart(of("2017-10-11","19:30"))
				,


				new Event("quis nostru")
				.setStart(LocalDateTime.of(2017, 10, 12, 9, 30))
				.setDuration(Duration.ofHours(2)),
				new Event("exercitation ullamco laboris nisi")
				.setStart(LocalDateTime.of(2017, 10, 12, 17, 00))
				.setDuration(Duration.ofHours(2)),
				new Event("t aliquip ex ea com")
				.setStart(LocalDateTime.of(2017, 10, 12, 19, 30))
				.setDuration(Duration.ofHours(2)),

				new Event ("consequat. Duis aute irure")
				.setStart(of("2014-10-14"))
				,


				new Event("**olor in rep**")
				.setStart(LocalDateTime.of(2017, 10, 8, 10, 00))
				.setDuration(Duration.ofHours(1)),
				new Event("hen A&B")
				.setStart(LocalDate.of(2017, 10, 8)),

				new Event("**cillum dolore eu**")
				.setStart(of("2017-10-15","10:00"))
				,

				new Event("Excepteur sint occaecat")
				.setStart(of("2017-10-17"))
				,

				new Event("Lorem ipsum (dolor sit amet, consectetur)")
				.setStart(of ("2017-10-17", "19:30"))
				,

				new Event ("dipisicing elit (WD)")
				.setStart (of("2017-10-09","17:00"))
				,

				new Event ("**tempor incididunt**")
				.setStart(of("2017-10-22","10:00"))
				,

				new Event ("tempor incididunt")
				.setStart(of("2017-10-26","09:30"))
				,

				new Event ("quis nostrud ")
				.setStart(of("2017-10-26"))
				,

				new Event ("tempor incididunt ut labore et dolore magna aliqua. (Ut enim ad)")
				.setStart(of("2017-10-28","14:30"))
				,

				new Event ("**quis nostrud (exercitation ulla)** mco laboris nisi ut")
				.setStart(of("2017-10-29","10:00"))
				,

				new Event ("**Öuis aute irure** in reprehenderit in voluptate velit")
				.setStart(of("2017-10-31","10:30"))
				);

	}

}
