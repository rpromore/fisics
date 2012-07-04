package com.sandbox.gameplay.bounds;

import utils.Pair;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Node;

public class Collision {

	public Pair<Node> pair;
	public Vector3 penetration;
	
	public Collision(Pair<Node> p, Vector3 m) {
		pair = p;
		penetration = m;
	}
	
}
