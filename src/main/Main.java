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
		int numberPeople = 10;
		int numberThreads = 10;
		int waitTime = 10;
		Long tickCount = 1L;
		ArrayList<Person> people = new ArrayList<Person>();
		Algorithm algorithm = new Algorithm();
		boolean run = true;

		// Start matching stuff
		for (int i = 0; i < args.length; i++) {
			if (args[i].matches("-help") || args[i].matches("-h")) {
				run = false;
				System.out
						.println("TRACKER Position Genreater:\n"
								+ "-help\t-h\t\tPrints this message\n"
								+ "-tick\t-t\t\tFollowed by number, time in seconds between generating new tracking data\n"
								+ "-people\t-p\t\tFollowed by number, number of people to track");
				break;
			} else if (args[i].matches("-tick") || args[i].matches("-t")) {
				try {
					waitTime = Integer.parseInt(args[i + 1]);
					System.out.println("Tick time set to: " + waitTime);
				} catch (NumberFormatException e) {
					System.err
							.println("Cannot parse int for tick time.\n\tInput: "
									+ args[i + 1]);
					run = false;
					break;
				}
			} else if (args[i].matches("-people") || args[i].matches("-p")) {
				try {
					numberPeople = Integer.parseInt(args[i + 1]);
					System.out.println("Number of people set to: "
							+ numberPeople);
				} catch (NumberFormatException e) {
					System.err
							.println("Cannot parse int for people.\n\tInput: "
									+ args[i + 1]);
					run = false;
					break;
				}
			}
		}

		if (run) {

			// Create the people
			while (numberPeople > 0) {

				people.add(new Person(numberPeople, algorithm.genVol(1),
						algorithm.genLat(), algorithm.genLon()));
				numberPeople--;
			}
			System.out
					.println(String.format("%d people created", people.size()));

			// Begin moving
			PersonProcessor processor = new PersonProcessor(numberThreads,
					people);
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
			}
		}
	}

}
