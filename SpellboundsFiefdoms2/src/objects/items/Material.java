package objects.items;

import java.awt.Graphics2D;

import objects.GameObject;
import utils.Colors;

public class Material extends Item {
	
	public Material(Item m) {
		super(m.getBounds().x, m.getBounds().y, ITEM_TYPES.Material, m.quantity, m.renderQuantity, m.name);
	}
	
	public Material(int x, int y, int quantity, boolean renderQuantity, String name) {
		super(x, y, ITEM_TYPES.Material, quantity, renderQuantity, name);
	}
	
	@Override
	public void objectTick() {
		
	}

	@Override
	public void objectRender(Graphics2D g) {
		g.setColor(Colors.yellow);
		g.fillOval(this.getX(), bounds.y, bounds.width, bounds.height);
		g.setColor(Colors.yellow.darker());
		g.drawOval(this.getX(), bounds.y, bounds.width, bounds.height);
		
		this.drawQuantity(g);
	}

	@Override
	public void useItem(GameObject parent) {
		
	}

}
