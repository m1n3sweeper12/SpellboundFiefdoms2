package objects.items;

import java.awt.Graphics2D;

import objects.GameObject;

public class Armor extends Item {
	
	public Armor(Item a) {
		super(a.getBounds().x, a.getBounds().y, ITEM_TYPES.Armor, a.quantity, a.renderQuantity, a.name);
	}
	
	public Armor(int x, int y, int quantity, boolean renderQuantity, String name) {
		super(x, y, ITEM_TYPES.Armor, quantity, renderQuantity, name);
	}
	
	@Override
	public void objectTick() {
		
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fillRect(this.getX(), bounds.y, bounds.width, bounds.height/2);
		g.setColor(c.darker());
		g.drawRect(this.getX(), bounds.y, bounds.width, bounds.height/2);
		
		this.drawQuantity(g);
	}

	@Override
	public void useItem(GameObject parent) {
		
	}

}
