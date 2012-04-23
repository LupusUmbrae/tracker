package com.logica.trace;

import java.util.LinkedList;
import java.util.Random;

public class Algorithm {

	public static final double MAX_MOVE_SPEED = 0.2;
	public static final double THRESHOLD = 0.7;

	int users;
	int runs;
	int pref;
	double modifier;
	Random rand = new Random();
	Position centre;
	double bias;
	LinkedList<Position> nearbyUsers = new LinkedList<Position>();
	
	Double volatility;
	Double curLat;
	Double curLng;
	Position curPos;
	Position newPos;
	Double usersVol[];
	Position usersPos[];
	
	
	public Algorithm(int users, double centreLat, double centreLng){
		init(users);
		centre.setLat(centreLat);
		centre.setLng(centreLng);
	}
	
	public Position run(Position[] usersPos, Position curPos){
		this.usersPos = usersPos;
		this.curPos = curPos;
		volatility = usersVol[curPos.getUser()];
		curLat = curPos.getLat();
		curLng = curPos.getLng();
		
		volatility = volatility + rand.nextDouble();
		pref = preference(volatility);
		
		for(Position genUser : usersPos){
			if ( Math.abs(genUser.getLat() - curLat) < MAX_MOVE_SPEED && Math.abs(genUser.getLng() - curLng) < MAX_MOVE_SPEED){
				nearbyUsers.add(genUser);
			}
		}
		
		
		nearbyUsers.clear();
		return newPos;
	}

	/**
	 * instantiates all required objects, and defines the volatility for each of the users
	 * @param users the number of users required
	 */
	
	private void init(int users) {
		this.users = users;
		usersVol = new Double[users];
		usersPos = new Position[users];
		
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
	
	/**
	 * checks the volatility and assigns it a preference based on an initial THRESHOLD value
	 * @param vol the volatility to check
	 * @return returns -1 if preference to flee, 0 if neutral, 1 if preference to approach
	 */
	
	private int preference(double vol){
		if(Math.abs(vol) > THRESHOLD){
			if(vol > 0){
				return 1;
			}
			else {
				return -1;
			}
		}
		return 0;
	}
	
}
