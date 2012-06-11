package pl.squirrel.slimtimer

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import org.joda.time.DateMidnight
import org.joda.time.DateTime

class ReportLastWeek {
	public static void main(String[] args) {
		new ReportLastWeek().go()
	}

	def go() {
		SlimTimer timer = new SlimTimer();

		DateTime midnight = new DateTime().withTimeAtStartOfDay()
		DateTime monday = midnight.dayOfWeek().withMinimumValue()
		DateTime lastMonday = monday.minusDays(7)

		System.out.println("Getting entries for $lastMonday and $monday")
		List<TimeEntry> entries = timer.getTimeEntries(lastMonday, monday)

		new Report(entries, new DateMidnight(lastMonday), new DateMidnight(monday)).printTsv()
	}
}

