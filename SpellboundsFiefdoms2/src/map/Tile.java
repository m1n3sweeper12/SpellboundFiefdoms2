package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {
	
	// rectangle to track collisions
	private Rectangle bounds;
	// boolean for if tile is solid
	private boolean solid;
	// image to draw for tile
	private BufferedImage img;
	// base color for if image is null
	private Color c;
	
	// constructor, creates bounds rectangle and initializes variables
	public Tile(int x, int y, int width, int height, boolean solid, BufferedImage img, Color c) {
		this.bounds = new Rectangle(x, y, width, height);
		this.solid = solid;
		this.img = img;
		this.c = c;
	}
	
	public void tick() {
		
	}
	
	
	public void render(Graphics2D g) {
		// if image isn't null, draw image, otherwise draw base color
		if(img != null) {
			g.drawImage(img, bounds.x, bounds.y, null);
		} else {
			g.setColor(c);
			g.fill(bounds);
			g.setColor(c.darker());
			g.draw(bounds);
		}
	}
	
	///////////////////////
	// GETTERS & SETTERS //
	///////////////////////
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public Color getColor() {
		return c;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
}
