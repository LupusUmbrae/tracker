package main.process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import main.types.Person;

public class PersonProcessor {

	BlockingQueue<Person> peopleMove = new LinkedBlockingQueue<Person>();
	BlockingQueue<Person> peopleMoved = new LinkedBlockingQueue<Person>();

	Move mover = new Move();
	
	public PersonProcessor(){
		
	}

	public void movePeople() {
		mover.run();
	}

	private void exportResults() {

	}
}
