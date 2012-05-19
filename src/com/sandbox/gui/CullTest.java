package com.sandbox.gui;

import java.nio.FloatBuffer;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.sandbox.Sandbox;

public class CullTest extends AbstractScreen {
	
	InputMultiplexer multiplexer = new InputMultiplexer();
	PerspectiveCameraController controller;

	public CullTest(Sandbox game) {
		super(game);
	}

	Mesh sphere;
	// PerspectiveCamera cam;
	SpriteBatch batch;
	BitmapFont font;
	Vector3[] positions = new Vector3[100];
	
	PerspectiveCamera camera;

	@Override
	public void show() {
		sphere = ObjLoader
				.loadObj(Gdx.files.internal("data/sphere.obj").read());
		camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		camera.far = 200;
		Random rand = new Random();
		for (int i = 0; i < positions.length; i++) {
			positions[i] = new Vector3(rand.nextFloat() * 100
					- rand.nextFloat() * 100, rand.nextFloat() * 100
					- rand.nextFloat() * 100, rand.nextFloat() * -100 - 3);
		}
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		controller = new PerspectiveCameraController(camera);
		multiplexer.addProcessor(controller);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	private static final int MAX_VERTICES = 10000 * 3;

	@Override
	public void render(float d) {
		GL10 gl = Gdx.gl10;

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);

		// Create light components
		float[] ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
		FloatBuffer ambientLight = BufferUtils.newFloatBuffer(8);
		ambientLight.put(ambient);
		
		float[] diffuse = { 0.8f, 0.8f, 0.8f, 1.0f };
		FloatBuffer diffuseLight = BufferUtils.newFloatBuffer(8);
		diffuseLight.put(diffuse);
		
		float[] spec = { 0.5f, 0.5f, 0.5f, 1.0f };
		FloatBuffer specularLight = BufferUtils.newFloatBuffer(8);
		specularLight.put(spec);
		
		float po[] = { -1.5f, 1.0f, -4.0f, 1.0f };
		FloatBuffer position = BufferUtils.newFloatBuffer(8);
		position.put(po);

		// Assign created components to GL_LIGHT0
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientLight);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseLight);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularLight);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, position);

		controller.update(Gdx.graphics.getDeltaTime()*5);
		camera.update();
		camera.apply(gl);

		int visible = 0;
		for (int i = 0; i < positions.length; i++) {
			if (camera.frustum.sphereInFrustum(positions[i], 1)) {
				gl.glColor4f(1, 1, 1, 1);
				visible++;
			} else {
				gl.glColor4f(1, 0, 0, 1);
			}
			gl.glPushMatrix();
			gl.glTranslatef(positions[i].x, positions[i].y, positions[i].z);
			sphere.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
		
//		int lastTouchX = 0;
//	    int lastTouchY = 0;	
		
//		if (Gdx.input.justTouched()) {
//	      lastTouchX = Gdx.input.getX();
//	      lastTouchY = Gdx.input.getY();
//	    } else if (Gdx.input.isTouched()) {
//	      camera.rotate(0.2f * (lastTouchX - Gdx.input.getX()), 0, 1.0f, 0);
//	      camera.rotate(0.2f * (lastTouchY - Gdx.input.getY()), 1.0f, 0, 0);
//	 
//	      lastTouchX = Gdx.input.getX();
//	      lastTouchY = Gdx.input.getY();
//	    }

		gl.glDisable(GL10.GL_DEPTH_TEST);
		batch.begin();
		font.draw(batch, "visible: " + visible + "/100", 0, 20);
		batch.end();
	}
}