package com.sandbox.gameplay;

import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.bounds.AABB;
import com.sandbox.gameplay.bounds.Circle;

public abstract class Bounds {
	public abstract boolean intersects(Bounds bounds);
	public abstract boolean intersects(AABB bounds);
	public abstract boolean intersects(Circle bounds);
	
	public abstract Vector3 penetration();
}
