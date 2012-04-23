package main.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import main.types.Person;

public class PersonProcessor {

	private static BlockingQueue<Person> peopleMove = new LinkedBlockingQueue<Person>();
	private static BlockingQueue<Person> peopleMoved = new LinkedBlockingQueue<Person>();

	Move mover = new Move();
	ArrayList<Move> movers = new ArrayList<Move>();

	/**
	 * 
	 */
	public PersonProcessor() {

	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	public void movePeople() throws InterruptedException {
		mover.run();

	}

	/**
	 * 
	 */
	public void stopPeople() {

	}

	/**
	 * 
	 * @throws IOException
	 */
	private void exportResults() throws IOException {
		File file = new File("track" + System.currentTimeMillis() + ".txt");
		FileWriter writer = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(writer);
		// Write to file
		while (!peopleMoved.isEmpty()) {
			Person person = peopleMove.poll();
			if (person != null) {
				out.write(String.format("%d,%s,%s", person.getId(), person
						.getLat().toString(), person.getLon().toString()));
			}
		}
		out.close();
		writer.close();
	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected Person takePeopleMoved() throws InterruptedException {
		Person person;
		synchronized (peopleMoved) {
			person = peopleMoved.take();
		}
		return person;
	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected Person takePeopleMove() throws InterruptedException {
		Person person;
		synchronized (peopleMove) {
			person = peopleMove.take();
		}
		return person;
	}

	/**
	 * 
	 * @param person
	 * @throws InterruptedException
	 */
	protected void putPeopleMoved(Person person) throws InterruptedException {
		synchronized (peopleMoved) {
			peopleMoved.put(person);
		}
	}

	/**
	 * 
	 * @param person
	 * @throws InterruptedException
	 */
	protected void putPeopleMove(Person person) throws InterruptedException {
		synchronized (peopleMove) {
			peopleMove.put(person);
		}
	}
}
