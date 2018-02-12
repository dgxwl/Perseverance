package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
/**
 * 游戏中的物体
 * @author JAVA
 *
 */
public abstract class GameObject {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public GameObject() {
		
	}

	public GameObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(GameObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public abstract BufferedImage getImage();
	
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}
	
	/**
	 * 碰撞检测
	 * @param obj
	 * @return
	 */
	public boolean hit(GameObject obj) {
		int x1 = this.x - obj.width;
		int x2 = this.x + this.width;
		int y1 = this.y - obj.height;
		int y2 = this.y + this.height;
		int x = obj.x;
		int y = obj.y;
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
	}
}
