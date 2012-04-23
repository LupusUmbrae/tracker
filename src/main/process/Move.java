package main.process;

import main.types.Person;

public class Move extends Thread {

	private boolean run = true;
	PersonProcessor processor;

	public Move(PersonProcessor processor) {
		this.processor = processor;
	}

	public void run() {
		Person person;
		Person[] people;
		Algorithm movePerson = new Algorithm();
		while (run) {
			try {
				person = processor.takePeopleMove();
				// Move the person
				people = processor.getAllPeople();
				person = movePerson.run(people, person);
				processor.putPeopleMoved(person);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out
						.println("Will carry on.. but might want to look into this");
			}
		}
	}

	public void end() {
		run = false;
	}
}
