package objects.hostiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import objects.GameObject;
import objects.attacks.SimpleAttack;
import objects.items.ITEM_TYPES;
import objects.items.Weapon;
import states.Game;

public class SimpleEnemy extends Hostile {
	
	private int attackCooldown;
	
	public SimpleEnemy(int x, int y) {
		super(x, y, 20, 1, 100, 2, 10, 500);
		attackCooldown = 0;
		baseAttack = null;
	}

	@Override
	public void objectTick() {
		currItem = inventory.getItem(0);
		if(currItem != null && currItem.getType() == ITEM_TYPES.Weapon) {
			weapon = (Weapon)currItem;
		}
		if(weapon != null)
			weapon.objectTick();
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fill(bounds);
		g.setColor(c.darker());
		g.draw(bounds);
		if(weapon != null)
			weapon.objectRender(g);
		if(baseAttack != null) {
			baseAttack.objectRender(g);
		}
		
		if(Game.debugging) {
			g.setColor(Color.black);
			g.draw(roamBoundary);
		}
	}

	@Override
	public void attack(GameObject o) {
		attackCooldown--;
		if(attackCooldown <= 0) {
			Random r = new Random();
			// if holding a weapon, add attack to weapon
			if(inventory.getItem(0) != null && inventory.getItem(0).getType() == ITEM_TYPES.Weapon) {
				//weapon.getAttacks().add(new SimpleAttack(this, r.nextInt(minPower + 5, maxPower + 5), getX(), getY(), 0, 0, bounds.width, 0, 50));
				weapon.useItem(this);
			// if not holding weapon, do basic enemy damage
			} else {
				baseAttack = new SimpleAttack(this, r.nextInt(minPower, maxPower), getX(), getY(), 0, 0, bounds.width, 0, 50);
			}
			attackCooldown = 50;
		}
	}

}
