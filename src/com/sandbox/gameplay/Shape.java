package com.sandbox.gameplay;

import java.math.BigInteger;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Shape extends Particle implements Neighborhood {	
	public Shape(ArrayList<Node> v) {
		super(v.get(0).getX(), v.get(0).getY());
		vertices = v;
		updateBounds();
	}
	
	// Mass: 		distribute mass equally among all points, i.e. uniform mass. Ponder this.
	
	public void draw(OrthographicCamera camera) {
		update();
		camera.update();
		sr.setProjectionMatrix(camera.combined);
		
		sr.begin(ShapeType.FilledCircle);
		sr.setColor(0, 1, 0, 1);
		sr.filledCircle(bounds.center().x, bounds.center().y, 5);
		sr.end();
		
		for( int i = 0; i < vertices.size(); i++ ) {
			Node cp = vertices.get(i);
			Node np;
			if( i == vertices.size()-1 )
				np = vertices.get(0);
			else
				np = vertices.get(i+1);
			
			sr.begin(ShapeType.FilledCircle);
			sr.setColor(1, 0, 0, 1);
			sr.filledCircle(cp.getX(), cp.getY(), (float) cp.radius());
			sr.end();
			
			sr.begin(ShapeType.Line);
			sr.setColor(1, 0, 0, 1);
			sr.line(cp.getX(), cp.getY(), np.getX(), np.getY());
			sr.end();
		}
	}
	
	public void move() {
//		for( Node n : vertices ) {
//			n.velocity().add(acceleration());
//			n.position(n.position().add(velocity()));
//		}
		velocity().add(acceleration());
		position(position().add(velocity()));
		updateBounds();
	}
	
	@Override
	public float getX() {
		return bounds.center().x;
	}
	@Override
	public float getY() {
		return bounds.center().y;
	}
	@Override
	public float getZ() {
		return bounds.center().z;
	}
	
	@Override
	public Vector3 position() {
		return bounds.center();
	}
	@Override
	public void position(Vector3 p) {
		Vector3 difference = bounds.center().cpy().sub(p);
		for( Node n : vertices ) {
			n.position().sub(difference);
		}
	}

	@Override
	public ArrayList<Node> getNeighbors() {
		return vertices;
	}

	@Override
	public Neighborhood neighborhood() {
		return neighborhood;
	}

	@Override
	public void neighborhood(Neighborhood n) {
		neighborhood = n;
	}

	@Override
	public void target(Vector3 t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void target(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void target(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector3 target() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double radius() {
		return bounds.center().len();
	}

	@Override
	public void radius(double r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BigInteger mass() {
		// TODO
		return null;
	}

	@Override
	public void mass(BigInteger m) {
		// TODO
		// mass = m;
	}

	@Override
	public void maxVelocity(int n) {
		// TODO
		// maxVelocity = n;
	}

	@Override
	public float maxVelocity() {
		// TODO
		return 0;
	}
	
	@Override
	public boolean intersects(Node n) {
		// 	using this page for help:
		// 		http://www.wildbunny.co.uk/blog/2011/04/20/collision-detection-for-dummies/
		
		// 	First we do AABB vs AABB collision test since it's easier and faster to calculate 
		//	than a more complex collision test.
		if( bounds.intersects(n.bounds) ) {
			return true;
		}
		
		return false;
	}
}
