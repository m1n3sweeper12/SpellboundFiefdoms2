package objects.hostiles;

import java.awt.Color;
import java.awt.Graphics2D;

import objects.GameObject;
import states.Game;
import utils.Colors;

public class EnemyTest extends Hostile {

	public EnemyTest(int x, int y) {
		super(x, y, 10, 1, 100, 100, 2, 100);
	}

	@Override
	public void objectTick() {
		currItem = inventory.getItem(0);
	}

	@Override
	public void objectRender(Graphics2D g) {
		// render enemy
		g.setColor(Colors.red);
		g.fill(bounds);
		g.setColor(Colors.red.darker());
		g.draw(bounds);
		
		this.drawHeldItem(g);
		
		// debugging
		if(Game.debugging) {
			g.setColor(Color.black);
			g.draw(roamBoundary);
			
			g.setColor(Color.white);
			g.drawOval(nextX, nextY, 2, 2);
		}
	}

	@Override
	public void attack(GameObject o) {
		
	}
	
}
