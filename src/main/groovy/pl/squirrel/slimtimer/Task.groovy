package pl.squirrel.slimtimer;

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
public class Task implements Comparable<Task> {
	int id
	String name
	
	int compareTo(Task arg) {
		name.compareTo(arg.name)
	}
	
	String getTracNumber() {
		if(name =~ /^\s*#\d+/) {
			(name =~ /^\s*#(\d+)/)[0][1]
		} else {
			""
		}
	}
	
	String getNameWithoutTracNumber() {
		if(name =~ /^\s*#\d+/) {
			(name =~ /^\s*#\d+:?\s*(.*)/)[0][1]
		} else {
			name
		}
	}
}
