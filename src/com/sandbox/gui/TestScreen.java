package com.sandbox.gui;

import java.math.BigInteger;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
		
//		Nodes points = new Nodes();
//		points.add(new Point(0, 0));
//		points.add(new Point(100, 0));
//		points.add(new Point(100, -100));
//		points.add(new Point(100, -200));
//		points.add(new Point(50, -200));
//		points.add(new Point(0, -100));
		
		nodes = new Nodes();
//		Node n1 = new Node(0, 0, nodes);
//		n1.acceleration(1.1f);
//		n1.target(100, 100);
//		nodes.add(n1);
//		
//		Node n2 = new Node(100, 100, nodes);
//		nodes.add(n2);
		Random gen = new Random();
		Node n = new Node(-100, 100, 5, 5, nodes);
		n.mass(new BigInteger("100000"));
		nodes.add(n);
//		for( int i = 0; i < 100; i++ ) {
//			n = new Node(gen.nextInt(300), gen.nextInt(300), 25, 25, nodes);
//			if( i == 0 )
//				n.mass(new BigInteger("100000"));
//			nodes.add(n);
//		}
		
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
		
		// Vector3 p_before = nodes.get(3).position();
		
//		Node n = nodes.getNeighbors().get(0);
//		n.gravity();
//		n.move();
//		n.colliding();
//		draw.filledCircle(n.getX(), n.getY(), n.radius(), new Color(255, 255, 255, .5f));
		
		// for( int i = 0; i < nodes.getNeighbors().size(); i++ ) {
		for( Node n : nodes.getNeighbors() ) {
			n.gravity();
			n.move();
			n.colliding();
			draw.filledCircle(n.getX(), n.getY(), n.radius(), new Color(255, 0, 0, .75f));
		}
		
//		Vector3 p_after = nodes.get(3).position();
//		Vector3 d = p_after.cpy().sub(p_before);
//		camera.translate(d.x, d.y, d.z);
		
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
		if (Gdx.input.justTouched()) {
			camera.unproject(_touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
		}
		if (Gdx.input.isTouched()) {
			camera.unproject(_touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			nodes.get(0).position(_touchPoint);
//			nodes.get(0).velocity(new Vector3(2, 2, 0));
//			nodes.get(1).position(new Vector3(_touchPoint.x+200, _touchPoint.y+200, 0));
//			nodes.get(1).velocity(new Vector3(-2, -2, 0));
//			nodes.get(2).position(new Vector3(_touchPoint.x, _touchPoint.y+200, 0));
//			nodes.get(2).velocity(new Vector3(2, -2, 0));
//			nodes.get(3).position(new Vector3(_touchPoint.x+200, _touchPoint.y, 0));
//			nodes.get(3).velocity(new Vector3(-2, 2, 0));
			
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
