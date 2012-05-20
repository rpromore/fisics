package com.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.sandbox.gui.TestScreen;

public class Sandbox extends Game {
	
	private FPSLogger fpsLogger;
	
	@Override
	public void create() {
		setScreen(new TestScreen(this));
		fpsLogger = new FPSLogger();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		fpsLogger.log();
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void resume() {
		super.resume();
	}
	
}
