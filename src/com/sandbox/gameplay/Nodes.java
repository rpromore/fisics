package com.sandbox.gameplay;

import java.util.ArrayList;

public class Nodes implements Neighborhood {
	private ArrayList<Node> nodes = new ArrayList<Node>();

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
