package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Collection;

public class Nodes implements Neighborhood {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	@Override
	public Collection<Node> findNearby(Node boid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Node> getNeighbors() {
		return nodes;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void add(Node n) {
		nodes.add(n);
	}
	
	public Node get(int i) {
		return nodes.get(i);
	}
}
