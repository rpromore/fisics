package com.sandbox.gui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;

public class PerspectiveCameraController extends InputAdapter {
	PerspectiveCamera cam;
	int lastX;
	int lastY;
	float angleX = -90;
	float angleY = 0;

	boolean W, A, S, D;
	
	private int forward_key = Keys.W;
	private int backward_key = Keys.S;
	private int left_key = Keys.A;
	private int right_key = Keys.D;
	private int up_key = Keys.Q;
	private int down_key = Keys.Z;
	
	private boolean inverted = false;
	
	private float sensitivity = 0.25f;
	private float speed = 2;
	private float speed_sqrt = (float) Math.sqrt(speed);

	static final float NINETY_DEGREE = 89.99f; // gimbal lock prevention
	
	public int forward() {
		return forward_key;
	}
	public void forward(int n) {
		forward_key = n;
	}

	public int backward() {
		return backward_key;
	}
	public void backward(int n) {
		backward_key = n;
	}
	
	public int left() {
		return left_key;
	}
	public void left(int n) {
		left_key = n;
	}
	
	public int right() {
		return right_key;
	}
	public void right(int n) {
		right_key = n;
	}
	
	public int up() {
		return up_key;
	}
	public void up(int n) {
		up_key = n;
	}
	
	public int down() {
		return down_key;
	}
	public void dow (int n) {
		down_key = n;
	}
	
	public boolean inverted() {
		return inverted;
	}
	public void inverted(boolean n) {
		inverted = n;
	}

	public PerspectiveCameraController(PerspectiveCamera cam) {
		this.cam = cam;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		lastX = x;
		lastY = y;
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		touchDragged(x, y, 0);
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// make sure cursor is confined to window
		Gdx.input.setCursorCatched(true);

		angleX += inverted ? (lastX - x) * sensitivity : (x - lastX) * sensitivity;
		lastX = x;
		angleY += inverted ? (y - lastY) * -sensitivity : (lastY - y) * sensitivity;
		lastY = y;

		if (angleY > NINETY_DEGREE)
			angleY = NINETY_DEGREE;
		else if (angleY < -NINETY_DEGREE)
			angleY = -NINETY_DEGREE;

		// first rotate around y axel
		// then rotate up/down, and
		final float cos = MathUtils.cosDeg(angleY);
		cam.direction.x = MathUtils.cosDeg(angleX) * cos;
		cam.direction.y = MathUtils.sinDeg(angleY) * 1f;
		cam.direction.z = MathUtils.sinDeg(angleX) * cos;
		cam.update();
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		cam.fieldOfView -= -amount * 0.1f;
		cam.update();
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == left_key)
			A = true;
		else if (keycode == right_key)
			D = true;
		else if (keycode == backward_key)
			S = true;
		else if (keycode == forward_key)
			W = true;

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == left_key)
			A = false;
		else if (keycode == right_key)
			D = false;
		else if (keycode == backward_key)
			S = false;
		else if (keycode == forward_key)
			W = false;

		return false;
	}

	void update(float delta) {

		// if all is false
		if (!(A | D | W | S))
			return;

		// is moving diagonal move speed is sqrt of normal
		if ((A ^ D) & (W ^ S))
			delta *= speed_sqrt;
		else {
			// if moving one direction move speed is full
			delta *= speed;
		}

		if (A & !D) {
			cam.position.x += delta * MathUtils.sinDeg(angleX);
			cam.position.z -= delta * MathUtils.cosDeg(angleX);
		}
		if (D & !A) {
			cam.position.x -= delta * MathUtils.sinDeg(angleX);
			cam.position.z += delta * MathUtils.cosDeg(angleX);
		}

		if (W & !S) {
			cam.position.x += delta * cam.direction.x;
			cam.position.y += delta * cam.direction.y;
			cam.position.z += delta * cam.direction.z;
		}
		if (S & !W) {
			cam.position.x -= delta * cam.direction.x;
			cam.position.y -= delta * cam.direction.y;
			cam.position.z -= delta * cam.direction.z;
		}
		cam.update();

	}
}