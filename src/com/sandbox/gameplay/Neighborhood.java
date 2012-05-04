package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Collection;

public interface Neighborhood {
	Collection<Locomotion> findNearby(Locomotion boid);
	ArrayList<Node> getNeighbors();
    double getRadius();
}
