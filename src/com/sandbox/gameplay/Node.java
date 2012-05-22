package com.sandbox.gameplay;

import java.math.BigDecimal;
import java.util.ArrayList;

import utils.BigSquareRoot;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Node {
	protected Vector3 position, velocity, target, acceleration, friction;
	protected float maxVelocity, maxAcceleration, volume, density, restitution;
	protected BigDecimal mass, width, height, radius;
	// double width, height, radius;
	Neighborhood neighborhood;
	
	protected ArrayList<Node> vertices;
	protected ShapeRenderer sr = new ShapeRenderer();
	protected AABB bounds = new AABB();

	// CONSTRUCTORS

	public Node(Vector3 position, Vector3 velocity, Vector3 acceleration,
			BigDecimal width, BigDecimal height, BigDecimal radius, float restitution, BigDecimal mass,
			Neighborhood neighborhood, float maxVelocity, float maxAcceleration) {
		this.position = new Vector3(position);
		this.velocity = new Vector3(velocity);
		this.acceleration = new Vector3(acceleration);
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.restitution(restitution);
		this.mass = mass;
		this.volume = (float) (radius.pow(3).floatValue() * Math.PI * (4/3));/*(float) ((4/3) * Math.PI * Math.pow(radius, 3))*/;
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
	
	public String bounds() {
		return bounds.toString();
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

	
	public BigDecimal radius() {
		return radius;
	}

	
	public void radius(BigDecimal r) {
		radius = r;
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
		final double a = radius.add(n.radius()).doubleValue();
		final double dx = getX() - n.getX();
		final double dy = getY() - n.getY();
		
		return a * a > (dx * dx + dy * dy);
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
			resolveCollisions(collidingWith);
	
			return collidingWith.size() > 0;
		}
		return false;
	}

	public void resolveCollisions(ArrayList<Node> nodes) {
		for (Node n : nodes) {
			if (n.equals(this))
				continue;

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
			float im2 = 1 / n.mass.floatValue();

			position().add(mtd.cpy().mul(im1 / (im1 + im2)));
			n.position().sub(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = velocity.cpy().sub(n.velocity);
			float vn = v.cpy().dot(mtd.nor());

			// collision impulse			
			float i = (-(1.0f + restitution) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			velocity.add(impulse.mul(im1));
			n.velocity.sub(impulse.mul(im2));
			
			// Friction
			friction.add(velocity.cpy().mul(.25f));
		}
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
