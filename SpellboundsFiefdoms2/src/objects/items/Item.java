package objects.items;

import java.awt.Font;
import java.awt.Graphics2D;

import objects.GameObject;
import objects.ObjectHandler;
import utils.Colors;

public abstract class Item extends GameObject {
	
	protected int quantity;
	protected boolean renderQuantity;
	protected String name;
	protected boolean pickedUp;
	protected ITEM_TYPES type;
	protected GameObject parent;
	
	public Item(Item item) {
		super(3, item.bounds.x, item.bounds.y, ObjectHandler.ITEM_SIZE, ObjectHandler.ITEM_SIZE, 0, 0, 0);
		this.parent = item.parent;
		this.type = item.type;
		this.quantity = item.quantity;
		this.renderQuantity = item.renderQuantity;
		this.name = item.name;
		pickedUp = false;
	}
	
	public Item(GameObject parent, int x, int y, ITEM_TYPES type, int quantity, boolean renderQuantity, String name) {
		super(3, x, y, ObjectHandler.ITEM_SIZE, ObjectHandler.ITEM_SIZE, 0, 0, 0);
		this.parent = parent;
		this.type = type;
		this.quantity = quantity;
		this.renderQuantity = renderQuantity;
		this.name = name;
		pickedUp = false;
	}
	
	protected void drawQuantity(Graphics2D g) {
		if(renderQuantity && quantity > 1) {
			g.setColor(Colors.black);
			g.setFont(new Font("Monospaced", Font.BOLD, 14));
			g.drawString(quantity + "", bounds.x + bounds.width, bounds.y);
		}
	}
	
	@Override
	public abstract void objectRender(Graphics2D g);
	public abstract void objectTick();
	public abstract void useItem(GameObject parent);
	
	///////////////////////
	// GETTERS & SETTERS //
	///////////////////////
	
	public ITEM_TYPES getType() {
		return type;
	}
	
	public void setType(ITEM_TYPES type) {
		this.type = type;
	}
	
	public boolean isPickedUp() {
		return pickedUp;
	}
	
	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public GameObject getParent() {
		return parent;
	}
	
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
}