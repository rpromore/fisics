package com.sandbox.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
	
	public float mass;
	public final Vector2 velocity;
	public final Vector2 acceleration;
	
	protected float maxVelocity = 2;
	protected float maxAcceleration = 2;
	
	private ArrayList<Vector2> targets;
	
	protected Neighborhood neighborhood;

	public DynamicGameObject (float x, float y, float width, float height) {
		this(x, y, width, height, 1, null);
	}
	public DynamicGameObject (float x, float y, float width, float height, float mass, Neighborhood hood) {
		super(x, y, width, height);
		this.mass = mass;
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.targets = new ArrayList<Vector2>();
		this.targets.add(new Vector2());
		this.neighborhood = hood;
		this.maxVelocity = 2;
		this.maxAcceleration = 2;
	}
	
	// ................................................................................................................
	
	public boolean checkBounds() {
		return (int) getX() > -410 // left
				&& (int) getX() < 440 - width*2 // right
				&& (int) getY() < 865 - height*2 // top
				&& (int) getY() > -865;
	}
	
	public boolean collidesWith(GameObject p) {	
		if (p != null && !equals(p)){
			return super.intersects(p);
		}
		return false;
	}
	public boolean isColliding(ArrayList<DynamicGameObject> players) {
		for( DynamicGameObject p : players ) {
			if( !equals(p) && collidesWith(p) ) 
				return true;
		}
		return false;
	}	
	public boolean colliding() {
		ArrayList<DynamicGameObject> collidingWith = new ArrayList<DynamicGameObject>();
		for( DynamicGameObject p : neighborhood.getNeighbors() ) 
		{
			if( collidesWith(p) ) {
				collidingWith.add(p);
			}
		}
		resolveCollisions(collidingWith);
		return collidingWith.size() > 0;
	}
				
	public void resolveCollisions(ArrayList<DynamicGameObject> players) {
		for( DynamicGameObject p : players ) {
			if( p.equals(this) )
				continue;
			
			// http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
			Vector2 delta = position().sub(p.position());
			float d = delta.len();
			if( d > radius() + p.radius() ) // just kidding
				continue;
			Vector2 mtd = delta.cpy().mul((radius() + p.radius()-d)/d);
			
			float im1 = 1/mass();
			float im2 = 1/p.mass();
			
			position(position().add(mtd.cpy().mul(im1 / (im1 + im2))));
			p.position(p.position().sub(mtd.cpy().mul(im2 / (im1 + im2))));
			
			//get the unit normal and unit tanget vectors
		    Vector2 uN = p.position().cpy().sub(this.position()).nor();
		    Vector2 uT = new Vector2(-uN.y, uN.x);

		    //project ball 1 & 2 's velocities onto the collision axis
		    float v1n = uN.dot(this.velocity);
		    float v1t = uT.dot(this.velocity);
		    float v2n = uN.dot(p.velocity);
		    float v2t = uT.dot(p.velocity);

		    //calculate the post collision normal velocities (tangent velocities don't change)
		    float v1nPost = (v1n*(this.mass-p.mass) + 2*p.mass*v2n)/(this.mass+p.mass);
		    float v2nPost = (v2n*(p.mass-this.mass) + 2*this.mass*v1n)/(this.mass+p.mass);

		    //convert scalar velocities to vectors
		    Vector2 postV1N = uN.mul(v1nPost);
		    Vector2 postV1T = uT.mul(v1t);
		    Vector2 postV2N = uN.mul(v2nPost);
		    Vector2 postV2T = uT.mul(v2t);

		    //change the balls velocities
		    this.velocity(postV1N.add(postV1T));
		    p.velocity(postV2N.add(postV2T));
		}
	}
	public boolean collidesWithinRange(DynamicGameObject p, int range) {
		if( getY() < targets().get(0).y ) {
			if (p != null && getX() < p.getX() + getWidth() + range && getX() > p.getX() - range
					&& getY() < p.getY() + getHeight() && getY() > p.getY() - range)
				return true;
		}
		else {
			if (p != null && getX() < p.getX() + p.getWidth() + range && getX() > p.getX() - range
					&& getY() < p.getY() + p.getHeight() + range && getY() > p.getY() )
				return true;
		}
		if (p != null && getX() < p.getX() + p.getWidth() + range && getX() > p.getX() - range
				&& getY() < p.getY() + p.getHeight() + range && getY() > p.getY() - range )
			return true;
		return false;
	}
	
	// ................................................................................................................
	public float mass() {
		return this.mass;
	}
	public void mass(float m) {
		this.mass = m;
	}
	public Vector2 velocity() {
		return this.velocity;
	}
	public void velocity(Vector2 v) {
		this.velocity.set(v);
	}
	public void velocity(float x, float y) {
		this.velocity.set(x, y);
	}
	public Vector2 acceleration() {
		return this.acceleration;
	}
	public void acceleration(Vector2 a) {
		this.acceleration.set(a);
	}
	public void acceleration(float x, float y) {
		this.acceleration.set(x, y);
	}
	public float maxVelocity() {
		return this.maxVelocity;
	}
	public void maxVelocity(float m) {
		this.maxVelocity = m;
	}
	public float maxAcceleration() {
		return this.maxAcceleration;
	}
	public void maxAcceleration(float m) {
		this.maxAcceleration = m;
	}
	public float maxAccel() {
		return this.maxAcceleration;
	}
	public void maxAccel(float m) {
		this.maxAcceleration = m;
	}
	public ArrayList<Vector2> targets() {
		return this.targets;
	}
	public void targets(ArrayList<Vector2> v){
		this.targets = v;
	}
	public Vector2 target() {
		if( this.targets.size() > 0 )
			return this.targets.get(this.targets.size()-1);
		return new Vector2(0, 0);
	}
	public void target(Vector2 v) {
		this.targets.add(0, v);
	}
	public void target(float x, float y) {
		this.targets.add(0, new Vector2(x, y));
	}
	public void target(int x, int y) {
		this.targets.add(0, new Vector2(x, y));
	}
	public void targetReached() {
		if( this.targets.size() > 0 )
			this.targets.remove(this.targets.size()-1);
	}
	public void clearTargets() {
		this.targets.clear();
	}
	
	public Neighborhood neighborhood() {
		return neighborhood;
	}
	public void neighborhood(Neighborhood n) {
		neighborhood = n;
	}
}
