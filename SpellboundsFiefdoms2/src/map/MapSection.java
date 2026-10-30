package map;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import utils.Colors;

public class MapSection {
	
	private ArrayList<Tile> tiles;
	private int x, y;
	private boolean locked;
	private Rectangle bounds;
	
	public MapSection() {
		tiles = new ArrayList<Tile>();
		x = 0;
		y = 0;
		//locked = false;
	}
	
	public void tick() {
		for(Tile t : tiles) {
			t.tick();
		}
	}
	
	public void render(Graphics2D g) {
		if(locked) {
			g.setColor(Colors.orange.darker());
			g.fillRect(x*Map.sectionWidth*Map.TILE_SIZE, y*Map.sectionHeight*Map.TILE_SIZE,
					Map.sectionWidth*Map.TILE_SIZE, Map.sectionHeight*Map.TILE_SIZE);
			g.setColor(Colors.orange.darker().darker());
			g.setStroke(new BasicStroke(5));
			g.drawRect(x*Map.sectionWidth*Map.TILE_SIZE, y*Map.sectionHeight*Map.TILE_SIZE,
					Map.sectionWidth*Map.TILE_SIZE, Map.sectionHeight*Map.TILE_SIZE);
			g.setStroke(new BasicStroke(1));
		} else {
			for(Tile t : tiles) {
				t.render(g);
			}
		}
	}
	
	/////////////////////////
	// GETTERS AND SETTERS //
	/////////////////////////
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
}
