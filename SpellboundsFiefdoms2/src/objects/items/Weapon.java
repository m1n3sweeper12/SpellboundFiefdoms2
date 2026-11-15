package objects.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import objects.GameObject;
import objects.attacks.Attack;
import objects.attacks.SimpleAttack;

public class Weapon extends Item {
	
	protected ArrayList<Attack> attacks;
	
	public Weapon(Item w) {
		super(w.getParent(), w.getBounds().x, w.getBounds().y, ITEM_TYPES.Weapon, w.quantity, w.renderQuantity, w.name);
	}
	
	public Weapon(GameObject parent, int x, int y, int quantity, boolean renderQuantity, String name) {
		super(parent, x, y, ITEM_TYPES.Weapon, quantity, renderQuantity, name);
		// TODO: implement custom attack numbers
		attacks = new ArrayList<Attack>(10);
	}
	
	@Override
	public void objectTick() {
		for(Attack a : attacks) {
			if(a != null) {
				a.objectTick();
			}
		}
		
		attacks.removeIf(a -> a.getTime() <= 0);
	}
	
	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fillRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		g.setColor(c.darker());
		g.drawRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		
		if(attacks != null) {
			for(Attack a : attacks) {
				if(a != null) {
					a.objectRender(g);
				}
			}
		}
		
		this.drawQuantity(g);
	}

	@Override
	public void useItem(GameObject parent) {
		Random r = new Random();
		attacks.add(new SimpleAttack(this, r.nextInt(2, 10), getX(), getY(), 0, 0, bounds.width, 0, 50));
	}
	
	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

}
