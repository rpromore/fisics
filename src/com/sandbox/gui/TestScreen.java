package com.sandbox.gui;

import java.math.BigDecimal;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.Sandbox;
import com.sandbox.gameplay.Node;
import com.sandbox.gameplay.Nodes;
import com.sandbox.gameplay.node2D.Circle;
import com.sandbox.gameplay.node2D.Rectangle;

public class TestScreen extends AbstractScreen {
	Nodes shapes;
	
	Vector3 _touchPoint = new Vector3();

	public TestScreen(Sandbox game) {
		super(game);
	}
	
	@Override
	public void show() {
		super.show();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update(true);
		
		shapes = new Nodes();
		float r = 0f;
		
		Rectangle ass = new Rectangle(0, 0, new BigDecimal("127.5632"), new BigDecimal("127.5632")); /*1.27562*Math.pow(10, 2), 1.27562*Math.pow(10, 2));*/
		ass.mass(new BigDecimal("59736000000000"));
		ass.velocity(new Vector3(0, 0, 0));
		ass.restitution(r);
		shapes.add(ass);
		
		Rectangle ass2 = new Rectangle(200, 0, new BigDecimal("10"), new BigDecimal("10"));
		ass2.mass(new BigDecimal("100"));
		ass2.velocity(new Vector3(-3, 0, 0));
		ass2.restitution(r);
		shapes.add(ass2);
		
		Rectangle ass3 = new Rectangle(0, 200, new BigDecimal("10"), new BigDecimal("10"));
		ass3.velocity(new Vector3(0, 0, 0));
		ass3.restitution(r);
//		shapes.add(ass3);
		
		Rectangle ass4 = new Rectangle(0, -200, new BigDecimal("10"), new BigDecimal("10"));
		ass4.velocity(new Vector3(0, 0, 0));
		ass4.restitution(r);
//		shapes.add(ass4);
		
		Random gen = new Random();
		
		int max = 200;
		int min = -200;
		
//		for( int i = 0; i < 100; i++ ) {
//			float x = gen.nextInt(max - min + 1) + min;
//			float y = gen.nextInt(max - min + 1) + min;
//			Circle a = new Circle(x, y, new BigDecimal("10"), new BigDecimal("10"));
//			a.velocity(new Vector3(0, 0, 0));
//			a.restitution(r);
//			shapes.add(a);
//		}
		
	}
	
	@Override
	public void render(float delta) {		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
		camera.update();
		camera.apply(gl);
		
//		Draw draw = new Draw(camera);
//		draw.update();
		
		batch.begin();
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		
//		for( Node n : shapes.getNeighbors() ) {
//			n.move();
//			n.draw(camera);
//		}
		
//		rectangle.colliding();
//		triangle.colliding();
		
//		System.out.println("RECTANGLE");
//		rectangle.draw(camera);
//		System.out.println("TRIANGLE");
//		triangle.draw(camera);
		
		for( Node n : shapes.getNeighbors() ) {
			n.draw(camera);
		}
		
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
			shapes.get(1).position(_touchPoint);	
			shapes.get(1).velocity(new Vector3(0, -5, 0));
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//if (camera.zoom <= 4)
				((OrthographicCamera)camera).zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			if (((OrthographicCamera)camera).zoom > .0001)
				((OrthographicCamera)camera).zoom -= 0.02;
		}

//		if (Gdx.input.isKeyPressed(Keys.A)) camera.rotate(20 * Gdx.graphics.getDeltaTime(), 0, 1, 0);
//        if (Gdx.input.isKeyPressed(Keys.D)) camera.rotate(-20 * Gdx.graphics.getDeltaTime(), 0, 1, 0);
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			// if (camera.position.x > 0)
			camera.translate(-3, 0, 0);
//			shapes.get(1).velocity(new Vector3(-3, 0, 0));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			// if (camera.position.x < Gdx.graphics.getWidth())
			camera.translate(3, 0, 0);
//			shapes.get(1).velocity(new Vector3(3, 0, 0));
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
//		if (Gdx.input.isKeyPressed(Input.Keys.F3))
//			nodes.get(0).mass(nodes.get(0).mass().add(new BigInteger("1000000000000")));
//		if (Gdx.input.isKeyPressed(Input.Keys.F4))
//			nodes.get(0).mass(nodes.get(0).mass().subtract(new BigInteger("1000000000000")));
//		if (Gdx.input.isKeyPressed(Input.Keys.L))
//			nodes.get(0).velocity(nodes.get(0).velocity().add(new Vector3(.1f, 0, 0)));
//		if (Gdx.input.isKeyPressed(Input.Keys.I))
//			nodes.get(0).velocity(nodes.get(0).velocity().add(new Vector3(0, .1f, 0)));
//		if (Gdx.input.isKeyPressed(Input.Keys.J))
//			nodes.get(0).velocity(nodes.get(0).velocity().sub(new Vector3(.1f, 0, 0)));
//		if (Gdx.input.isKeyPressed(Input.Keys.K))
//			nodes.get(0).velocity(nodes.get(0).velocity().sub(new Vector3(0, .1f, 0)));
	}

}
