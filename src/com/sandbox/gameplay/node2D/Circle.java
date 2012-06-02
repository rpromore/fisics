package com.sandbox.gameplay.node2D;

import java.math.BigDecimal;

import utils.BigSquareRoot;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.BasicNode;
import com.sandbox.gameplay.Neighborhood;
import com.sandbox.gameplay.Node;

public class Circle extends BasicNode implements Node {
	
	BigDecimal radius;
	
	private static BigSquareRoot bgs = new BigSquareRoot();

	public Circle(float x, float y) {
		this(x, y, new BigDecimal("100"), new BigDecimal("100"), null);
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height) {
		this(x, y, width, height, null);
	}
	public Circle(int x, int y, BigDecimal width, BigDecimal height) {
		this(x, y, width, height, null);
	}
	public Circle(float x, float y, Neighborhood neighborhood) {
		this(x, y, new BigDecimal("100"), new BigDecimal("100"), neighborhood);
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
//		this.radius = bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2")));
		this.radius = new BigDecimal(width.floatValue()/2);
		this.bounds = new com.sandbox.gameplay.bounds.Circle(this);
	}
	
	public BigDecimal radius() {
		System.out.println("Radius: "+radius);
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
	
}
