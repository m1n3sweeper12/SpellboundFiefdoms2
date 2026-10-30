package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import objects.items.Item;
import states.Game;
import utils.Colors;

public abstract class GameObject {
	
	// tracks type of game object
	// 0 -> player
	// 1 -> hostile
	// 2 -> passive
	// 3 -> item
	// 4 -> attack
	protected int id;
	
	// tracks object's health
	protected int hp;
	
	// tracks the direction of movement
	// will be very useful when sprites/animations are added
	protected int xDir, yDir;
	// tracks object's speed
	protected double speed, currSpeed;
	// tracks if object is running
	protected boolean isRunning, canRun;
	// tracks run stamina
	protected int runStamina, maxRunStamina;
	
	// color to show on the mini map
	protected Color c;
	
	// collision bounds,
	// basic bounds used for object collisions
	// edge bounds used for tile collisions
	protected Rectangle bounds;
	protected Rectangle topBounds, bottomBounds, leftBounds, rightBounds;
	
	// usable item held by object
	protected Item heldItem;
	
	// used for animations, tracks how long hurt animation/effect should run
	private int hurtTimer;
	
	public GameObject(int id, int x, int y, int width, int height, int hp, int speed, int runStamina) {
		this.id = id;
		switch(id) {
		case 0: // player
			c = Colors.purple;
			break;
		case 1: // hostile
			c = Colors.red;
			break;
		case 2: // passive
			c = Colors.blue;
			break;
		case 3: // item
			c = Colors.yellow;
			break;
		default:
			c = Colors.white;
		}
		// set bounds rectangles
		this.bounds = new Rectangle(x, y, width, height);
		this.topBounds = new Rectangle(x + 5, y, width - 10, 5);
		this.bottomBounds = new Rectangle(x + 5, y + height - 5, width - 10, 5);
		this.leftBounds = new Rectangle(x, y + 5, 5, height - 10);
		this.rightBounds = new Rectangle(x + width - 5, y + 5, 5, height - 10);
		
		// set starting values for variables
		this.hp = hp;
		this.speed = speed;
		isRunning = false;
		this.runStamina = runStamina;
		this.maxRunStamina = runStamina;
		xDir = 0;
		yDir = 0;
	}
	
	public void hurt(int damage) {
		hp -= damage;
		hurtTimer = 200;
	}
	
	public void tick() {
		// stop running if stamina reaches 0
		if(runStamina < 0) {
			runStamina = 0;
			canRun = false;
		}
		// stop incrementing stamina if stamina reaches max
		if(runStamina >= maxRunStamina) {
			runStamina = maxRunStamina;
		}
		// set speed to running speed if running
		if(isRunning && canRun) {
			currSpeed = speed*2;
			runStamina -= 2;
		// set speed to walking speed if not running
		} else {
			currSpeed = speed;
			runStamina++;
			// once stamina reaches 0,
			// it must reach halfway before object can run again
			if(runStamina > maxRunStamina/2) {
				canRun = true;
			}
		}
		
		if(heldItem != null) {
			heldItem.setX(getCenterX());
			heldItem.setY(getCenterY());
		}
	}
	
	public void render(Graphics2D g) {
		
		// TEMP
		// draws oval over object when hurt
		if(hurtTimer > 0) {
			hurtTimer--;
			g.setColor(Colors.red);
			g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
		} else {
			hurtTimer = 0;
		}
		
		// draw all bounds if debugging
		if(Game.debugging) {
			g.setColor(Color.red);
			g.draw(bottomBounds);
			g.draw(topBounds);
			g.draw(leftBounds);
			g.draw(rightBounds);
		}
		
		if(heldItem != null)
			heldItem.objectRender(g);
	}
	
	public abstract void objectTick();
	public abstract void objectRender(Graphics2D g);
	
	protected void drawHeldItem(Graphics2D g) {
		// draw currently held item
		if(heldItem != null) {
			heldItem.objectRender(g);
		}
	}
	
	///////////////////////
	// GETTERS & SETTERS //
	///////////////////////
	
	public Item getHeldItem() {
		return heldItem;
	}
	
	public void setHeldItem(Item heldItem) {
		this.heldItem = heldItem;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getRunStamina() {
		return runStamina;
	}
	
	public void setRunStamina(int runStamina) {
		this.runStamina = runStamina;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int hp) {
		this.hp = hp;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Rectangle getTopBounds() {
		return topBounds;
	}
	
	public Rectangle getBottomBounds() {
		return bottomBounds;
	}
	
	public Rectangle getLeftBounds() {
		return leftBounds;
	}
	
	public Rectangle getRightBounds() {
		return rightBounds;
	}
	
	public int getX() {
		return bounds.x;
	}
	
	public int getCenterX() {
		return bounds.x + bounds.width/2;
	}
	
	public void setX(int x) { // sets x for all bound boxes
		this.bounds.x = x;
		this.topBounds.x = x + 5;
		this.bottomBounds.x = x + 5;
		this.leftBounds.x = x;
		this.rightBounds.x = x + bounds.width - 5;
	}
	
	public int getXDir() {
		return xDir;
	}
	
	public void setXDir(int xDir) {
		this.xDir = xDir;
	}
	
	public int getY() {
		return bounds.y;
	}
	
	public int getCenterY() {
		return bounds.y + bounds.height/2;
	}
	
	public void setY(int y) { // sets y for all bound boxes
		this.bounds.y = y;
		this.topBounds.y = y;
		this.bottomBounds.y = y + bounds.height - 5;
		this.leftBounds.y = y + 5;
		this.rightBounds.y = y + 5;
	}
	
	public int getYDir() {
		return yDir;
	}
	
	public void setYDir(int yDir) {
		this.yDir = yDir;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
}
