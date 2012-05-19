package com.sandbox.gui;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.TimeUtils;
import com.sandbox.Sandbox;

public class ProjectTest extends AbstractScreen {


	public ProjectTest(Sandbox game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	Mesh sphere;
	PerspectiveCamera cam;
	SpriteBatch batch;
	BitmapFont font;
	Vector3[] positions = new Vector3[100];
	Vector3 tmp = new Vector3();
	TextureRegion logo;
	
	PerspectiveCameraController controller;

	@Override
	public void show () {
		sphere = ObjLoader.loadObj(Gdx.files.internal("data/sphere.obj").read());
//		cam = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam.far = 200;
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 5, 10);
		cam.lookAt(0, 0, 0);
		cam.update();
		controller = new PerspectiveCameraController(cam);
		
		Random rand = new Random();
		for (int i = 0; i < positions.length; i++) {
			positions[i] = new Vector3(rand.nextFloat() * 100 - rand.nextFloat() * 100, rand.nextFloat() * 100 - rand.nextFloat()
				* 100, rand.nextFloat() * -100 - 3);
		}
		batch = new SpriteBatch();
		font = new BitmapFont();
		logo = new TextureRegion(new Texture(Gdx.files.internal("data/badlogicsmall.jpg")));
		
		// Gdx.input.setInputProcessor(controller);

		if (Gdx.input.isKeyPressed(Keys.W)) camera.position.z -= Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.S)) camera.position.z += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.A)) camera.position.x -= Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.D)) camera.position.x += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.Q)) camera.position.y += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.E)) camera.position.y -= Gdx.graphics.getDeltaTime();
	}

	@Override
	public void render (float d) {
		GL10 gl = Gdx.gl10;

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		cam.update();
		cam.apply(gl);

		int visible = 0;
		for (int i = 0; i < positions.length; i++) {
			if (cam.frustum.sphereInFrustum(positions[i], 1)) {
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

		gl.glDisable(GL10.GL_DEPTH_TEST);
		batch.begin();
		for (int i = 0; i < positions.length; i++) {
			tmp.set(positions[i]);
			cam.project(tmp);
			if (tmp.z < 0) continue;
			batch.draw(logo, tmp.x, tmp.y);
		}
		batch.end();
	}
}