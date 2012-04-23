package main;

import java.io.IOException;
import java.util.ArrayList;

import main.process.Algorithm;
import main.process.PersonProcessor;
import main.types.Person;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Get the passed in ARRRgs..
		int numberPeople = 10;
		int numberThreads = 10;
		int waitTime = 10;
		Long tickCount = 1L;
		ArrayList<Person> people = new ArrayList<Person>();
		Algorithm volCalc = new Algorithm();

		if (args.length == 1) {

		}

		// Create the people
		while (numberPeople > 0) {

			people.add(new Person(numberPeople, volCalc.genVol(1)));
			numberPeople--;
		}
		System.out.println(String.format("%d people created", people.size()));

		// Begin moving
		PersonProcessor processor = new PersonProcessor(numberThreads, people);
		try {
			processor.movePeople();
			System.out.println(String.format("%d threads created",
					numberThreads));

			// Start the export process...
			while (true) {
				Thread.sleep(waitTime * 1000);
				processor.exportResults();
				System.out.println(String.format("Tick %s completed",
						tickCount.toString()));

				tickCount++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

}
