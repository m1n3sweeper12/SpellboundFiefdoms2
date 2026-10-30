package objects.items;

import java.awt.Graphics2D;
import java.util.ArrayList;

import objects.GameObject;
import objects.attacks.Attack;

public class Weapon extends Item {
	
	protected ArrayList<Attack> attacks;
	
	public Weapon(Item w) {
		super(w.getBounds().x, w.getBounds().y, ITEM_TYPES.Weapon, w.quantity, w.renderQuantity, w.name);
	}
	
	public Weapon(int x, int y, int quantity, boolean renderQuantity, String name) {
		super(x, y, ITEM_TYPES.Weapon, quantity, renderQuantity, name);
		// TODO: implement custom attack numbers
		attacks = new ArrayList<Attack>(10);
	}
	
	@Override
	public void objectTick() {
		for(Attack a : attacks) {
			if(a != null) {
				a.objectTick();
				
				if(a.getTime() <= 0)
					a = null;
			}
		}
	}
	
	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fillRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		g.setColor(c.darker());
		g.drawRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		
		this.drawQuantity(g);
	}

	@Override
	public void useItem(GameObject parent) {
		
	}
	
	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

}
