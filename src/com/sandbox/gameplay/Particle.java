package com.sandbox.gameplay;

import java.math.BigDecimal;
import java.util.ArrayList;

import utils.BigSquareRoot;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Particle implements Node {
	private Vector3 position, velocity, target, acceleration;
	private float maxVelocity, maxAcceleration, volume, density, restitution;
	private BigDecimal mass, width, height, radius;
	// double width, height, radius;
	Neighborhood neighborhood;
	
	protected ArrayList<Node> vertices;
	protected ShapeRenderer sr = new ShapeRenderer();
	protected AABB bounds = new AABB();
	
	private static BigSquareRoot bgs = new BigSquareRoot();

	// CONSTRUCTORS

	public Particle(float x, float y) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("50"), 1, new BigDecimal("1"),
				null, 100, .1f);
	}
	public Particle(float x, float y, BigDecimal width, BigDecimal height) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))) /*(float) Math.sqrt(Math.pow(width, 2)+Math.pow(height,  2))*.5f*/, 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Particle(int x, int y, BigDecimal width, BigDecimal height) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))), 1, new BigDecimal("1"), null, 100, .1f);
	}
	public Particle(float x, float y, Neighborhood neighborhood) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("50"), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
	}
	
	public Particle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		this(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, bgs.get(width.pow(2).add(height.pow(2)).divide(new BigDecimal("2"))), 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
	}

	public Particle(Vector3 position, Vector3 velocity, Vector3 acceleration,
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
	}

	// GETTERS AND SETTERS
	
	public String bounds() {
		return bounds.toString();
	}

	@Override
	public Vector3 position() {
		return position;
	}

	@Override
	public void position(Vector3 v) {
		position.set(v);
	}

	@Override
	public Vector3 velocity() {
		return velocity;
	}

	@Override
	public void velocity(Vector3 v) {
		velocity.set(v);
	}
	
	@Override
	public void target(Vector3 t) {
		target.set(t);
	}
	@Override
	public void target(int x, int y) {
		target(new Vector3(x, y, 0));
	}
	@Override
	public void target(float x, float y) {
		target(new Vector3(x, y, 0));
	}
	@Override
	public Vector3 target() {
		return target;
	}
	
	@Override
	public Vector3 acceleration() {
		return acceleration;
	}

	@Override
	public void acceleration(Vector3 v) {
		acceleration.set(v);
	}

	@Override
	public float getX() {
		return position.x;
	}

	@Override
	public float getY() {
		return position.y;
	}

	@Override
	public float getZ() {
		return position.z;
	}

	@Override
	public BigDecimal radius() {
		return radius;
	}

	@Override
	public void radius(BigDecimal r) {
		radius = r;
	}
	
	@Override
	public float restitution() {
		return restitution;
	}
	
	@Override
	public void restitution(float r) {
		restitution = r;
	}

	@Override
	public BigDecimal mass() {
		return mass;
	}

	@Override
	public void mass(BigDecimal m) {
		mass = m;
	}
	
	@Override
	public void maxVelocity(int n) {
		maxVelocity = n;
	}
	@Override
	public float maxVelocity() {
		return maxVelocity;
	}
	
	@Override
	public Neighborhood neighborhood() {
		return neighborhood;
	}
	@Override
	public void neighborhood(Neighborhood n) {
		neighborhood = n;
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

	public boolean intersects(Node n) {
		final double a = radius.add(n.radius()).doubleValue();
		final double dx = getX() - n.getX();
		final double dy = getY() - n.getY();
		
		return a * a > (dx * dx + dy * dy);
	}
	
	public boolean intersects(Particle n) {
		final double a = radius.add(n.radius()).floatValue();
		final double dx = getX() - n.getX();
		final double dy = getY() - n.getY();
		
		return a * a > (dx * dx + dy * dy);
	}
	
	public boolean willIntersect(Node n) {
		final double a = radius.add(n.radius()).floatValue();
		final double dx = getX() + velocity.x - n.getX();
		final double dy = getY() + velocity.y - n.getY();
		
		return a * a > (dx * dx + dy * dy);
	}

	public boolean collidesWith(Node n) {
		if (n != null && !equals(n)) {
			return intersects(n) || willIntersect(n);
		}
		return false;
	}

	public boolean colliding() {
		if( neighborhood != null ) {
			ArrayList<Particle> collidingWith = new ArrayList<Particle>();
			for (Node n : neighborhood.getNeighbors()) {
				Particle p = (Particle) n;
				if (collidesWith(n)) {
					collidingWith.add(p);
				}
			}
			resolveCollisions(collidingWith);
	
			return collidingWith.size() > 0;
		}
		return false;
	}

	public void resolveCollisions(ArrayList<Particle> nodes) {
		for (Particle n : nodes) {
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
			System.out.println(im1);
			
			float i = (-(1.0f + restitution) * vn) / (im1 + im2);
			Vector3 impulse = mtd.mul(i);
			
			// change in momentum
			velocity.add(impulse.mul(im1));
			n.velocity.sub(impulse.mul(im2));
		}
	}
	
	public void gravity() {
		if( neighborhood != null ) {
			float g = (float) (6.674*Math.pow(10, -11));
			for( Node n : neighborhood.getNeighbors() ) {
				Particle p = (Particle) n;
				if( !equals(p) ) {
//					float m = p.mass.floatValue()*mass.floatValue();
//					Vector3 po = n.position().cpy().sub(position);
//					float d = po.len2();
//					po.nor();
//					float f = (float) g*(m/d);
//					po.mul(f);
//					po.div(mass.floatValue());
					
					float m = p.mass.floatValue();
					Vector3 po = n.position().cpy().sub(position);
					float d = po.len2();
					po.nor();
					float f = (float) g*(m/d);
					po.mul(f);

					acceleration.add(po);
				}
			}
		}
	}

	@Override
	public void move() {
		velocity.add(acceleration);
		position.add(velocity);
	}
	
	public void update() {
		acceleration.mul(0);
		gravity();
		move();
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
