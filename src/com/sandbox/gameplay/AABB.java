package com.sandbox.gameplay;

import com.badlogic.gdx.math.Vector3;

public class AABB {
	private Vector3 lower, upper, penetration;

	public AABB() {
		this(new Vector3(), new Vector3());
	}

	public AABB(Vector3 l, Vector3 u) {
		lower = new Vector3(l);
		upper = new Vector3(u);
		penetration = new Vector3();
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

	public final boolean intersects(final AABB aabb) {
		if (!((lower.x > aabb.upper.x) || (aabb.lower.x > upper.x)
				|| (lower.y > aabb.upper.y) || (aabb.lower.y > upper.y)
				|| (lower.z > aabb.upper.z) || (aabb.lower.z > upper.z))) {
			penetration(aabb);
			return true;
		}
		return false;
	}

	public void penetration(AABB aabb) {
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
	}

	public Vector3 penetration() {
		return penetration;
	}

	public String toString() {
		return "Lower: " + lower + "\nUpper: " + upper;
	}
}
