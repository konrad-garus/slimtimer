package pl.squirrel.slimtimer

import org.joda.time.DateMidnight


class ReportLine {
	Task task
	Map<DateMidnight, Set<TimeEntry>> entries
a
	def ReportLine(DateMidnight from, DateMidnight to) {
		initEntries(from, to)
	}

	def ReportLine(Task task, DateMidnight from, DateMidnight to) {
		this.task = task
		initEntries(from, to)
	}

	def initEntries(DateMidnight from, DateMidnight to) {
		entries = new LinkedHashMap()
		for(DateMidnight d = from; d < to; d = d.plusDays(1)) {
			entries[d] = []
		}
	}

	def addEntry(TimeEntry entry) {
		entries[new DateMidnight(entry.startTime)].add(entry)
	}

	def getTracNumber() {
		task.getTracNumber()
	}

	Collection<String> renderDays() {
		entries.collect {k, v ->
			if(v.empty) {
				""
			} else {
				return renderDecimalHours(accumulateDay(v))
			}
		}
	}
	
	static String renderDecimalHours(int minutes) {
		return String.format("%.2f", Math.round(minutes * 4 / 60) / 4)
	}
	
	static int accumulateDay(Collection<TimeEntry> entries) {
		int sumSeconds = entries.sum {
			it.durationInSeconds
		}
		Math.ceil(sumSeconds / 60).toInteger()
	}
}
