package com.sandbox.gameplay.bounds;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Bounds;
import com.sandbox.gameplay.Node;

public class AABB extends Bounds {
	private Vector3 lower, upper, penetration;
	private Node node;
	
	public AABB(Node n) {
		node = n;
		lower = new Vector3();
		upper = new Vector3();
		penetration = new Vector3();
		update();
	}
	
	public void update() {
		float lx = node.position().x;
		float ly = node.position().y;
//		float lz = position.z;
		float lz = 0;
		float hx = node.position().x + node.width().floatValue();
		float hy = node.position().y + node.height().floatValue();
//		float hz = position.z + depth.floatValue();
		float hz = 0;
		
		lower(new Vector3(lx, ly, lz));
		upper(new Vector3(hx, hy, hz));
	}

	public Vector3 center() {
		return upper.cpy().sub(upper.cpy().sub(lower).div(2));
	}

	public Vector3 lower() {
		return lower;
	}

	public void lower(Vector3 l) {
		lower.set(l);
	}

	public Vector3 upper() {
		return upper;
	}

	public void upper(Vector3 u) {
		upper.set(u);
	}

	@Override
	public final boolean intersects(final Bounds other) {
		return other.intersects(this);
	}
	
	@Override
	public final boolean intersects(final AABB aabb) {
		if (!((lower.x > aabb.upper.x) || (aabb.lower.x > upper.x)
				|| (lower.y > aabb.upper.y) || (aabb.lower.y > upper.y)
				|| (lower.z > aabb.upper.z) || (aabb.lower.z > upper.z))) {
			
			Vector3 amin = lower;
			Vector3 amax = upper;
			Vector3 bmin = aabb.lower;
			Vector3 bmax = aabb.upper;

			Vector3 mtd = new Vector3();

			float left = (bmin.x - amax.x);
			float right = (bmax.x - amin.x);
			float top = (bmin.y - amax.y);
			float bottom = (bmax.y - amin.y);

			// box intersect. work out the mtd on both x and y axes.
			if (Math.abs(left) < right)
				mtd.x = left;
			else
				mtd.x = right;

			if (Math.abs(top) < bottom)
				mtd.y = top;
			else
				mtd.y = bottom;

			// 0 the axis with the largest mtd value.
			if (Math.abs(mtd.x) < Math.abs(mtd.y))
				mtd.y = 0;
			else
				mtd.x = 0;
			
			penetration.set(mtd);
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean intersects(final Circle other) {
		return false;
	}

	public Vector3 penetration() {
		return penetration;
	}
}
