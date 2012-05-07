package com.sandbox.gui;

import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.Sandbox;
import com.sandbox.gameplay.Node;
import com.sandbox.gameplay.Nodes;
import com.sandbox.gameplay.Shape;
import com.sandbox.graphics.Draw;

public class TestScreen extends AbstractScreen {
	Nodes nodes;
	Shape shape;
	
	Vector3 _touchPoint = new Vector3();

	public TestScreen(Sandbox game) {
		super(game);
	}
	
	@Override
	public void show() {
		super.show();
		
		nodes = new Nodes();
		nodes.add(new Node(0, 0, 5, 5));
		nodes.add(new Node(100, 0, 5, 5));
		nodes.add(new Node(100, -100, 5, 5));
		nodes.add(new Node(0, -100, 5, 5));
		
		shape = new Shape(nodes.getNeighbors());
	}
	
	@Override
	public void render(float delta) {
		camera.update();
		camera.apply(gl);
		
		Draw draw = new Draw(camera);
		draw.update();
		
		batch.begin();
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		
		shape.draw(camera);
		
		batch.end();
		
		handleInput();
		
		stage.clear();
		super.render(delta);
		
		try {
			Thread.sleep(sleep); // ~60FPS
		} catch (InterruptedException e) {
		}
	}
	
	int sleep = 2;
	
	private void handleInput() {
		if (Gdx.input.isTouched()) {
			camera.unproject(_touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			nodes.get(0).position(_touchPoint);			
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//if (camera.zoom <= 4)
				camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			if (camera.zoom > .0001)
				camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			// if (camera.position.x > 0)
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			// if (camera.position.x < Gdx.graphics.getWidth())
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			// if (camera.position.y > 0)
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			// if (camera.position.y < Gdx.graphics.getHeight())
			camera.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F2))
			sleep += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			if (sleep > 2)
				sleep -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F3))
			nodes.get(0).mass(nodes.get(0).mass().add(new BigInteger("1000000000000")));
		if (Gdx.input.isKeyPressed(Input.Keys.F4))
			nodes.get(0).mass(nodes.get(0).mass().subtract(new BigInteger("1000000000000")));
		if (Gdx.input.isKeyPressed(Input.Keys.L))
			nodes.get(0).velocity(nodes.get(0).velocity().add(new Vector3(.1f, 0, 0)));
		if (Gdx.input.isKeyPressed(Input.Keys.I))
			nodes.get(0).velocity(nodes.get(0).velocity().add(new Vector3(0, .1f, 0)));
		if (Gdx.input.isKeyPressed(Input.Keys.J))
			nodes.get(0).velocity(nodes.get(0).velocity().sub(new Vector3(.1f, 0, 0)));
		if (Gdx.input.isKeyPressed(Input.Keys.K))
			nodes.get(0).velocity(nodes.get(0).velocity().sub(new Vector3(0, .1f, 0)));
	}

}
