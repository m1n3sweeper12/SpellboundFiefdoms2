package objects.attacks;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import objects.GameObject;
import utils.Colors;

public class CircleAttack extends Attack {
	
	protected int stroke;
	
	public CircleAttack(GameObject parent, int x, int y, int xDir, int yDir, int size, int stroke, int speed, int time) {
		super(parent, 10, x, y, xDir, yDir, ATTACK_TYPE.Circle, size, speed, time);
		this.stroke = stroke;
	}

	@Override
	public void objectTick() {
		
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(Colors.purple);
		g.setStroke(new BasicStroke(stroke));
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
