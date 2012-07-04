package com.sandbox.gameplay.bounds;

import com.badlogic.gdx.math.Vector3;

public abstract class Bounds {
	public abstract Vector3 intersects(Bounds bounds);
	public abstract Vector3 intersects(BoundingBox bounds);
	public abstract Vector3 intersects(BoundingSphere bounds);
	
	public abstract Vector3 penetration();
	public abstract float depth();
	
	public abstract Vector3 getLowerBounds();
	public abstract Vector3 getUpperBounds();
	
}
