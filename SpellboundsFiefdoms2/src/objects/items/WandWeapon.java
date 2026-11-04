package objects.items;

import java.awt.Graphics2D;

import objects.GameObject;
import objects.attacks.Attack;
import objects.attacks.SimpleAttack;

public class WandWeapon extends Weapon {
	
	public WandWeapon(GameObject parent, int x, int y, boolean renderQuantity) {
		super(parent, x, y, 1, renderQuantity, "Wand");
	}
	
	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fillRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		g.setColor(c.darker());
		g.drawRect(bounds.x, bounds.y + bounds.height/4, bounds.width, bounds.height/4);
		
		this.drawQuantity(g);
		
		for(Attack a : attacks) {
			a.objectRender(g);
		}
	}
	
	@Override
	public void useItem(GameObject parent) {
		attacks.add(new SimpleAttack(this, getCenterX(), getCenterY(), parent.getXDir(), parent.getYDir(), 10, 4, 50));
	}

}
