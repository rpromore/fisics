package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Collection;

public interface Neighborhood {
	Collection<Node> findNearby(Node boid);
	ArrayList<Node> getNeighbors();
    double getRadius();
}
