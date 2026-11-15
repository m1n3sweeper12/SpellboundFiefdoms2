package map;

import java.awt.Graphics2D;

public class House extends Structure {

	public House(int x, int y, int width, int height, String intPath) {
		super(0, x, y, width, height, intPath);
	}

	@Override
	public void tick() {
		for(StructTile t : exterior) {
			if(t != null) {
				t.tick();
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		for(StructTile t : exterior) {
			if(t != null) {
				t.render(g);
			}
		}
	}

}
