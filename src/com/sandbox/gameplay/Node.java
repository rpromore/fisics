package com.sandbox.gameplay;

import java.math.BigDecimal;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public interface Node {
	public AABB bounds = null;
	public Neighborhood neighborhood();
	public void neighborhood(Neighborhood n);
	public Vector3 position();
	public void position(Vector3 v);
	public Vector3 velocity();
	public void velocity(Vector3 v);
	public void target(Vector3 t);
	public void target(int x, int y);
	public void target(float x, float y);
	public Vector3 target();
	public Vector3 acceleration();
	public void acceleration(Vector3 v);
	public float getX();
	public float getY();
	public float getZ();
	
	public BigDecimal radius();
	public void radius(BigDecimal r);
	
	public float restitution();
	public void restitution(float r);
	public BigDecimal mass();
	public void mass(BigDecimal m);
	public void maxVelocity(int n);
	public float maxVelocity();
	public void move();
	public void draw(Camera camera);
	
	public String bounds();
}
