package pl.squirrel.slimtimer;

import java.util.List
import java.util.Set

import org.joda.time.DateMidnight
import org.joda.time.format.DateTimeFormat;

public class Report {
	DateMidnight from
	DateMidnight to
	List<ReportLine> lines
	
	def Report(List<TimeEntry> entries, DateMidnight from, DateMidnight to) {
		this.from = from
		this.to = to
		List<Task> tasks = getUniqueTasks(entries).asList().sort(false)
		lines = tasks.collect {
			new ReportLine(it, from, to)
		}
		entries.each {
			findLine(it.task).addEntry(it)
		}
	}
	
	Set<Task> getUniqueTasks(List<TimeEntry> entries) {
		Set<Task> result = []
		entries.each {
			result.add(it.task)
		}
		result
	}
	
	ReportLine findLine(Task task) {
		lines.find {
			it.task.equals(task)
		}
	}
	
	def printTsv() {
		String dt = DateTimeFormat.forPattern("M/d/yyyy").print(to.minusDays(1))
		String dev = "Konrad Garus"
		lines.each {
			print dt
			print '\t'
			print dev
			print '\t'
			print it.getTracNumber()
			
			print '\n'
		}
	}
}
