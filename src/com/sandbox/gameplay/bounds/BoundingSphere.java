package com.sandbox.gameplay.bounds;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.node2D.Circle;

public class BoundingSphere extends Bounds {
	
	private Circle circle;
	private Vector3 penetration;
	
	public float depth;
	
	public BoundingSphere(Circle c) {
		circle = c;
		penetration = new Vector3();
	}

	@Override
	public Vector3 intersects(Bounds bounds) {
		return bounds.intersects(this);
	}

	@Override
	public Vector3 intersects(BoundingBox bounds) {
		System.out.println("aabb");
		return new Vector3();
	}

	@Override
	public Vector3 intersects(BoundingSphere bounds) {
//		float a = circle.radius().add(bounds.circle.radius()).floatValue();
//		Vector3 delta = circle.position().cpy().sub(bounds.circle.position());
//		float d = delta.len();
//		float depth = (a - d) / d;
//		
//		final double dx = circle.getX() - bounds.circle.getX();
//		final double dy = circle.getY() - bounds.circle.getY();
//
//		if( (a * a) > (dx * dx + dy * dy) ) {
//			if (d > 0f)
//				penetration = delta.cpy().mul(depth);
//			return true;
//		}
		
//		Vector3 direction = circle.position().cpy().sub(bounds.circle.position());
//		float distance = direction.len();
//		direction.nor();
//		depth = (bounds.circle.radius().floatValue() + circle.radius().floatValue()) - distance;
//		
//		penetration = direction.mul(depth);
//		
//		return depth > 0;
		
		// get the mtd
	    Vector3 delta = circle.position().cpy().sub(bounds.circle.position());
	    float r = circle.radius().floatValue() + bounds.circle.radius().floatValue();
	    float dist2 = delta.cpy().dot(delta);
	    
	    if( dist2 > r*r ) {
//	    	penetration.mul(0);
	    	return new Vector3();
	    }
	    
	    float dist = (float) Math.sqrt(dist2);
	    return delta.mul(r-dist).div(dist);
	    
//	    float d = delta.len();
//	    // minimum translation distance to push balls apart after intersecting
//	    penetration = delta.mul(((circle.radius().floatValue() + bounds.circle.radius().floatValue())-d)/d);
//	    
//	    return penetration.len() > 0;
	}

	@Override
	public Vector3 penetration() {
		return penetration;
	}
	
	@Override
	public float depth() {
		return depth;
	}

	@Override
	public Vector3 getLowerBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3 getUpperBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
