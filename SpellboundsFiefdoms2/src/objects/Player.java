package objects;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import input.KeyHandler;
import map.Map;
import objects.items.Inventory;
import objects.items.Item;
import states.Game;

public class Player extends GameObject {
	
	private Inventory inventory;
	private Item currItem; // holds currently selected item
	private int currSlot; // tracks index of current item
	private boolean canMoveSlot, canUseItem; // tracks key press for moving current slot
	private int power, powerIncTimer; // controls how much a player can use an item
	
	public Player(int x, int y) {
		super(0, x, y, Map.TILE_SIZE, Map.TILE_SIZE, 200, 2, 200);
		inventory = new Inventory(10);
		currItem = null;
		currSlot = 0;
		canMoveSlot = true;
		canUseItem = true;
		power = 100;
		powerIncTimer = 10;
	}
	
	@Override
	public void objectTick() {
		// infinite stamina if debugging
		if(Game.debugging) runStamina = maxRunStamina;
		
		// regain power
		powerIncTimer--;
		if(powerIncTimer <= 0) {
			power++;
			powerIncTimer = 10;
		}
		if(power > 100) power = 100;
		if(power < 0) power = 0;
		
		// BUG: for some reason, when both W & D are pressed, Key Event for E is not registering
		if(KeyHandler.getKey(KeyEvent.VK_E)) { // use current item
			System.out.println("got here");
			if(canUseItem && power >= 10 && (xDir != 0 || yDir != 0)) {
				if(currItem != null) {
					currItem.useItem(this);
					power -= 10;
				}
				canUseItem = false;
			}
		} else {
			canUseItem = true;
		}
		
		if(KeyHandler.getKey(KeyEvent.VK_SHIFT)) { // player running
			isRunning = true;
		} else {
			isRunning = false;
		}
		
		if(KeyHandler.getKey(KeyEvent.VK_W)) { // player moves up
			setY((int)(this.bounds.y - this.currSpeed));
			yDir = -1;
		}
		if(KeyHandler.getKey(KeyEvent.VK_S)) { // player moves down
			setY((int)(this.bounds.y + this.currSpeed));
			yDir = 1;
		}
		// set y direction to 0 if not moving up or down
		if(!KeyHandler.getKey(KeyEvent.VK_W) && !KeyHandler.getKey(KeyEvent.VK_S)) {
			yDir = 0;
		}
		
		if(KeyHandler.getKey(KeyEvent.VK_A)) {// player moves left
			setX((int)(this.bounds.x - this.currSpeed));
			xDir = -1;
		}
		if(KeyHandler.getKey(KeyEvent.VK_D)) { // player moves right
			setX((int)(this.bounds.x + this.currSpeed));
			xDir = 1;
		}
		// set x direction to 0 if not moving left or right
		if(!KeyHandler.getKey(KeyEvent.VK_A) && !KeyHandler.getKey(KeyEvent.VK_D)) {
			xDir = 0;
		}
		
		if(KeyHandler.getKey(KeyEvent.VK_LEFT)) { // move current slot to the left
			if(canMoveSlot) {
				currSlot--;
				canMoveSlot = false;
				if(currSlot < 0) {
					currSlot = 9;
				}
			}
		} else if(KeyHandler.getKey(KeyEvent.VK_RIGHT)) { // move current slot to the right
			if(canMoveSlot) {
				currSlot++;
				canMoveSlot = false;
				if(currSlot > 9) {
					currSlot = 0;
				}
			}
		} else {
			canMoveSlot = true;
		}
		currItem = inventory.getItem(currSlot);
		if(currItem != null) { // tick current item
			currItem.objectTick();
		}
	}
	
	@Override
	public void objectRender(Graphics2D g) {
		// render player
		g.setColor(c);
		g.fill(bounds);
		g.setColor(c.darker());
		g.draw(bounds);
		// draw currently held item
		this.drawHeldItem(g);
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public int getCurrSlot() {
		return currSlot;
	}
	
	public Item getCurrItem() {
		return currItem;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}

}
