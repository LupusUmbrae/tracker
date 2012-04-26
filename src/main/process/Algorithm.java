package main.process;

import java.util.LinkedList;
import java.util.Random;

import main.types.Person;

public class Algorithm {

	public static final double MAX_MOVE_SPEED = 0.2;
	public static final double CENTRE_LAT = 30.0;
	public static final double CENTRE_LON = 30.0;
	public static final double RADIUS = 5.0;

	double modifier; // gaussian modifier for more/less extreme movement. Higher = more likely.
	Random rand = new Random();
	LinkedList<Person> nearbyUsers = new LinkedList<Person>();

	double volatility;
	double curLat;
	double curLng;
	double nearbyLat;
	double nearbyLon;
	Person curPos;
	Person newPos;
	double usersVol[];
	Person usersPos[];
	double nearAngle;
	double moveAngle;

	public Algorithm() {
	}

	/**
	 * takes as the input a list of current user positions, and the current
	 * user. Uses this current user and the positions to work out a movement
	 * distance
	 * 
	 * @param usersPos
	 *            List of every user currently being tracked
	 * @param curPos
	 *            Current user to track
	 * @return a Person, updating the curPos user with the new position.
	 */

	public Person run(Person[] usersPos, Person curPos) {
		this.usersPos = usersPos;
		this.curPos = this.newPos = curPos;
		volatility = curPos.getVol();
		curLat = curPos.getLat();
		curLng = curPos.getLon();

		// checks to see if another person is within movement range of the
		// current person

		for (Person genUser : usersPos) {
			if (Math.abs(genUser.getLat() - curLat) < MAX_MOVE_SPEED
					&& Math.abs(genUser.getLon() - curLng) < MAX_MOVE_SPEED) {
				nearbyUsers.add(genUser);
			}
		}

		// works out the average angle of the nearby users in relation to the
		// current user, and sets the 'nearAngle' to the opposite direction
		
		nearbyLat = 0;
		nearbyLon = 0;
		
		if (!nearbyUsers.isEmpty()) {
			for (Person p : nearbyUsers) {
				nearbyLat += p.getLat();
				nearbyLon += p.getLon();
			}
			nearbyLat = nearbyLat / (nearbyUsers.size());
			nearbyLon = nearbyLon / (nearbyUsers.size());
			if (Math.abs((nearbyLat - curLat) - (nearbyLon - curLng)) < 0.0000001) {
				nearAngle = Math.PI * rand.nextDouble();
			} else {
			nearAngle = Math.tan(((nearbyLat - curLat) / (nearbyLon - curLng)))
					+ Math.PI;
			}
		} else {
			nearAngle = Math.PI * rand.nextDouble(); // means less backtracking
		}
		nearbyUsers.clear();

		// works out the movement angle by using the volatility to determine the
		// randomness of movement towards/away from current users.

		if (Math.sqrt(Math.pow(curLat - CENTRE_LAT,2) + Math.pow(curLng - CENTRE_LON, 2)) != RADIUS){ // Checks to see if the current point is on the border.
		moveAngle = nearAngle + (volatility + 1) * rand.nextDouble()
				* (Math.PI / 2);
		}
		else { // if on the edge of the circle, forces the target inwards but still away from the
			double centreAngle = Math.tan((curLat - CENTRE_LAT) / (curLng - CENTRE_LON)) + Math.PI;
			moveAngle =  centreAngle - (centreAngle - nearAngle) + ((volatility + 1) * rand.nextGaussian() * (Math.PI / 2));
		}
		newPos = movement(moveAngle);

		return newPos;
	}

	/**
	 * takes in an angle argument, works out a random distance moved and checks
	 * it against the location of the circle to make sure the person has not
	 * left the circle. Future versions may include the chance to leave the
	 * circle.
	 * 
	 * @param moveAngle2
	 *            the angle of movement
	 * @return the new position
	 */

	private Person movement(double moveAngle2) {
		double moveSpeed = (rand.nextDouble() * MAX_MOVE_SPEED);
		if (moveSpeed < (MAX_MOVE_SPEED/2)) moveSpeed = (MAX_MOVE_SPEED/2);
		curLat += moveSpeed * Math.sin(moveAngle2);
		curLng += moveSpeed * Math.cos(moveAngle2);

		double distFromCentre = Math.sqrt(Math.pow(curLat - CENTRE_LAT,2) + Math.pow(curLng - CENTRE_LON, 2));
		if(distFromCentre > RADIUS){
			curLat = RADIUS*(curLat/ (curLat + curLng)) + CENTRE_LAT;
			curLng = RADIUS*(curLng/ (curLat + curLng)) + CENTRE_LON;
		}
			
		newPos.setLat(curLat);
		newPos.setLon(curLng);

		return newPos;
	}

	/**
	 * instantiates all required objects, and defines the volatility for each of
	 * the users
	 * 
	 * @param users
	 *            the number of users required
	 */

	public double genVol(double modifier) {

		if (modifier == 1) {
			return rand.nextGaussian();
		} else if (modifier == 0) {
			return 0;
		} else {
			return biasVolatility();
		}
	}

	/**
	 * Adds a bias into the volatility. Positive modifiers increase the
	 * likelyhood of errant behaviour
	 * 
	 * @return returns the modified volatility
	 */

	private double biasVolatility() {
		double vol = rand.nextGaussian();
		int sign = 1;
		if (vol < 0)
			sign = -1;
		return (sign * Math.pow(Math.abs(vol), 1 / modifier));
	}
	
	/**
	 * Generates a random latitude within the given parameters
	 * @return returns a latitude position
	 */
	
	public double genLat() {
		return (rand.nextDouble()*RADIUS*2 + CENTRE_LAT - RADIUS);
	}
	
	/**
	 * Generates a random longitude within the given parameters
	 * @return returns a longitude position
	 */
	
	public double genLon() {
		return (rand.nextDouble()*RADIUS*2 + CENTRE_LON - RADIUS);
		
	}
}	