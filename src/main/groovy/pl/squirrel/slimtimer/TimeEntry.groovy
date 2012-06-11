package pl.squirrel.slimtimer;

import groovy.transform.EqualsAndHashCode;

import org.joda.time.DateTime

@EqualsAndHashCode
public class TimeEntry {
	int id
	DateTime startTime
	DateTime endTime
	int durationInSeconds
	Task task
	
}
