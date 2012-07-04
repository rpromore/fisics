package com.sandbox.gameplay;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.bounds.Bounds;

public class BasicNode implements Node {
	protected Vector3 position;
	protected Vector3 velocity;
	protected Vector3 target;
	protected Vector3 acceleration;
	protected Vector3 friction;
	protected float maxVelocity, maxAcceleration, volume, density, restitution;
	protected BigDecimal mass;
	protected BigDecimal width;
	protected BigDecimal height;
	// double width, height, radius;
	protected Neighborhood neighborhood;
	
	protected ArrayList<Node> vertices;
	protected ShapeRenderer sr = new ShapeRenderer();
	protected Bounds bounds;

	// CONSTRUCTORS
	
	public BasicNode(BasicNode n) {
		this(n.position, n.velocity, n.acceleration, n.width, n.height, n.restitution, n.mass, n.neighborhood, n.maxVelocity, n.maxAcceleration);
		bounds = n.bounds();
	}

	public BasicNode(Vector3 position, Vector3 velocity, Vector3 acceleration,
			BigDecimal width, BigDecimal height, float restitution, BigDecimal mass,
			Neighborhood neighborhood, float maxVelocity, float maxAcceleration) {
		this.position = new Vector3(position);
		this.velocity = new Vector3(velocity);
		this.acceleration = new Vector3(acceleration);
		this.width = width;
		this.height = height;
		this.restitution(restitution);
		this.mass = mass;
		this.density = mass.floatValue()/volume;
		this.neighborhood = neighborhood;
		this.target = new Vector3(0, 0, 0);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.vertices = new ArrayList<Node>();
		vertices.add(this);
		
		friction = new Vector3();
	}

	// GETTERS AND SETTERS
	
	public Bounds bounds() {
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
	
	public Vector3 friction() {
		return friction;
	}

	public void friction(Vector3 v) {
		friction.set(v);
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

	public Vector3 intersects(Node n) {
		return bounds.intersects(n.bounds());
	}
	
//	public boolean willIntersect(Node n) {
//		Node m = new BasicNode(this);
//		((BasicNode)m).acceleration().mul(0);
//		((BasicNode)m).gravity();
//		((BasicNode)m).move();
//		return m.bounds().intersects(n.bounds());
//	}

//	public boolean collidesWith(Node n) {
//		if (n != null && !equals(n)) {
//			return intersects(n);
//		}
//		return false;
//	}
//	
//	public boolean willCollideWith(Node n) {
//		if (n != null && !equals(n)) {
//			return willIntersect(n);
//		}
//		return false;
//	}
	
	public void gravity() {
		if( neighborhood != null ) {
			float g = (float) (6.674*Math.pow(10, -11));
			for( Node n : neighborhood.getNeighbors() ) {
				Node p = (Node) n;
				if( !equals(p) ) {
					float m = p.mass().floatValue()*mass.floatValue();
					Vector3 po = n.position().cpy().sub(position);
					float d = po.len2();
					po.nor();
					float f = (float) g*(m/d);
					po.mul(f);
					po.div(mass.floatValue());

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
	}
	
	public void draw(Camera camera) {
	}
}
