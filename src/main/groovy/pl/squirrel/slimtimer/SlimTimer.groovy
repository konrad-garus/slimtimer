package pl.squirrel.slimtimer

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.HTTPBuilder

import java.text.SimpleDateFormat

import org.joda.time.DateTime
public class SlimTimer {
	private String apiKey
	private HTTPBuilder http

	private def uid
	private def token

	public SlimTimer() {
		Properties props = readProperties()

		this.apiKey = props['api.key']

		this.http = new HTTPBuilder('http://slimtimer.com')
		this.http.handler.failure = { resp -> println "Unexpected failure: ${resp.statusLine}" }
		this.http.headers = [Accept: 'application/xml']

		logIn(props['email'], props['password']);
	}

	private Properties readProperties() {
		Properties props = new Properties()
		new File("config/slimtimer.properties").withInputStream { stream ->
			props.load(stream)
		}
		props
	}

	def logIn(String _email, String _pw) {
		http.request(POST, XML) { req ->
			uri.path = '/users/token'
			headers.Accept = 'application/xml'
			body = {
				request {
					user {
						email (_email)
						password (_pw)
					}
					'api-key' (apiKey)
				}
			}
			response.success = { resp, reader ->
				uid = reader.'user-id'.text()
				token = reader.'access-token'.text()
				println "Logged in, access token: $token"
			}
		}
	}

	def getTasks(int limit = Integer.MAX_VALUE) {
		List<Task> tasks = []
		int offset=0
		boolean done=false
		while(!done) {
			int cnt = appendTasks(tasks, offset)
			done = cnt==0 || (cnt+offset >= limit)
			offset += 50
		}
		println "Got ${tasks.size()} tasks"
		tasks
	}

	def appendTasks(_tasks, _offset) {
		def reqTasks
		http.request(GET, XML) { req ->
			uri.path = "/users/$uid/tasks"
			uri.query = [api_key: apiKey, access_token: token, offset: _offset, show_completed: 'no']
			headers.Accept = 'application/xml'

			response.success = { resp, reader ->
				reqTasks = reader.'task'
			}
		}
		_tasks.addAll(mapTasks(reqTasks))
		reqTasks.size()
	}

	def mapTasks(reqTasks) {
		reqTasks.collect { mapTask(it) }
	}

	def mapTask(xmlTask) {
		Task task = new Task()
		task.id = xmlTask.'id'.toString().toInteger()
		task.name = xmlTask.'name'.toString()
		task
	}

	def getTimeEntries(DateTime from, DateTime to) {
		def xmlEntries = fetchEntries(from, to);
		mapTimeEntries(xmlEntries)
	}

	def fetchEntries(DateTime from, DateTime to) {
		List result = []
		String fromStr = DateHelper.toSlimXml(from)
		String toStr = DateHelper.toSlimXml(to)
		http.request(GET, XML) { req ->
			uri.path = "/users/$uid/time_entries"
			uri.query = [api_key: apiKey,
						access_token: token,
						offset: 0,
						range_start: fromStr,
						range_end: toStr]
			headers.Accept = 'application/xml'

			response.success = { resp, reader ->
				reader.'time-entry'.each { result.add(it) }
			}
		}
		println "Got ${result.size()} time entries"
		result
	}


	def mapTimeEntries(xmlEntries) {
		xmlEntries.collect {
			TimeEntry entry = new TimeEntry()
			entry.id = it.'id'.toString().toInteger()
			entry.startTime = new DateTime(it.'start-time'.toString())
			entry.endTime = new DateTime(it.'end-time'.toString())
			entry.durationInSeconds = it.'duration-in-seconds'.toString().toInteger()
			entry.task = mapTask(it.'task')
			entry
		}
	}

	//	def parseUTC(String date) {
	//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	//		sdf.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")))
	//		sdf.parse(date)
	//	} TODO

	def completeTask(Task _task) {
		http.request(PUT, XML) { req ->
			uri.path = "/users/$uid/tasks/${_task.id}"
			uri.query = [api_key: apiKey,
						access_token: token]
			body = {
				task {
					name (_task.name)
					'completed-on' (DateHelper.toSlimXml(new DateTime()))
				}
			}
			headers.Accept = 'application/xml'

			response.success = { resp, reader ->
				println "Removed: ${_task.name}"
			}
		}
	}
}
