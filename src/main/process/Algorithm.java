package main.process;

import java.util.LinkedList;
import java.util.Random;

import main.types.Person;

public class Algorithm {

	public static final double MAX_MOVE_SPEED = 0.2;
	public static final double CENTRE_LAT = 30.0;
	public static final double CENTRE_LON = 30.0;
	public static final double RADIUS = 5.0;
	
	int users;
	int runs;
	int pref;
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
	
	
	public Algorithm(int users){
		init(users);
	}
	
	/**
	 * takes as the input a list of current user positions, and the current user. Uses this current user and the positions to work out a movement distance
	 * @param usersPos List of every user currently being tracked
	 * @param curPos Current user to track
	 * @return a Person, updating the curPos user with the new position.
	 */
	
	public Person run(Person[] usersPos, Person curPos){
		this.usersPos = usersPos;
		this.curPos = this.newPos = curPos;
		volatility = usersVol[curPos.getId()];
		curLat = curPos.getLat();
		curLng = curPos.getLon();
		
		// checks to see if another person is within movement range of the current person
		
		for(Person genUser : usersPos){
			if ( Math.abs(genUser.getLat() - curLat) < MAX_MOVE_SPEED && Math.abs(genUser.getLon() - curLng) < MAX_MOVE_SPEED){
				nearbyUsers.add(genUser);
			}
		}
		
		// works out the average angle of the nearby users in relation to the current user, and sets the 'nearAngle' to the opposite direction
		
		if (!nearbyUsers.isEmpty()){
			for (Person p : nearbyUsers){
				nearbyLat += p.getLat();
				nearbyLon += p.getLon();
			}
			nearbyLat = nearbyLat/(nearbyUsers.size());
			nearbyLon = nearbyLon/(nearbyUsers.size());
			nearAngle = Math.tan(((nearbyLat - curLat) / (nearbyLon - curLng))) + Math.PI;
		}
		else {
			nearAngle = 2*Math.PI*rand.nextDouble();
		}
		nearbyUsers.clear();
		
		// works out the movement angle by using the volatility to determine the randomness of movement towards/away from current users.
		
		moveAngle = nearAngle + (volatility+1)*rand.nextDouble()*(Math.PI/2);
		
		newPos = movement(moveAngle);
		
		nearbyLat = 0;
		nearbyLon = 0;
		
		return newPos;
	}

	/**
	 * takes in an angle arguement, works out a random distance moved and checks it against the location of the circle to make sure the person has not
	 * left the circle. Future versions may include the chance to leave the circle. 
	 * @param moveAngle2 the angle of movement
	 * @return the new position
	 */
	
	private Person movement(double moveAngle2) {
		double moveSpeed = rand.nextDouble()*MAX_MOVE_SPEED;
		curLat = moveSpeed*Math.sin(moveAngle2);
		curLng = moveSpeed*Math.cos(moveAngle2);
		
		//TODO - FIX ME! RETARD! check circle movement
		
			
		newPos.setLat(curLat);
		newPos.setLon(curLng);
		
		return newPos;
	}

	/**
	 * instantiates all required objects, and defines the volatility for each of the users
	 * @param users the number of users required
	 */
	
	private void init(int users) {
		this.users = users;
		usersVol = new double[users];
		usersPos = new Person[users];
		
		if (modifier == 1){
		for(int x = 0; x < users; x++){
			usersVol[x] = rand.nextGaussian();
		}
		}
		else {
			for(int x = 0; x < users; x++){
			usersVol[x] = biasVolatility();	
			}
		}
	}

	/**
	 * Adds a bias into the volatility. Positive modifiers increase the likelyhood of errant behaviour
	 * @return returns the modified volatility
	 */
	
	private double biasVolatility(){
		volatility = rand.nextGaussian();
		int sign = 1;
		if(volatility < 0) sign = -1;
		return (sign*Math.pow(Math.abs(volatility),1 / modifier));
	}
}
