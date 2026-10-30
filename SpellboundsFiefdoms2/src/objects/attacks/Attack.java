package objects.attacks;

import java.awt.Graphics2D;

import objects.GameObject;

public abstract class Attack extends GameObject {
	
	protected ATTACK_TYPE type;
	// variable that tracks duration of attack
	protected int time;
	protected GameObject parent;
	
	public Attack(GameObject parent, int x, int y, int xDir, int yDir, ATTACK_TYPE type, int size, int speed, int time) {
		super(4, x, y, size, size, 1, speed, 0);
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
}
