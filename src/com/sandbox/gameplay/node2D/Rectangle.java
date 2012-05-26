package com.sandbox.gameplay.node2D;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Neighborhood;
import com.sandbox.gameplay.Node;

public class Rectangle extends Node {
	public Rectangle(float x, float y) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), 1, new BigDecimal("1"),
				null, 100, .1f);
	}
	public Rectangle(float x, float y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Rectangle(int x, int y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Rectangle(float x, float y, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
	}
	public Rectangle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
	}
	
	@Override
	public void draw(Camera camera) {
		update();
		camera.update();
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Rectangle);
		sr.setColor(1, 0, 0, 1);
		sr.rect(getX(), getY(), width.floatValue(), height.floatValue());
		sr.end();
		
		sr.begin(ShapeType.Point);
		sr.setColor(1, 1, 1, 1);
		sr.point(bounds.lower().x, bounds.lower().y, bounds.lower().z);
		sr.setColor(0, 1, 1, 1);
		sr.point(bounds.upper().x, bounds.upper().y, bounds.upper().z);
		sr.end();
		
		sr.begin(ShapeType.FilledCircle);
		sr.setColor(1, 0, 1, 1);
		sr.filledCircle(position.x, position.y, 5);
		sr.end();
	}
	
	@Override
	public void updateBounds() {
		float lx = position.x;
		float ly = position.y;
//		float lz = position.z;
		float lz = 0;
		float hx = position.x + width.floatValue();
		float hy = position.y + height.floatValue();
//		float hz = position.z + depth.floatValue();
		float hz = 0;
		
		bounds.lower(new Vector3(lx, ly, lz));
		bounds.upper(new Vector3(hx, hy, hz));
	}
	
	@Override
	public void gravity() {
		
	}
	
	@Override
	public void resolveCollisions(ArrayList<Node> nodes) {
		for (Node m : nodes) {
			if (m.equals(this))
				continue;
			
			Vector3 mtd = bounds.penetration().cpy();
			
			float im1 = 1 / mass.floatValue();
			float im2 = 1 / m.mass().floatValue();

			position.add(mtd.cpy().mul(im1 / (im1 + im2)));
//			if( collidesWith(neighborhood.getNeighbors().get(0) ) ) {
//				n.position.sub(mtd.cpy().mul(im2 / (im1 + im2)));
//			}
//			n.position().sub(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = velocity.cpy().sub(m.velocity());
			float vn = v.cpy().dot(mtd.nor());

			// collision impulse			
			float i = (-(1.0f + restitution) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			velocity.add(impulse.mul(im1));
//			n.velocity.sub(impulse.mul(im2));
			
			// Friction
			friction.add(velocity);
			
		}
	}
	
}
