package com.sandbox.gameplay.bounds;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Bounds;

public class Circle extends Bounds {
	
	private com.sandbox.gameplay.node2D.Circle circle;
	private Vector3 penetration;
	
	public Circle(com.sandbox.gameplay.node2D.Circle c) {
		circle = c;
		penetration = new Vector3();
	}

	@Override
	public boolean intersects(Bounds bounds) {
		return bounds.intersects(this);
	}

	@Override
	public boolean intersects(AABB bounds) {
		System.out.println("aabb");
		return false;
	}

	@Override
	public boolean intersects(Circle bounds) {
		float a = circle.radius().add(bounds.circle.radius()).floatValue();
		Vector3 delta = circle.position().cpy().sub(bounds.circle.position());
		float d = delta.len();
		float depth = (a - d) / d;
		
		final double dx = circle.getX() - bounds.circle.getX();
		final double dy = circle.getY() - bounds.circle.getY();

		if( (a * a) > (dx * dx + dy * dy) ) {
			if (d != 0f)
				penetration = delta.cpy().mul(depth);
//			else {
//				d = (float) (a - 1.0f);
//				delta = new Vector3(a, 0, 0);
//				penetration = delta.cpy().mul(depth);
//			}
			return true;
		}
		return false;
	}

	@Override
	public Vector3 penetration() {
		return penetration;
	}
	
	

}
