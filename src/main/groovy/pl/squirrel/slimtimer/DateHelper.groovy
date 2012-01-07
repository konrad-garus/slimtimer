package pl.squirrel.slimtimer

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class DateHelper {
	static public String toSlimXml(DateTime dt) {
		DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(dt)
	}
}
