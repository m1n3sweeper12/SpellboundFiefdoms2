package map;

import java.awt.Graphics2D;

public class MapHandler {
	
	private Map test;
	private Map currMap;
	
	public MapHandler() {
		// TEMP, initialize test map
		test = new Map();
		test.loadMap("res/maps/test-map-tiles2.txt");
		
		currMap = test;
	}
	
	public void tick() {
		currMap.tick();
	}
	
	public void render(Graphics2D g) {
		currMap.render(g);
	}
	
	public Map getCurrMap() {
		return currMap;
	}
	
}
