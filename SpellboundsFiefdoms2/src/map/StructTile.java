package map;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class StructTile extends Tile {
	
	int type;

	public StructTile(int type, int x, int y, int width, int height, boolean solid, BufferedImage img, Color c) {
		super(x, y, width, height, solid, img, c);
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

}
