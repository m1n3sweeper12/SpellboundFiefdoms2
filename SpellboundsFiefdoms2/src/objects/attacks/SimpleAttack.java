package objects.attacks;

import java.awt.Graphics2D;

import objects.GameObject;

public class SimpleAttack extends Attack {

	public SimpleAttack(GameObject parent, int x, int y, int xDir, int yDir, int size, int speed, int time) {
		super(parent, 10, x, y, xDir, yDir, ATTACK_TYPE.Simple, size, speed, time);
	}

	@Override
	public void objectTick() {
		time--;
		setX(getX() + xDir*(int)speed);
		setY(getY() + yDir*(int)speed);
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(c);
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
}
