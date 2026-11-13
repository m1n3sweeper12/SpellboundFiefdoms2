package objects.hostiles;

import java.awt.Graphics2D;

import objects.GameObject;
import objects.attacks.SimpleAttack;
import objects.items.Weapon;

public class SimpleEnemy extends Hostile {
	
	private Weapon weapon;
	private int attackCooldown;
	
	public SimpleEnemy(int x, int y) {
		super(x, y, 20, 1, 100, 2, 10, 500);
		weapon = new Weapon(this, getCenterX(), getCenterY(), 1, false, "EnemyWeapon");
		inventory.addItem(weapon);
		attackCooldown = 0;
	}

	@Override
	public void objectTick() {
		currItem = inventory.getItem(0);
		weapon.objectTick();
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fill(bounds);
		g.setColor(c.darker());
		g.draw(bounds);
		weapon.objectRender(g);
	}

	@Override
	public void attack(GameObject o) {
		attackCooldown--;
		if(attackCooldown <= 0) {
			weapon.getAttacks().add(new SimpleAttack(this, getX(), getY(), 0, 0, bounds.width, 0, 50));
			attackCooldown = 50;
		}
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

}
