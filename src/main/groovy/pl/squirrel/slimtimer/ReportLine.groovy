package pl.squirrel.slimtimer

import org.joda.time.DateMidnight

class ReportLine {
	Task task
	Map<DateMidnight, Set<TimeEntry>> entries
	
	def ReportLine(Task task, DateMidnight from, DateMidnight to) {
		this.task = task
		entries = [:]
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
}
