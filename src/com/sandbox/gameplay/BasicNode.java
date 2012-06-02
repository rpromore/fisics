package com.sandbox.gameplay;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class BasicNode implements Node {
	protected Vector3 position;
	protected Vector3 velocity;
	protected Vector3 target;
	protected Vector3 acceleration;
	protected float friction;
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
//		this.volume = (float) (radius.pow(3).floatValue() * Math.PI * (4/3));/*(float) ((4/3) * Math.PI * Math.pow(radius, 3))*/;
		this.density = mass.floatValue()/volume;
		this.neighborhood = neighborhood;
		this.target = new Vector3(0, 0, 0);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.vertices = new ArrayList<Node>();
		vertices.add(this);
		
		friction = 0f;
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
		return bounds.intersects(n.bounds());
	}
	
	public boolean willIntersect(Node n) {
		Node m = new BasicNode(this);
		m.position().add(m.velocity());
		return m.bounds().intersects(n.bounds());
	}

	public boolean collidesWith(Node n) {
		if (n != null && !equals(n)) {
			return intersects(n);
		}
		return false;
	}
	
	public boolean willCollideWith(Node n) {
		if (n != null && !equals(n)) {
			return willIntersect(n);
		}
		return false;
	}

	public boolean colliding() {
		if( neighborhood != null ) {
			ArrayList<Node> collidingWith = new ArrayList<Node>();
			ArrayList<Node> willCollideWith = new ArrayList<Node>();
			
			for (Node n : neighborhood.getNeighbors()) {
				if (collidesWith(n)) {
					collidingWith.add(n);
				}
				if( willCollideWith(n)) {
					willCollideWith.add(n);
				}
			}
			if( collidingWith.size() > 0 ) 	
				resolveCollisions(collidingWith);
			if( willCollideWith.size() > 0 )
				resolveFutureCollisions(willCollideWith);
	
			return collidingWith.size() > 0 && willCollideWith.size() > 0;
		}
		return false;
	}
	
	public void resolveFutureCollisions(ArrayList<Node> nodes) {
		for( Node m : nodes ) {
			if( m.equals(this) )
				continue;
			
			
		}
	}

	public void resolveCollisions(ArrayList<Node> nodes) {
		for (Node m : nodes) {
			if (m.equals(this))
				continue;
			
			Vector3 delta = m.position().cpy().sub(position);
			
//			if( delta.len() > 1 ) return;
			
			Vector3 mtd = bounds.penetration().cpy();
			
			System.out.println(mtd);
			
			float im1 = 1 / mass.floatValue();
			float im2 = 1 / m.mass().floatValue();

			position.add(mtd.cpy().mul(im1 / (im1 + im2)));
//			m.position().sub(mtd.cpy().mul(im2 / (im1 + im2)));

			// impact speed
			Vector3 v = velocity.cpy().sub(m.velocity());
			float vn = v.cpy().dot(mtd.nor());

			// collision impulse			
			float i = (-(1.0f + restitution) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			velocity.add(impulse.mul(im1));
//			m.velocity().sub(impulse.mul(im2));
			
			// Friction
			float fric = velocity.len();
			float maxFric = .02f;
			if( fric > maxFric )
				fric = maxFric;
			
		}
	}
	
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
					
//					float m = p.mass().floatValue();
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
		velocity.add(acceleration).add(friction);
		position.add(velocity);
	}
	
	public void update() {
		acceleration.mul(0);
		gravity();
		colliding();
		move();
		friction = 0;
	}
	
	public void draw(Camera camera) {
		update();
	}
}
