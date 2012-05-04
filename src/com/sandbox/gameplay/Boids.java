package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Collection;

public class Boids implements Neighborhood {

	private ArrayList<Boid> boids = new ArrayList<Boid>();
	
	public void add(Boid b) {
		boids.add(b);
	}
	public void add(ArrayList<Boid> boids) {
		this.boids.addAll(boids);
	}
	
	@Override
	public Collection<Locomotion> findNearby(Locomotion boid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Boid> getNeighbors() {
		return boids;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

}
