package objects.attacks;

import java.awt.Graphics2D;

import objects.GameObject;

public abstract class Attack extends GameObject {
	
	// variable that tracks type of attack
	protected ATTACK_TYPE type;
	// variable that tracks duration of attack
	protected int time;
	// variable that tracks parent object
	protected GameObject parent;
	// variable that tracks damage of attack
	protected int damage;
	
	public Attack(GameObject parent, int damage, int x, int y, int xDir, int yDir, ATTACK_TYPE type, int size, int speed, int time) {
		super(4, x, y, size, size, 1, speed, 0);
		this.damage = damage;
		this.parent = parent;
		this.type = type;
		this.xDir = xDir;
		this.yDir = yDir;
		this.time = time;
	}

	@Override
	public abstract void objectTick();

	@Override
	public abstract void objectRender(Graphics2D g);
	
	public int getTime() {
		return time;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
}
