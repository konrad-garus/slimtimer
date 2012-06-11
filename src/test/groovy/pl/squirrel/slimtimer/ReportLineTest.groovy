package pl.squirrel.slimtimer;

import java.util.concurrent.TimeUnit

import org.joda.time.DateMidnight
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

public class ReportLineTest {
	static DateMidnight monday
	static DateMidnight tuesday
	static DateMidnight wednesday
	
	ReportLine line
	
	@BeforeClass
	static public void setupDates() {
		monday = new DateMidnight().dayOfWeek().withMinimumValue()
		tuesday = monday.plusDays(1)
		wednesday = tuesday.plusDays(1)
	}
	
	@Test
	public void roundUpOneMinute() {
		testRoundEntry(10, 1)
	}

	@Test
	public void roundUpMoreMinutes() {
		testRoundEntry(121, 3)
	}

	@Test
	public void exactDuration() {
		testRoundEntry(120, 2)
	}
	
	@Test
	public void testRenderDay() {
		line = new ReportLine(monday, tuesday)
		Assert.assertEquals([""], line.renderDays())
	}
	
	@Test
	public void testRenderOneEntryExactDuration() {
		line = new ReportLine(monday, wednesday)
		addEntry(monday, TimeUnit.MINUTES.toSeconds(15))
		Assert.assertEquals(["0.25", ""], line.renderDays())
	}
	
	@Test
	public void testRounding() {
		line = new ReportLine(monday, wednesday)
		addEntry(monday, TimeUnit.MINUTES.toSeconds(3))
		addEntry(monday, TimeUnit.MINUTES.toSeconds(4))
		addEntry(tuesday, TimeUnit.MINUTES.toSeconds(8))
		Assert.assertEquals(["0.00", "0.25"], line.renderDays())
	}
	
	@Test
	public void testLongerDuration() {
		line = new ReportLine(monday, wednesday)
		addEntry(monday, TimeUnit.MINUTES.toSeconds(17))
		addEntry(monday, TimeUnit.MINUTES.toSeconds(4))
		addEntry(tuesday, TimeUnit.MINUTES.toSeconds(233))
		Assert.assertEquals(["0.25", "4.00"], line.renderDays())
	}

	def testRoundEntry(int sec, int expected) {
		TimeEntry entry = new TimeEntry()
		entry.durationInSeconds = sec
		Assert.assertEquals(expected, ReportLine.getDurationInMinutes(entry))
	}
	
	def addEntry(DateMidnight dt, long durationInSeconds) {
		TimeEntry entry = new TimeEntry()
		entry.durationInSeconds = (int) durationInSeconds
		entry.startTime = new DateTime().withDayOfYear(dt.dayOfYear)
		line.addEntry(entry)
	}
}
