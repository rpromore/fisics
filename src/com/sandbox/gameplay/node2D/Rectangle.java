package com.sandbox.gameplay.node2D;

import java.math.BigDecimal;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.sandbox.gameplay.BasicNode;
import com.sandbox.gameplay.Neighborhood;
import com.sandbox.gameplay.Node;
import com.sandbox.gameplay.bounds.AABB;

public class Rectangle extends BasicNode implements Node {
	public Rectangle(float x, float y) {
		this(x, y, new BigDecimal("100"), new BigDecimal("100"), null);
	}
	public Rectangle(float x, float y, BigDecimal width, BigDecimal height) {
		this(x, y, width, height, null);
	}
	public Rectangle(int x, int y, BigDecimal width, BigDecimal height) {
		this(x, y, width, height, null);
	}
	public Rectangle(float x, float y, Neighborhood neighborhood) {
		this(x, y, new BigDecimal("100"), new BigDecimal("100"), neighborhood);
	}
	public Rectangle(float x, float y, BigDecimal width, BigDecimal height, Neighborhood neighborhood) {
		super(new Vector3(x, y, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 0), width, height, 1, new BigDecimal("1"),
				neighborhood, 100, .1f);
		this.bounds = new AABB(this);
	}
	
//	@Override
//	public Vector3 position() {
//		return position.cpy().add(width.floatValue()).div(2);
//	}
	
	@Override
	public void draw(Camera camera) {
		update();
		((AABB)bounds).update();
		camera.update();
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Rectangle);
		sr.setColor(1, 0, 0, 1);
		sr.rect(getX(), getY(), width.floatValue(), height.floatValue());
		sr.end();
		
		sr.begin(ShapeType.Point);
		sr.setColor(1, 1, 1, 1);
		sr.point(((AABB)bounds).lower().x, ((AABB)bounds).lower().y, ((AABB)bounds).lower().z);
		sr.setColor(0, 1, 1, 1);
		sr.point(((AABB)bounds).upper().x, ((AABB)bounds).upper().y, ((AABB)bounds).upper().z);
		sr.end();
	}
	
	@Override
	public void gravity() {
//		if( neighborhood != null ) {
//			float g = (float) (6.674*Math.pow(10, -11));
//			for( Node n : neighborhood.getNeighbors() ) {
//				Node p = (Node) n;
//				if( !equals(p) ) {
//					
//					float m = p.mass().floatValue();
//					Vector3 po = n.position().cpy();
//					po.x += width.floatValue();
//					po.x /= 2;
//					po.y += height.floatValue();
//					po.y /= 2;
//					po.sub(position);
//					float d = po.len2();
//					po.nor();
//					float f = (float) g*(m/d);
//					po.mul(f);
//					
//					System.out.println("Gravity: "+po);
//
//					acceleration.add(po);
//				}
//			}
//		}
	}
	
}
