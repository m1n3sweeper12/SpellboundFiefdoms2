package objects.passives;

import java.awt.Graphics2D;

import objects.GameObject;
import objects.ObjectHandler;
import objects.items.Inventory;
import objects.items.Item;
import utils.Colors;

public abstract class Passive extends GameObject {
	
	protected Inventory inventory;
	protected Item currItem;
	
	public Passive(int x, int y, int hp, int speed) {
		super(2, x, y, ObjectHandler.OBJ_SIZE, ObjectHandler.OBJ_SIZE, hp, speed, 100);
		inventory = new Inventory(5);
		currItem = null;
	}

	@Override
	public abstract void objectTick();
	
	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(Colors.blue);
		g.fill(bounds);
		g.setColor(Colors.blue.darker());
		g.draw(bounds);
	}

}