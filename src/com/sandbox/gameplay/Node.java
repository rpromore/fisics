package com.sandbox.gameplay;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class Node {
	protected Vector3 position, velocity, target, acceleration, friction;
	protected float maxVelocity, maxAcceleration, volume, density, restitution;
	protected BigDecimal mass, width, height;
	// double width, height, radius;
	Neighborhood neighborhood;
	
	protected ArrayList<Node> vertices;
	protected ShapeRenderer sr = new ShapeRenderer();
	protected AABB bounds = new AABB();

	// CONSTRUCTORS

	public Node(Vector3 position, Vector3 velocity, Vector3 acceleration,
			BigDecimal width, BigDecimal height, float restitution, BigDecimal mass,
			Neighborhood neighborhood, float maxVelocity, float maxAcceleration) {
		this.position = new Vector3(position);
		this.velocity = new Vector3(velocity);
		this.acceleration = new Vector3(acceleration);
		this.width = width;
		this.height = height;
		this.restitution(restitution);
		this.mass = mass;
//		this.volume = (float) (radius.pow(3).floatValue() * Math.PI * (4/3));/*(float) ((4/3) * Math.PI * Math.pow(radius, 3))*/;
		this.density = mass.floatValue()/volume;
		this.neighborhood = neighborhood;
		this.target = new Vector3(0, 0, 0);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.vertices = new ArrayList<Node>();
		bounds = new AABB();
		vertices.add(this);
		updateBounds();
		
		friction = new Vector3(0,0,0);
	}

	// GETTERS AND SETTERS
	
	public AABB bounds() {
		return bounds;
	}

	public Vector3 position() {
		return position;
	}

	public void position(Vector3 v) {
		position.set(v);
	}

	public Vector3 velocity() {
		return velocity;
	}

	public void velocity(Vector3 v) {
		velocity.set(v);
	}
	
	public void target(Vector3 t) {
		target.set(t);
	}
	public void target(int x, int y) {
		target(new Vector3(x, y, 0));
	}
	public void target(float x, float y) {
		target(new Vector3(x, y, 0));
	}
	public Vector3 target() {
		return target;
	}
	
	public Vector3 acceleration() {
		return acceleration;
	}

	public void acceleration(Vector3 v) {
		acceleration.set(v);
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	
	public float getZ() {
		return position.z;
	}

	public BigDecimal width() {
		return width;
	}

	public BigDecimal height() {
		return height;
	}
	
	
	public float restitution() {
		return restitution;
	}
	
	
	public void restitution(float r) {
		restitution = r;
	}

	
	public BigDecimal mass() {
		return mass;
	}
	public void mass(BigDecimal m) {
		mass = m;
	}
	
	
	public void maxVelocity(int n) {
		maxVelocity = n;
	}
	public float maxVelocity() {
		return maxVelocity;
	}
	
	
	public Neighborhood neighborhood() {
		return neighborhood;
	}
	public void neighborhood(Neighborhood n) {
		neighborhood = n;
	}

	// REST

	public boolean intersects(Node n) {
		return bounds.intersects(n.bounds);
	}

	public boolean collidesWith(Node n) {
		if (n != null && !equals(n)) {
			return intersects(n);
		}
		return false;
	}

	public boolean colliding() {
		if( neighborhood != null ) {
			ArrayList<Node> collidingWith = new ArrayList<Node>();
			for (Node n : neighborhood.getNeighbors()) {
				Node p = (Node) n;
				if (collidesWith(n)) {
					collidingWith.add(p);
				}
			}
			if( collidingWith.size() > 0 ) 	
				resolveCollisions(collidingWith);
	
			return collidingWith.size() > 0;
		}
		return false;
	}

	public void resolveCollisions(ArrayList<Node> nodes) {
		
	}
	
	public void gravity() {
		if( neighborhood != null ) {
			float g = (float) (6.674*Math.pow(10, -11));
			for( Node n : neighborhood.getNeighbors() ) {
				Node p = (Node) n;
				if( !equals(p) ) {
					float m = p.mass.floatValue()*mass.floatValue();
					Vector3 po = n.position().cpy().sub(position);
					float d = po.len2();
					po.nor();
					float f = (float) g*(m/d);
					po.mul(f);
					po.div(mass.floatValue());
					
//					float m = p.mass.floatValue();
//					Vector3 po = n.position().cpy().sub(position);
//					float d = po.len2();
//					po.nor();
//					float f = (float) g*(m/d);
//					po.mul(f);

					acceleration.add(po);
				}
			}
		}
	}
	
	public void move() {
		velocity.add(acceleration).sub(friction);
		position.add(velocity);
	}
	
	public void update() {
		acceleration.mul(0);
		gravity();
		move();
		friction.mul(0);
		colliding();
		updateBounds();
	}

	public void updateBounds() {
		Vector3 min = vertices.get(0).position().cpy();
		Vector3 max = vertices.get(vertices.size()-1).position().cpy();
		
		for( Node n : vertices ) {
			if( n.position().x < min.x )
				min.x = n.position().x;
			if( n.position().y < min.y )
				min.y = n.position().y;
			if( n.position().z < min.z )
				min.z = n.position().z;
			
			if( n.position().x > max.x )
				max.x = n.position().x;
			if( n.position().y > max.y )
				max.y = n.position().y;
			if( n.position().z > max.z )
				max.z = n.position().z;
		}
		
		bounds.lower(min);
		bounds.upper(max);
	}
	
	public void draw(Camera camera) {
		update();
		camera.update();
	}
}
