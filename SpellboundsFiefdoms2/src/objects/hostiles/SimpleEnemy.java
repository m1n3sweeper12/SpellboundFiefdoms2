package objects.hostiles;

import java.awt.Graphics2D;

import objects.GameObject;
import objects.attacks.Attack;
import objects.attacks.SimpleAttack;

public class SimpleEnemy extends Hostile {
	
	private Attack attack;
	
	public SimpleEnemy(int x, int y) {
		super(x, y, 20, 1, 100, 2, 10, 500);
		attack = null;
	}

	@Override
	public void objectTick() {
		currItem = inventory.getItem(0);
		if(attack != null) {
			attack.tick();
		}
		if(attack != null && attack.getTime() == 0) {
			attack = null;
		}
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fill(bounds);
		g.setColor(c.darker());
		g.draw(bounds);
		
		if(attack != null) {
			attack.render(g);
		}
	}

	@Override
	public void attack(GameObject o) {
		if(attack == null) {
			int attackXDir, attackYDir;
			// set x direction towards target object
			if(getCenterX() > o.getCenterX()) attackXDir = -1;
			else if(getCenterX() < o.getCenterX()) attackXDir = 1;
			else attackXDir = 0;
			// set y direction towards target object
			if(getCenterY() > o.getCenterY()) attackYDir = -1;
			else if(getCenterY() < o.getCenterY()) attackYDir = 1;
			else attackYDir = 0;
			
			attack = new SimpleAttack(this, getCenterX(), getCenterY(), attackXDir, attackYDir, bounds.width/2, 1, 10);
		}
	}

}
