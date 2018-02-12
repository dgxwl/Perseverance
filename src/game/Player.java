package game;

import java.awt.image.BufferedImage;
/**
 * 玩家
 * @author JAVA
 *
 */
public class Player extends GameObject {
	public boolean isAlive;
	private static BufferedImage[] img = new BufferedImage[2];
	
	private int speed;  //移动速度
	
	static {
		img[0] = GameObject.loadImage("alive.png");
		img[1] = GameObject.loadImage("dead.png");
	}
	
	
	
	public Player() {
		isAlive = true;
		this.width = 16;
		this.height = 16;
		this.x = World.WIDTH/2 - this.width/2;		//
		this.y = World.HEIGHT/2 - this.height/2;	//放在屏幕中间
		this.speed = 3;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void stepX(int dir) {
		x += (speed * dir);
	}
	
	public void stepY(int dir) {
		y += (speed * dir);
	}
	
	public boolean isOutOfBoundUp() {
		return y <= 0;
	}
	
	public boolean isOutOfBoundRight() {
		return x >= World.WIDTH-this.width;
	}
	
	public boolean isOutOfBoundBottom() {
		return y >= World.HEIGHT-this.height-32;
	}
	
	public boolean isOutOfBoundLeft() {
		return x <= 0;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void goDead() {
		isAlive = false;
	}

	@Override
	public BufferedImage getImage() {
		if (isAlive) {
			return img[0];
		}
		return img[1];
	}
}
