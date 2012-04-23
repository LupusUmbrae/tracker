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

	private ArrayList<Move> movers = new ArrayList<Move>();
	private ArrayList<Person> people;

	/**
	 * 
	 */
	public PersonProcessor(int threads, ArrayList<Person> people) {
		while (threads > 0) {
			movers.add(new Move(this));
			threads--;
		}
		this.people = people;
		peopleMove.addAll(people);
	}

	/**
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void movePeople() throws InterruptedException, IOException {
		for (Move mover : movers) {
			mover.start();
		}
	}

	/**
	 * 
	 */
	public void stopPeople() {
		for (Move mover : movers) {
			mover.end();
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void exportResults() throws IOException, InterruptedException {
		File file = new File("track" + System.currentTimeMillis() + ".txt");
		FileWriter writer = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(writer);
		boolean filesLeft = true;
		// Write to file
		while (filesLeft) {
			Person person = pollPeopleMoved();
			if (person != null) {
				out.write(String.format("%d,%s,%s\n", person.getId(), person
						.getLat().toString(), person.getLon().toString()));
			} else {
				filesLeft = false;
			}
		}
		out.close();
		writer.close();

		peopleMove.addAll(people);
	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected Person pollPeopleMoved(){
		Person person;
		synchronized (peopleMoved) {
			person = peopleMoved.poll();
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
	
	public Person[] getAllPeople(){
		Person[] peopleArray;
		synchronized (people) {
			peopleArray = new Person[people.size()];
			people.toArray(peopleArray);
		}
		return peopleArray;
	}
}
