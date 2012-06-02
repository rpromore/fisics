package com.sandbox.gameplay;

import java.math.BigDecimal;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public interface Node {
	public Bounds bounds();
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
	public BigDecimal width();
	public BigDecimal height();
	public float restitution();	
	public void restitution(float r);
	public BigDecimal mass();
	public void mass(BigDecimal m);
	public void maxVelocity(int n);
	public float maxVelocity();
	public Neighborhood neighborhood();
	public void neighborhood(Neighborhood n);
	public void draw(Camera camera);
}
