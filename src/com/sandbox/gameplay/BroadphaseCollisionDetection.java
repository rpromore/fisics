package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Set;

import com.sandbox.gameplay.bounds.Collision;

import utils.Pair;

public interface BroadphaseCollisionDetection {
	public Set<Collision> colliding();
	public void run(ArrayList<Node> nodes);
}