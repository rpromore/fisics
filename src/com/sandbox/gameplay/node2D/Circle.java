package com.sandbox.gameplay.node2D;

import java.math.BigDecimal;
import java.util.ArrayList;

import utils.BigSquareRoot;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Neighborhood;
import com.sandbox.gameplay.Node;

public class Circle extends Node {
	
	BigDecimal radius;
	
	private static BigSquareRoot bgs = new BigSquareRoot();

	public Circle(float x, float y) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), 1, new BigDecimal("1"),
				null, 100, .1f);
		this.radius = new BigDecimal("50");
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"), null, 100, .1f);
		this.radius = bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2")));
	}
	public Circle(int x, int y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"), null, 100, .1f);
		this.radius = bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2")));
	}
	public Circle(float x, float y, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
		this.radius = new BigDecimal("50");
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
		this.radius = bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2")));
	}
	
	public BigDecimal radius() {
		return radius;
	}

	
	public void radius(BigDecimal r) {
		radius = r;
	}
	
	@Override
	public void draw(Camera camera) {
		update();
		camera.update();
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Circle);
		sr.setColor(1, 0, 0, 1);
		sr.circle(getX(), getY(), radius.floatValue());
		sr.end();
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 1, 1);
		sr.line(getX(), getY(), getX()+velocity.cpy().nor().mul(radius.floatValue()).x, getY()+velocity.cpy().nor().mul(radius.floatValue()).y);
		sr.end();
	}
	
	@Override
	public boolean intersects(Node n) {
		if( n.getClass().equals(Circle.class) ) 
			return intersects((Circle)n);
		return false;
	}
	
	public boolean intersects(Circle n) {
		final double a = radius.add(n.radius()).doubleValue();
		final double dx = getX() - n.getX();
		final double dy = getY() - n.getY();
		
		return a * a > (dx * dx + dy * dy);
	}
	
	@Override
	public void resolveCollisions(ArrayList<Node> nodes) {
		for (Node m : nodes) {
			if (m.equals(this))
				continue;

			Circle n = (Circle) m;
			
			// http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
			Vector3 delta = position().cpy().sub(n.position());
			float d = delta.len();
			
			Vector3 mtd;
			if (d != 0f)
				mtd = delta.cpy().mul((float) ((radius.add(n.radius()).doubleValue() - d) / d));
			else {
				d = (float) (radius.add(n.radius()).doubleValue() - 1.0f);
				delta = new Vector3((float) (radius.add(n.radius()).doubleValue()), 0, 0);
				mtd = delta.mul((float) (((radius.add(n.radius()).doubleValue()) - d) / d));
			}

			float im1 = 1 / mass.floatValue();
			float im2 = 1 / n.mass().floatValue();

			position.add(mtd.cpy().mul(im1 / (im1 + im2)));
//			if( collidesWith(neighborhood.getNeighbors().get(0) ) ) {
//				n.position.sub(mtd.cpy().mul(im2 / (im1 + im2)));
//			}
//			n.position().sub(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = velocity.cpy().sub(n.velocity());
			float vn = v.cpy().dot(mtd.nor());

			// collision impulse			
			float i = (-(1.0f + restitution) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			velocity.add(impulse.mul(im1));
//			n.velocity.sub(impulse.mul(im2));
			
			// Friction
			friction.add(velocity.cpy().mul(.085f));
		}
	}
	
}
