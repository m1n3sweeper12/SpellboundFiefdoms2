package objects.passives;

import java.awt.Graphics2D;

import utils.Colors;

public class NPCTest extends Passive {
	
	public NPCTest(int x, int y) {
		super(x, y, 100, 1);
	}
	
	@Override
	public void objectTick() {
		currItem = inventory.getItem(0);
	}
	
	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(Colors.blue);
		g.fill(bounds);
		g.setColor(Colors.blue.darker());
		g.draw(bounds);
		
		// draw held item
		this.drawHeldItem(g);
	}

}
