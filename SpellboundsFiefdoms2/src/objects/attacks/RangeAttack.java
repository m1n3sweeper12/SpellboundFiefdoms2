package objects.attacks;

import java.awt.Graphics2D;

import objects.GameObject;

public class RangeAttack extends Attack {
	
	public RangeAttack(GameObject parent, int x, int y, int xDir, int yDir, int size, int speed, int time) {
		super(parent, x, y, xDir, yDir, ATTACK_TYPE.Range, size, speed, time);
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
