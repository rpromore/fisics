package com.sandbox.gameplay;

import java.util.ArrayList;

public class Nodes implements Neighborhood {
	private ArrayList<Node> nodes = new ArrayList<Node>();

	@Override
	public ArrayList<Node> getNeighbors() {
		return nodes;
	}

	public void add(Node n) {
		n.neighborhood = this;
		nodes.add(n);
	}
	
	public Node get(int i) {
		return nodes.get(i);
	}
	
	public void clear() {
		nodes.clear();
	}
}
