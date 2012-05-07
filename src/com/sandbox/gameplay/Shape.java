package com.sandbox.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Shape extends Node implements Neighborhood {
	private ArrayList<Node> vertices;
	private ShapeRenderer sr = new ShapeRenderer();
	
	public Shape(ArrayList<Node> v) {
		super(v.get(0).getX(), v.get(0).getY());
		for( Node n : v ) {
			n.neighborhood = this;
		}
		vertices = v;
	}
	
	@Override
	public float getX() {
		return vertices.get(0).getX();
	}
	@Override
	public float getY() {
		return vertices.get(0).getY();
	}
	@Override
	public float getZ() {
		return vertices.get(0).getZ();
	}
	
	@Override
	public Vector3 position() {
		return vertices.get(0).position();
	}
	@Override
	public void position(Vector3 p) {
		Vector3 difference = vertices.get(0).position().cpy().sub(p);
		for( Node n : vertices ) {
			n.position(n.position().sub(difference));
		}
	}
	
	@Override
	public Vector3 velocity() {
		// TODO should this be an average of all nodes?
		return vertices.get(0).velocity();
	}
	@Override
	public void velocity(Vector3 p) {
		Vector3 difference = vertices.get(0).velocity().cpy().sub(p);
		for( Node n : vertices ) {
			n.velocity(n.velocity().sub(difference));
		}
	}
	
	@Override
	public Vector3 acceleration() {
		// TODO should this be an average of all nodes?
		return vertices.get(0).acceleration();
	}
	@Override
	public void acceleration(Vector3 p) {
		Vector3 difference = vertices.get(0).acceleration().cpy().sub(p);
		for( Node n : vertices ) {
			n.acceleration(n.acceleration().sub(difference));
		}
	}
	
	// Colliding: 	edge-to-edge collisions.
	// Mass: 		distribute mass equally among all points, i.e. uniform mass. Ponder this.
	
	public void update() {
//		for( Node n : vertices ) {
//			n.move();
//			n.gravity();
//			n.colliding();
//		}
	}
	
	public void draw(OrthographicCamera camera) {
		update();
		camera.update();
		sr.setProjectionMatrix(camera.combined);
		
		for( int i = 0; i < vertices.size(); i++ ) {
			Node cp = vertices.get(i);
			Node np;
			if( i == vertices.size()-1 )
				np = vertices.get(0);
			else
				np = vertices.get(i+1);
			
			sr.begin(ShapeType.FilledCircle);
			sr.setColor(1, 0, 0, 1);
			sr.filledCircle(cp.getX(), cp.getY(), cp.radius());
			sr.end();
			
			sr.begin(ShapeType.Line);
			sr.setColor(1, 0, 0, 1);
			sr.line(cp.getX(), cp.getY(), np.getX(), np.getY());
			sr.end();
		}
	}

	@Override
	public ArrayList<Node> getNeighbors() {
		return vertices;
	}
}
