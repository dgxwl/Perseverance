package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 700;	//
	public static final int HEIGHT = 700;	//窗口宽高
	
	public static final int START = 0;  //游戏启动
	public static final int RUNNING = 1;  //游戏运行
	public static final int GAME_OVER = 2;  //游戏结束
	private int state = START;	//游戏状态
	
	/* 按键开关 */
	private boolean kUp = false;
	private boolean kRight = false;
	private boolean kDown = false;
	private boolean kLeft = false;
	
	private long startTime;
	private long endTime;
	
	private Player player = new Player();
	private Bullet[] bullets = new Bullet[80];
	
	//初始化子弹数组
	private void initBullet() {
		int direction;
		Random random = new Random();
		for (int i = 0; i < bullets.length; i++) {
			direction = (int) (random.nextInt(4)+1);  //随机生成子弹出场方向
			switch (direction) {
			case 1://上
				bullets[i] = new Bullet(random.nextInt(WIDTH-10), -10, direction);
				break;
			case 2://右
				bullets[i] = new Bullet(WIDTH, random.nextInt(HEIGHT-10), direction);
				break;
			case 3://下
				bullets[i] = new Bullet(random.nextInt(WIDTH-10), HEIGHT, direction);
				break;
			case 4://左
				bullets[i] = new Bullet(-10, random.nextInt(HEIGHT-10), direction);
				break;
			}
		}
	}
	
	/**
	 * 子弹随机移动
	 */
	int stepIndex = 0;
	public void bulletStepAction() {
		if (state == RUNNING) {
			stepIndex++;
			stepIndex %= 3;		//子弹速度调节(定时器10ms执行一次,此处控制30ms子弹移动一步)
			if (stepIndex == 0) {
				Random random = new Random();
				int xSpeed;
				int ySpeed;
				int pOrN = 1;
				for (Bullet bullet : bullets) {
					//根据子弹的方向属性进行移动
					switch (bullet.getDirection()) {
					case Bullet.D_UP:
						xSpeed = (5 - random.nextInt(3)) * pOrN;
						ySpeed = 5;
						bullet.step(xSpeed, ySpeed);
						pOrN = -pOrN;
						break;
					case Bullet.D_RIGHT:
						xSpeed = -3;
						ySpeed = (5 - random.nextInt(3)) * pOrN;
						bullet.step(xSpeed, ySpeed);
						pOrN = -pOrN;
						break;
					case Bullet.D_BOTTOM:
						xSpeed = (5 - random.nextInt(3)) * pOrN;
						ySpeed = -5;
						bullet.step(xSpeed, ySpeed);
						pOrN = -pOrN;
						break;
					case Bullet.D_LEFT:
						xSpeed = 3;
						ySpeed = (5 - random.nextInt(3)) * pOrN;
						bullet.step(xSpeed, ySpeed);
						pOrN = -pOrN;
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 玩家控制移动(根据按键开关情况)
	 */
	public void playerStepAction() {
		if (state == RUNNING) {
			if (kUp && !kRight && !kDown && !kLeft && !player.isOutOfBoundUp()) {	//上
				player.stepY(-1);
			} else if (!kUp && kRight && !kDown && !kLeft && !player.isOutOfBoundRight()) {	//右
				player.stepX(1);
			} else if (!kUp && !kRight && kDown && !kLeft && !player.isOutOfBoundBottom()) {	//下
				player.stepY(1);
			} else if (!kUp && !kRight && !kDown && kLeft && !player.isOutOfBoundLeft()) {	//左
				player.stepX(-1);
			} else if (kUp && kRight && !kDown && !kLeft && !(player.isOutOfBoundRight() || player.isOutOfBoundUp())) {		//右上
				player.stepX(1);
				player.stepY(-1);
			} else if (kUp && !kRight && !kDown && kLeft && !(player.isOutOfBoundLeft() || player.isOutOfBoundUp())) {		//左上
				player.stepX(-1);
				player.stepY(-1);
			} else if (!kUp && kRight && kDown && !kLeft && !(player.isOutOfBoundRight() || player.isOutOfBoundBottom())) {		//右下
				player.stepX(1);
				player.stepY(1);
			} else if (!kUp && !kRight && kDown && kLeft && !(player.isOutOfBoundLeft() || player.isOutOfBoundBottom())) {		//左下
				player.stepX(-1);
				player.stepY(1);
			}
		}
	}
	
	/**
	 * 子弹越界,根据方向属性调整坐标重新上场
	 */
	public void bulletOutOfBoundAction() {
		if (state == RUNNING) {
			Random random = new Random();
			for (Bullet bullet : bullets) {
				if (bullet.isOutOfBound()) {
					switch (bullet.getDirection()) {
					case Bullet.D_UP:
						bullet.setX(random.nextInt(WIDTH-10));
						bullet.setY(0);
						break;
					case Bullet.D_RIGHT:
						bullet.setX(WIDTH-10);
						bullet.setY(random.nextInt(HEIGHT-10));
						break;
					case Bullet.D_BOTTOM:
						bullet.setX(random.nextInt(WIDTH-10));
						bullet.setY(HEIGHT-10);
						break;
					case Bullet.D_LEFT:
						bullet.setX(0);
						bullet.setY(random.nextInt(HEIGHT-10));
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 检查游戏是否结束(碰到子弹)
	 */
	public void checkGameOver() {
		for (Bullet bullet : bullets) {
			if (bullet.hit(player) && player.isAlive()) {
				player.goDead();
				state = GAME_OVER;
				endTime = System.currentTimeMillis();
				break;
			}
		}
	}
	
	public void action() {
		//键盘侦听
		KeyAdapter l = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				/*
				 * 上38
				 * 右39
				 * 下40
				 * 左37
				 */
				int pressed = e.getKeyCode();
				if (state == RUNNING) {
					switch (pressed) {
					case 38:
						kUp = true;
						break;
					case 39:
						kRight = true;
						break;
					case 40:
						kDown = true;
						break;
					case 37:
						kLeft = true;
						break;
					}
				}

				if (pressed == 10) {  //按下enter键(不局限于游戏状态是RUNNING,提到外面)
					if (state == START) {
						state = RUNNING;
						startTime = System.currentTimeMillis();
					} else if (state == GAME_OVER) {
						//游戏结束,清理现场准备下一次开始游戏
						kUp = false;
						kRight = false;
						kDown = false;
						kLeft = false;
						player = new Player();
						bullets = new Bullet[50];
						initBullet();
						state = RUNNING;
						startTime = System.currentTimeMillis();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (state == RUNNING) {
					int release = e.getKeyCode();
					switch (release) {
					case 38:
						kUp = false;
						break;
					case 39:
						kRight = false;
						break;
					case 40:
						kDown = false;
						break;
					case 37:
						kLeft = false;
						break;
					}
				}
			}
		};
		
		this.addKeyListener(l);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				bulletStepAction();
				bulletOutOfBoundAction();
				playerStepAction();
				checkGameOver();
				repaint();
			}
		}, 10, 10);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);  //不加没背景颜色
		setBackground(Color.BLACK);
		
		for (Bullet bullet : bullets) {
			if (bullet == null) {
				break;
			}
			bullet.paintObject(g);
		}
		
		player.paintObject(g);
		
		g.setFont(new Font(null, Font.BOLD, 30));
		g.setColor(Color.blue);
		switch (state) {
		case START:
			g.drawString("按ENTER开始游戏", 200, 450);
			break;
		case GAME_OVER:
			g.drawString("本次存活了"+ (endTime-startTime)/1000 +"秒~", 150, 400);
			g.drawString("按ENTER重新开始游戏", 200, 450);
			break;
		}
	}
	
	public static void main(String[] args) {
		World world = new World();
		JFrame frame = new JFrame("game");
		frame.setSize(WIDTH, HEIGHT);
		frame.add(world);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		world.initBullet();
		world.requestFocus();
		world.action();
	}
}
