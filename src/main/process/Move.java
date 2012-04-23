package main.process;

import main.types.Person;

public class Move extends Thread {

	private boolean run = true;
	
	public void run() {
		// Doesnt need to stop
		PersonProcessor processor = new PersonProcessor();
		Person person;
		while(run){
			try {
				person = processor.takePeopleMove();
				// Move the person
				
				processor.putPeopleMoved(person);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Will carry on.. but might want to look into this");
			}
		}
	}
	
	public void end(){
		run = false;
	}
}

