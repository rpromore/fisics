package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import utils.Pair;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.bounds.Collision;

public class SweepAndPrune implements BroadphaseCollisionDetection {

//	HashSet<Pair<Node>> colliding = new HashSet<Pair<Node>>();
	
	HashSet<Collision> colliding = new HashSet<Collision>();
	
	@Override
	public Set<Collision> colliding() {
		@SuppressWarnings("unchecked")
		HashSet<Collision> c = (HashSet<Collision>) colliding.clone();
		colliding.clear();
		return c;
	}

	@Override
	public void run(ArrayList<Node> nodes) {
		for( int i = 0; i < nodes.size(); i++ ) {
			for( int j = i; j < nodes.size(); j++ ) {
				
				
//				colliding.add(new Pair<Node>(nodes.get(i), nodes.get(j)));
				
				if( !nodes.get(i).equals(nodes.get(j)) ) {
					Vector3 intersect = nodes.get(i).bounds().intersects(nodes.get(j).bounds());
					if( intersect.len() > 0 ) {
						Collision c = new Collision(new Pair<Node>(nodes.get(i), nodes.get(j)), intersect);
						if( !colliding.contains(c) ) 
							colliding.add(c);
					}
				}
			}
		}
	}
	
}