package objects;

import map.Map;

public class Camera {
	
	private float x, y;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject player, int screenWidth, int screenHeight) {
		x = player.getCenterX() - screenWidth/2;
		y = player.getCenterY() - screenHeight/2;
		
		
		if(x < 0) {
			x = 0;
		}
		
		if(x > (Map.mapWidth*Map.sectionWidth*Map.TILE_SIZE - screenWidth)) {
			x = Map.mapWidth*Map.sectionWidth*Map.TILE_SIZE - screenWidth;
		}
		
		if(y < 0) {
			y = 0;
		}

		if(y > (Map.mapHeight*Map.sectionHeight*Map.TILE_SIZE - screenHeight)) {
			y = Map.mapHeight*Map.sectionHeight*Map.TILE_SIZE - screenHeight;
		}
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
}
