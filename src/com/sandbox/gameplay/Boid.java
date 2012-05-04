package com.sandbox.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Boid extends DynamicGameObject {
	/* ************************ VARIABLES ************************ */
	public static float width = 26;
	public static float height = 26;

	private int id;

	public static enum TeamColor {
		RED, ORANGE, YELLOW, GREEN, BLUE, TEAL, PURPLE
	}

	/**
	 * The various behaviors available to each player, seek and flee.
	 */
	public static enum Behavior {
		SEEK, FLEE, PURSUE, MOVE
	};

	/* ************************ CONSTRUCTORS ********************* */

	public Boid(float x, float y, int id, Neighborhood neighborhood) {
		this(x, y, width, height, -1, 2, 1f, id, neighborhood);
	}

	public Boid(Boid p) {
		this(p.x, p.y, Boid.width, Boid.height, -1, p.maxVelocity,
				p.maxAcceleration, p.id, p.neighborhood);
	}

	public Boid(float x, float y, float width, float height, float radius,
			float maxVelocity, float maxAcceleration, int id, Neighborhood neighborhood) {
		super(x, y, width, height);
		this.id = id;
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.mass = 250;
		this.neighborhood = neighborhood;
	}

	public void draw(SpriteBatch spriteBatch, float angle) {
		
	}

	/* ************************ GETTERS AND SETTERS ************************ */
	public int getID() {
		return id;
	}

	public void setID(int i) {
		id = i;
	}

	public boolean equals(Object p) {
		Boid o = (Boid) p;
		if (p != null)
			return id == o.id;
		return false;
	}
	
	public void separate(float distance) {
		Vector2 sum = new Vector2(0, 0);
		int count = 0;
		for( Boid b : neighborhood.getNeighbors() ) {
			float d = b.position().cpy().sub(position()).len();
			if( d > 0 && d < distance ) {
				Vector2 diff = position().cpy().sub(b.position()).nor();
				diff.set(diff.x/d, diff.y/d);
				sum.add(diff);
				count++;
			}
		}
		if( count > 0 )
			sum.set(sum.x/count, sum.y/count);
		System.out.println("COUNT: "+count);
		velocity.add(sum.mul(2));
	}

	public void seek(float x, float y) {
		seek(new Vector2(x, y), false);
	}
	
	public void seek(Vector2 pl) {
		seek(pl, false);
	}

	public void seek(Vector2 pl, boolean slowdown) {
		System.out.println("SEEKING: "+pl);
		Vector2 steeringForce;
		Vector2 desiredVelocity = pl.cpy().sub(position());
		float d = desiredVelocity.len();
		if( d > 1f ) {
			if( slowdown && d < 100 )
				desiredVelocity.nor().mul(maxVelocity*(d/100));
			else
				desiredVelocity.nor().mul(maxVelocity);
			
			steeringForce = desiredVelocity.cpy().sub(velocity);
		}
		else
			steeringForce = new Vector2(0, 0);

		velocity.add(steeringForce);
	}

	public void flee(float x, float y) {
		flee(new Vector2(x, y));
	}

	public void flee(Vector2 pl) {
		seek(pl.mul(-1), false);
	}

	public void arrive(float x, float y) {
		arrive(new Vector2(x, y));
	}
	public void arrive(Vector2 pl) {
		seek(pl, true);
	}

	public void pursue(DynamicGameObject pl) {
		float c = (float) 0.25;
		Vector2 target2 = null;
		float d = pl.position().sub(this.position()).len();
		float T = d * c;
		
	}

	protected void evade(DynamicGameObject pl) {
		float d = pl.position().dst(position());
		float T = d / this.maxVelocity;
		Vector2 target = pl.position().add(pl.velocity.cpy().mul(T / 2));

		
	}

	public void avoid(ArrayList<Boid> players) {
		
	}
	
	public boolean positionOccupied(Vector2 target) {
		for( Boid b : neighborhood.getNeighbors() ) {
			if( b.intersects(target, b.radius) )
				return true;
		}
		return false;
	}
	
	public Vector2 findNewTarget(Vector2 target) {
		Vector2 next;
		
		return null;
	}
	
	public void checkTarget() {
		if( positionOccupied(target()) ) {
			clearTargets();
			//target(200, 200);
		}
	}

	public void move() {
		if( velocity.len() > maxVelocity )
			velocity.set(maxVelocity, maxVelocity);
		position(position().add(velocity));
		velocity.set(0, 0);
	}
}
