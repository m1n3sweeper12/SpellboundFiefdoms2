package map;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Door extends StructTile{

	public Door(int x, int y, int width, int height, boolean solid, BufferedImage img, Color c) {
		super(2, x, y, width, height, solid, img, c);
	}

}
