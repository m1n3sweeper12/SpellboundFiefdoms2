package objects.hostiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import objects.GameObject;
import objects.ObjectHandler;
import objects.items.Inventory;
import objects.items.Item;

public abstract class Hostile extends GameObject {
	
	protected Inventory inventory;
	protected Item currItem;
	protected Rectangle roamBoundary;
	private int roamTimer;
	protected int nextX, nextY;
	// variable to track hostile state
	// "roam", "chase", "die"
	protected String state;
	// variables to track min/max damage of attacks
	protected int minPower, maxPower;
	
	public Hostile(int x, int y, int hp, int speed, int roamSize, int minPower, int maxPower, int attackTime) {
		super(1, x, y, ObjectHandler.OBJ_SIZE, ObjectHandler.OBJ_SIZE, hp, speed, 100);
		// Hostile objects can only hold 1 item at a time
		// this will typically be a weapon,
		// but random enemy generation could change the item that is held
		inventory = new Inventory(1);
		currItem = null;
		roamBoundary = new Rectangle(getCenterX() - roamSize, getCenterY() - roamSize, roamSize*2, roamSize*2);
		roamTimer = 0;
		state = "roam";
		this.minPower = minPower;
		this.maxPower = maxPower;
	}
	
	public void chase(GameObject o) {
		
		// set next x to target object's x position
		nextX = o.getCenterX();
		// set next y to target object's y position
		nextY = o.getCenterY();
		
		move(nextX, nextY);
	}
	
	public void roam() {
		// decrement roam timer
		roamTimer--;
		
		// once roam timer runs out, generate random
		// point to move to within roam boundary
		if(roamTimer <= 0) {
			Random r = new Random();
			
			// set minimum x and y distance to travel
			int minXDist = roamBoundary.width/4;
			int minYDist = roamBoundary.height/4;
			
			nextX = r.nextInt(roamBoundary.x, roamBoundary.x + roamBoundary.width);
			nextY = r.nextInt(roamBoundary.y, roamBoundary.y + roamBoundary.height);
			
			// generate random x position until nextX is >= minXDist
			while(Math.abs(nextX - getCenterX()) < minXDist)
				nextX = r.nextInt(roamBoundary.x, roamBoundary.x + roamBoundary.width);
			
			// generate random y position until nextY is >= minYDist
			while(Math.abs(nextY - getCenterY()) < minYDist)
				nextY = r.nextInt(roamBoundary.y, roamBoundary.y + roamBoundary.height);
			
			// reset roam timer
			roamTimer = r.nextInt(400, 401);
		}
		
		move(nextX, nextY);
	}
	
	public void move(int x, int y) {
		// set roamBoundary Rectangle to center around object
		roamBoundary.x = getCenterX() - roamBoundary.width/2;
		roamBoundary.y = getCenterY() - roamBoundary.height/2;
		
		// check if next x is less than current x
		if(x < this.getCenterX()) {
			// set x direction to left
			xDir = -1;
		// check if next x is greater than current x
		} else if(x > this.getCenterX()) {
			// set x direction to right
			xDir = 1;
		// if current x equals next x, set x direction to 0
		} else {
			xDir = 0;
		}
		
		// check if next y is less than current y
		if(y < this.getCenterY()) {
			// set y direction to up
			yDir = -1;
		// check if next y is greater than current y
		} else if(y > this.getCenterY()) {
			// set y direction to down
			yDir = 1;
		// if current y equals next y, set y direction to 0
		} else {
			yDir = 0;
		}
		
		// set x and y to current position + direction*speed
		this.setX(getX() + (int)(xDir*currSpeed));
		this.setY(getY() + (int)(yDir*currSpeed));
		currItem.setX(getCenterX());
		currItem.setY(getCenterY());
	}

	@Override
	public abstract void objectTick();
	
	@Override
	public abstract void objectRender(Graphics2D g);
	
	public abstract void attack(GameObject o);
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Rectangle getRoamBoundary() {
		return roamBoundary;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
}