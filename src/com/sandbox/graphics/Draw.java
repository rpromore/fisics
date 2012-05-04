package com.sandbox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Draw {
	private OrthographicCamera camera;
	private GL10 gl = Gdx.graphics.getGL10();
	private ShapeRenderer s = new ShapeRenderer();
	
	/**
	 * Creates the projection view for the items to be drawn
	 * @param camera the orthographic camera that the drawings will be projected to
	 */
	public Draw(OrthographicCamera camera){
		this.camera = camera;
	}
	
	public void update() {
		gl.glEnable(GL10.GL_LINE_SMOOTH | GL10.GL_BLEND);
		gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void line(float x1, float y1, float x2, float y2, Color color, int size) {
		camera.update();
		s.setProjectionMatrix(camera.combined);
		gl.glLineWidth(size);
		s.begin(ShapeType.Line);
		s.setColor(color);
		s.line(x1, y1, x2, y2);
		s.end();
	}
	
	public void line(float x1, float y1, float x2, float y2, Color color) {
		line(x1, y1, x2, y2, color, 1);
	}
	
	public void rectangle(float x, float y, float width, float height, Color color){
		this.rectangle(x, y, width, height, color, 0);
	}
	
	public void rectangle(float x, float y, float width, float height, Color color, float angle) {
		s.begin(ShapeType.Rectangle);
		s.setProjectionMatrix(camera.combined);
		s.setColor(color);
		s.rotate(0, 0, 1, angle);
		s.rect(x, y, width, height);
		s.end();
	}
	
	public void filledRectangle(float x, float y, float width, float height, Color color){
		this.filledRectangle(x, y, width, height, color, 0);
	}
	
	public void filledRectangle(float x, float y, float width, float height, Color color, float angle) {
		s.begin(ShapeType.FilledRectangle);
		s.setProjectionMatrix(camera.combined);
		s.setColor(color);
		s.rotate(0, 0, 1, angle);
		s.filledRect(x, y, width, height);
		s.end();
	}
	
	
	
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color){
		this.triangle(x1, y1, x2, y2, x3, y3, color, 0);
	}
	
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color, float angle){
		s.begin(ShapeType.FilledTriangle);
		s.setProjectionMatrix(camera.combined);
		s.setColor(color);
		s.rotate(0, 0, 1, angle);
		s.filledTriangle(x1, y1, x2, y2, x3, y3);
		s.end();
	}
	
	public void filledCircle(float x, float y, float radius, Color color){
		this.filledCircle(x, y, radius, color, 0);
	}
	
	public void filledCircle(float x, float y, float radius, Color color, float angle){
		s.begin(ShapeType.FilledCircle);
		s.setProjectionMatrix(camera.combined);
		s.setColor(color);
		s.rotate(0, 0, 1, angle);
		s.filledCircle(x, y, radius, 100);
		s.end();
	}

	public void circle(float x, float y, float radius, Color color){
		this.circle(x, y, radius, color, 1, 0);
	}
	public void circle(float x, float y, float radius, Color color, int size) {
		this.circle(x, y, radius, color, size, 0);
	}
	public void circle(float x, float y, float radius, Color color, int size, float angle){
		s.begin(ShapeType.Circle);
		s.setProjectionMatrix(camera.combined);
		gl.glLineWidth(size);
		s.setColor(color);
		s.rotate(0, 0, 1, angle);
		s.circle(x, y, radius);
		s.end();
	}	
}
