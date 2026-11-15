package map;

import java.awt.Graphics2D;

public abstract class Structure {
	
	// variable to represent the type of structure
	// 0 -> house
	// 1 -> shop
	// 2 -> park (?)
	protected int type;
	protected StructTile[] exterior;
	protected int x, y, extWidth, extHeight;
	protected Map interior;
	
	public Structure(int type, int x, int y, int extWidth, int extHeight, String intPath) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.extWidth = extWidth;
		this.extHeight = extHeight;
		exterior = new StructTile[extWidth*extHeight];
		interior = new Map();
		interior.loadMap(intPath);
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g);
	
	public void setTile(int x, int y, StructTile t) {
		exterior[y*extWidth+x] = t;
	}
	
	public StructTile[] getExterior() {
		return exterior;
	}
	
}
