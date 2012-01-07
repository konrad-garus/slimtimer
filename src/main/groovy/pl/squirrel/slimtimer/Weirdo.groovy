package pl.squirrel.slimtimer

class Weirdo {
	public static void main(String[] args) {
		new Weirdo().go()
	}
	
	private List tasks = new ArrayList()
	
	def go() {
		boolean done = false;
		while(!done) {
			spin()
			done=true
		}
	}
	
	def spin() {
		map([1, 2, 3])
	}
	
	def map(coll) {
		coll.each {
			println('App')
			tasks.add('Test')
		}
	}
}
