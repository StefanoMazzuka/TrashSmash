package res;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

import main.Main;
import main.Render;

/**
 * Class defines a bullet object
 * @author Ben Pinhorn
 *
 */
public class Bullet implements Drawable {
	private int x, y, typeCode, velocity;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	public final static int width = 10, height = 10;
	private BufferedImage image;
	public boolean isShip, isDead = false, isExplode = false;
	private ReentrantReadWriteLock lck;
	private long aniChange = 0, frameLength = 25;
	private int frame = 0;
	private BufferedImage[] animation;
	
	/**
	 * Constructor. Creates a bullet.
	 * @param x x-position
	 * @param y y-position
	 * @param type waste category
	 * @param isShip true if fired by player character
	 */
	public Bullet(int x, int y, int type, boolean isShip){
		setVelocity(6);
		setType(type);
		setX(x);
		setY(y);
		//Imports important graphics
		try {
			if(this.typeCode == GARBAGE) {
				this.image = ImageIO.read(getClass().getClassLoader().getResource("Other/redBullet.png"));
				this.animation = Render.redBulletExplosion;
			}
			else if(this.typeCode == RECYCLING) {
				this.image = ImageIO.read(getClass().getClassLoader().getResource("Other/blueBullet.png"));
				this.animation = Render.blueBulletExplosion;
			}
			else if(this.typeCode == COMPOST) {
				this.image = ImageIO.read(getClass().getClassLoader().getResource("Other/greenBullet.png"));
				this.animation = Render.greenBulletExplosion;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isShip = isShip;
	}
	
	/**
	 * Checks bullet collision with an enemy.
	 * @param enemy
	 * @return
	 */
	public boolean checkCollision(Enemy enemy) {
		if(this.x+this.getWidth()/2 >= enemy.getX() && this.x <= enemy.getX() + enemy.getWidth()/2) {
			if(this.y+this.getHeight()/2 >= enemy.getY() && this.y <= enemy.getY() + enemy.getHeight()/2) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks bullet collision with a ship.
	 * @param ship
	 * @return
	 */
	public boolean checkCollision(Ship ship) {
		if(this.x >= ship.getX() && this.x <= ship.getX() + Ship.getWidth()/2) {
			if(this.y >= ship.getY() && this.y <= ship.getY() + Ship.getHeight()/2) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public int getType(){ //Returns the type of bullet that this is (Recycling, Garbage, Compost)
		return this.typeCode;
	}
	
	public void setType(int typeCode){
		this.typeCode = typeCode;
	}
	
	public void setVelocity(int newV){
		velocity = newV;
	}
	
	public void move(){
		this.y -= velocity;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	public void setX(int newx){
		x = newx;
	}

	@Override
	public int getX() {
		return x;
	}

	public void setY(int newy){
		y = newy;
	}
	
	@Override
	public int getY() {
		return y;
	}

	public boolean isShip() {
		return isShip;
	}
	
	public boolean isDead(){
		return this.isDead;
	}
	
	private void setDead(boolean isDead){
		this.isDead = isDead;
	}

	public void explode() { //runs the explode animation for a bullet, see Render.java
		lck = Main.gMain.render.lck;
		if(!isExplode) {
			isExplode = true;
			this.image = animation[0];
		}
		
		lck.readLock().lock();
		if(aniChange == 0) {
			aniChange = System.currentTimeMillis();
		}
		
		if(System.currentTimeMillis() - aniChange > frameLength) {
			aniChange = System.currentTimeMillis();
			frame++;
			if(frame == 5) {
				this.setDead(true);
				lck.readLock().unlock();
				return;
			}
			this.image = animation[frame];	
		}
		lck.readLock().unlock();
	}
}
