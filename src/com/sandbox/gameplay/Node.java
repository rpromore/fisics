package com.sandbox.gameplay;

import java.math.BigInteger;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class Node {
	private Vector3 position, velocity, target, acceleration;
	private float width, height, radius, gravity, maxVelocity, maxAcceleration;
	private BigInteger mass;
	Neighborhood neighborhood;

	// CONSTRUCTORS

	public Node(float x, float y, Neighborhood neighborhood) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), 100, 100, 50, new BigInteger("1"),
				neighborhood, 100, .1f);
	}
	
	public Node(float x, float y, float width, float height, Neighborhood neighborhood) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, (float) Math.sqrt(Math.pow(width, 2)+Math.pow(height,  2))*.5f, new BigInteger("1"),
				neighborhood, 100, .1f);
	}

	public Node(Vector3 position, Vector3 velocity, Vector3 acceleration,
			float width, float height, float radius, BigInteger mass,
			Neighborhood neighborhood, float maxVelocity, float maxAcceleration) {
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.mass = mass;
		this.neighborhood = neighborhood;
		this.target = new Vector3(0, 0, 0);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
	}

	// GETTERS AND SETTERS

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

	public float radius() {
		return radius;
	}

	public void radius(float r) {
		radius = r;
	}

	public BigInteger mass() {
		return mass;
	}

	public void mass(BigInteger m) {
		mass = m;
	}
	
	public void maxVelocity(int n) {
		maxVelocity = n;
	}
	public float maxVelocity() {
		return maxVelocity;
	}
	
	// BEHAVIORS
	
	public void seek(float x, float y) {
		seek(new Vector3(x, y, 0), false);
	}
	
	public void seek(Vector3 pl) {
		seek(pl, false);
	}

	public void seek(Vector3 pl, boolean slowdown) {
		Vector3 steeringForce;
		Vector3 desiredVelocity = pl.cpy().sub(position());
		float d = desiredVelocity.len();
		if( d > 1f ) {
			if( slowdown && d < 100 )
				desiredVelocity.nor().mul(maxVelocity*(d/100));
			else
				desiredVelocity.nor().mul(maxVelocity);
			
			steeringForce = desiredVelocity.cpy().sub(velocity);
		}
		else
			steeringForce = new Vector3(0, 0, 0);

		velocity.add(steeringForce);
	}

	public void arrive(float x, float y) {
		arrive(new Vector3(x, y, 0));
	}
	public void arrive(Vector3 pl) {
		seek(pl, true);
	}

	// REST

	public boolean intersects(Node other) {
		final double a = radius + other.radius;
		final double dx = getX() - other.getX();
		final double dy = getY() - other.getY();
		
		return a * a > (dx * dx + dy * dy);
	}

	public boolean collidesWith(Node n) {
		if (n != null && !equals(n)) {
			return intersects(n);
		}
		return false;
	}

	public boolean colliding() {
		ArrayList<Node> collidingWith = new ArrayList<Node>();
		for (Node n : neighborhood.getNeighbors()) {
			if (collidesWith(n)) {
				collidingWith.add(n);
			}
		}
		resolveCollisions(collidingWith);
		return collidingWith.size() > 0;
	}

	public void resolveCollisions(ArrayList<Node> nodes) {
		for (Node n : nodes) {
			if (n.equals(this))
				continue;

			// http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
			Vector3 delta = position().cpy().sub(n.position());
			float d = delta.len();
			if (d > radius() + n.radius()) // just kidding
				continue;
			Vector3 mtd;
			if (d != 0f)
				mtd = delta.cpy().mul((radius() + n.radius() - d) / d);
			else {
				d = n.radius + radius - 1.0f;
				delta = new Vector3(n.radius + radius(), 0, 0);
				mtd = delta.mul(((radius() + n.radius) - d) / d);
			}

			float im1 = 1 / mass.floatValue();
			float im2 = 1 / n.mass.floatValue();

			position().add(mtd.cpy().mul(im1 / (im1 + im2)));
			n.position().sub(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = velocity.cpy().sub(n.velocity);
			float vn = v.dot(mtd.nor());

			// sphere intersecting but moving away from each other already
			if (vn > 0.0f)
				return;

			// collision impulse
			float i = (-(1.0f + 0.085f) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);

			// change in momentum
			velocity.add(impulse.mul(im1));
			n.velocity.sub(impulse.mul(im2));
		}
	}
	
	public void gravity() {
		// TODO Decide if this should stay.
		acceleration.mul(0);
		float g = (float) (6.674*Math.pow(10, -11));
		for( Node n : neighborhood.getNeighbors() ) {
			if( !equals(n) ) {
				float m = n.mass.floatValue()*mass.floatValue();
				Vector3 p = n.position.cpy().sub(position);
				float d = p.len2();
				p.nor();
				float f = (float) (g*m)/d;
				p.mul(f);
				p.div(mass.floatValue());
				acceleration.add(p);
			}
		}
	}

	public void move() {
		velocity.add(acceleration);
		position.add(velocity);
		
	}
}