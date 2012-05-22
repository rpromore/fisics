package com.sandbox.gameplay.node2D;

import java.math.BigDecimal;

import utils.BigSquareRoot;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.Neighborhood;
import com.sandbox.gameplay.Node;

public class Circle extends Node {
	
	private static BigSquareRoot bgs = new BigSquareRoot();

	public Circle(float x, float y) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("50"), 1, new BigDecimal("1"),
				null, 100, .1f);
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))) /*(float) Math.sqrt(Math.pow(width, 2)+Math.pow(height,  2))*.5f*/, 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Circle(int x, int y, BigDecimal width, BigDecimal height) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))), 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Circle(float x, float y, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("50"), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
	}
	public Circle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
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
