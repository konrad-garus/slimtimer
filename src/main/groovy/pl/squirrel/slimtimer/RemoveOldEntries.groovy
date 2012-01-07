package pl.squirrel.slimtimer

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import org.joda.time.DateTime

class RemoveOldEntries {
	public static void main(String[] args) {
		new RemoveOldEntries().go()
	}

	def go() {
		SlimTimer timer = new SlimTimer();
		
		List<Task> allTasks = timer.getTasks();
		println "All tasks: ${allTasks.size()}"

		DateTime from = new DateTime().minusDays(2);
		DateTime to = new DateTime()

		List<TimeEntry> recentEntries = timer.getTimeEntries(from, to)
		List<Integer> uniqueTasksOnEntries = getUniqueTaskIds(recentEntries)
		println "Time entries: ${recentEntries.size()} tasks: ${uniqueTasksOnEntries.size()}"


		List<Integer> removingTaskIds = allTasks.collect {it.id }
		removingTaskIds.removeAll(uniqueTasksOnEntries)
		println "Tasks w/o completed: ${removingTaskIds.size()}"

		List<Task> matchingTasks = allTasks.findAll { removingTaskIds.contains(it.id) }
		println "Final list of tasks to purge: ${matchingTasks.size()}"

		//		matchingTasks.each { timer.completeTask(it) } TODO
	}

	List<Integer> getUniqueTaskIds(List<TimeEntry> entries) {
		entries.collect {it.task.id}.unique()
	}
}

