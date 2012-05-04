package com.sandbox.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sandbox.Sandbox;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
	
	protected final Sandbox game;
	protected final BitmapFont font;
	protected final SpriteBatch batch;
	
	protected static final int VIRTUAL_WIDTH = 800;
	protected static final int VIRTUAL_HEIGHT = 600;
	private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
	private Rectangle viewport;
	
	protected InputMultiplexer inputs;
	
	protected GL10 gl;
	
	protected Stage stage;
	
	protected OrthographicCamera camera;

	public AbstractScreen(Sandbox game) {
		this.game = game;
		this.font = new BitmapFont();
		this.batch = new SpriteBatch();
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		this.gl = Gdx.graphics.getGL10();
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		camera.position.set(0, 0, 0);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		float aspectRatio = (float)width/(float)height;
	    float scale = 1f;
	    Vector2 crop = new Vector2(0f, 0f);

	    if(aspectRatio > ASPECT_RATIO)
	    {
	        scale = (float)height/(float)VIRTUAL_HEIGHT;
	        crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
	    }
	    else if(aspectRatio < ASPECT_RATIO)
	    {
	        scale = (float)width/(float)VIRTUAL_WIDTH;
	        crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
	    }
	    else
	    {
	        scale = (float)width/(float)VIRTUAL_WIDTH;
	    }

	    float w = (float)VIRTUAL_WIDTH*scale;
	    float h = (float)VIRTUAL_HEIGHT*scale;
	    viewport = new Rectangle(crop.x, crop.y, w, h);
	}

	@Override
	public void render(float delta) {
		batch.enableBlending();
		gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
		stage.dispose();
	}
}