package com.sandbox.gameplay;

import com.badlogic.gdx.math.Vector3;

public class AABB {
	private Vector3 lower, upper;
	public AABB() {
		this(new Vector3(), new Vector3());
	}
	public AABB(Vector3 l, Vector3 u) {
		lower = new Vector3(l);
		upper = new Vector3(u);
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
		return !((lower.x > aabb.upper.x) || (aabb.lower.x > upper.x) ||
			       (lower.y > aabb.upper.y) || (aabb.lower.y > upper.y) ||
			       (lower.z > aabb.upper.z) || (aabb.lower.z > upper.z)) ;
	  }
	
	public String toString() {
		return "Lower: "+lower+"\nUpper: "+upper;
	}
}
