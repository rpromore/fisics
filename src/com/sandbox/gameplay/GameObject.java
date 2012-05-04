package com.sandbox.gameplay;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor {
	public TextureRegion skin;

	public float radius;

	public UUID uuid;

	public void setSkin (TextureRegion texture) {
		this.skin = texture;
		super.width = skin.getRegionWidth();
		super.height = skin.getRegionHeight();
	}

	public GameObject (float x, float y, float width, float height) {
		super.width = width;
		super.height = height;
		super.x = x + width / 2;
		super.y = y + height / 2;
		this.radius = (width >= height) ? (width / 2f) : (height / 2f);
		super.rotation = 0;
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, parentAlpha);
		batch.draw(skin, x, y, width, height);
	}

	@Override
	public Actor hit (float x, float y) {
		return x > 0 && x < width && y > 0 && y < height ? this : null;
	}

	public float getX () {
		return super.x;
	}

	public void setX (float x) {
		super.x = x;
	}

	public float getY () {
		return super.y;
	}

	public void setY (float y) {
		super.y = y;
	}

	public float getWidth () {
		return super.width;
	}

	public void setWidth (float w) {
		super.width = w;
	}

	public float getHeight () {
		return super.height;
	}

	public void setHeight (float h) {
		super.height = h;
	}

	public float radius () {
		return this.radius;
	}

	public void radius (float r) {
		this.radius = r;
	}

	public Vector2 position () {
		return new Vector2(super.x, super.y);
	}

	public void position (Vector2 v) {
		setX(v.x);
		setY(v.y);
	}

	public void position (float x, float y) {
		setX(x);
		setY(y);
	}
	
	public float rotation () {
		return super.rotation;
	}

	public void rotation (float r) {
		super.rotation = (r % 360);
	}
	public float angle () {
		return super.rotation;
	}

	public void angle (float a) {
		super.rotation = (a % 360);
	}

	public boolean intersects (GameObject other) {
		final double a = radius*100 + other.radius;
	    final double dx = x - other.x;
	    final double dy = y - other.y;
	    return a * a > (dx * dx + dy * dy);
	}
	public boolean intersects (Vector2 other, float otherRadius) {
		final double a = radius + otherRadius;
	    final double dx = x - other.x;
	    final double dy = other.y - other.y;
	    return a * a > (dx * dx + dy * dy);
	}

	@Override
	public boolean equals(Object other) {
		GameObject obj = (GameObject) other;
		if (other != null)
			return uuid == obj.uuid;
		return false;
	}
}
