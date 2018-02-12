package game;

import java.awt.image.BufferedImage;
/**
 * 子弹类
 * @author JAVA
 *
 */
public class Bullet extends GameObject {

	private static BufferedImage img;
	static {
		img = GameObject.loadImage("bullet.png");
	}
	
	/* 子弹出场方向属性 */
	public static final int D_UP = 1;
	public static final int D_RIGHT = 2;
	public static final int D_BOTTOM = 3;
	public static final int D_LEFT = 4;
	private int direction;
	
	public Bullet(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		width = 10;
		height = 10;
		this.direction = direction;
	}
	/**
	 * 子弹移动
	 * @param xSpeed
	 * @param ySpeed
	 */
	public void step(int xSpeed, int ySpeed) {
		this.x += xSpeed;
		this.y += ySpeed;
	}
	
	@Override
	public BufferedImage getImage() {
		return img;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	/**
	 * 子弹是否出界
	 * @return
	 */
	public boolean isOutOfBound() {
		if (x > World.WIDTH || x < 0 || y < 0 || y > World.HEIGHT) {
			return true;
		}
		return false;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
