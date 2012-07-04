package com.sandbox.gameplay;

import java.util.ArrayList;
import java.util.Set;

import utils.Pair;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.bounds.Collision;

public class Nodes implements Neighborhood {
	private ArrayList<Node> nodes;
	private BroadphaseCollisionDetection broadphase;
	private NarrowphaseCollisionDetection narrowphase;
	
	public Nodes(BroadphaseCollisionDetection bp, NarrowphaseCollisionDetection np) {
		this(bp, np, new ArrayList<Node>());
	}
	public Nodes(BroadphaseCollisionDetection bp, NarrowphaseCollisionDetection np, ArrayList<Node> n) {
		nodes = n;
		broadphase = bp;
		narrowphase = np;
	}

	@Override
	public ArrayList<Node> getNeighbors() {
		return nodes;
	}

	public void add(Node n) {
		n.neighborhood(this);
		nodes.add(n);
	}
	
	public Node get(int i) {
		return nodes.get(i);
	}
	
	public void clear() {
		nodes.clear();
	}
	
	public void colliding() {
		// First do broadphase, then narrowphase.
	}
	
	public void resolveCollisions() {
		
	}
	
	// Update all nodes, draw them.
	public void update(Camera camera) {
		for( int i = 0; i < 5; i++ ) {
			broadphase.run(nodes);
			resolveCollisions(broadphase.colliding());
		}
		
		for( Node n : nodes ) {
			n.update();
			n.draw(camera);
		}
	}
	
//	public boolean colliding() {
//		if( nodes.size() != 0 ) {
//			ArrayList<Node> collidingWith = new ArrayList<Node>();
//			ArrayList<Node> willCollideWith = new ArrayList<Node>();
//			
//			for (Node n : nodes) {
//				if (n.collidesWith(n)) {
//					collidingWith.add(n);
//				}
//			}
//			if( collidingWith.size() > 0 ) 	
//				resolveCollisions(collidingWith);
//	
//			return collidingWith.size() > 0 && willCollideWith.size() > 0;
//		}
//		return false;
//	}
//
	
	public void resolveCollisions(Set<Collision> nodes) {
//		System.out.println(nodes.size());
		for (Collision p : nodes) {
			
			Node m = p.pair.getFirst();
			Node n = p.pair.getSecond();
			
			
//			System.out.println(m.bounds().penetration());
//			System.out.println(n.bounds().penetration());
//			System.out.println("----------------------");
			
			Vector3 mtd = p.penetration;
			
			float im1 = 1 / m.mass().floatValue();
			float im2 = 1 / n.mass().floatValue();

			m.position().sub(mtd.cpy().mul(im1 / (im1 + im2)));
			n.position().add(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = m.velocity().cpy().sub(n.velocity());
			float vn = v.cpy().dot(mtd.nor());
			
			if (vn < 0.0f) continue;
			
			// collision impulse		
			float i = (-(1.0f + 0) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			m.velocity().add(impulse.cpy().mul(im1));
			n.velocity().sub(impulse.cpy().mul(im2));
			
			m.friction(m.velocity().cpy().mul(.085f));
			n.friction(n.velocity().cpy().mul(.085f));
			
		}
	}
	
}
