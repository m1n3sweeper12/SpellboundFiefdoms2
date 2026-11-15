package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import objects.ObjectHandler;
import objects.hostiles.Hostile;
import objects.hostiles.SimpleEnemy;
import states.Game;
import utils.Colors;

public class MapSection {
	
	private ArrayList<Tile> tiles;
	private int x, y;
	private boolean locked;
	private Rectangle bounds, spawnBounds;
	// variable to track type of section
	// 0 -> default, nothing
	// 1 -> enemy spawning area
	// 2 -> market
	private int id;
	private boolean active = false;
	
	private Random r;
	private int spawnCooldown;
	
	public MapSection(int id) {
		tiles = new ArrayList<Tile>();
		this.id = id;
		r = new Random();
		if(id == 1) {
			spawnCooldown = r.nextInt(250, 500);
		}
		x = 0;
		y = 0;
	}
	
	public void tick() {
		for(Tile t : tiles) {
			t.tick();
		}
		
		if(active) {
			switch(id) {
			case 0:
				break;
			case 1:
				spawnEnemies();
				break;
			default:
				break;
			}
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
		// if debugging, draw section border
		// with different color depending on id
		if(Game.debugging) {
			switch(id) {
			case 0:
				g.setColor(Color.black); // default
				break;
			case 1:
				g.setColor(Color.red); // enemy spawning
				break;
			case 2:
				g.setColor(Color.blue); // market
				break;
			}
			g.setStroke(new BasicStroke(4));
			g.draw(bounds);
			g.setStroke(new BasicStroke(1));
			
			
		}
	}
	
	private void spawnEnemies() {
		spawnCooldown--;
		if(spawnCooldown <= 0) {
			spawnEnemy();
			spawnCooldown = r.nextInt(250, 500);
		}
	}
	
	// method to spawn enemies in enemy spawning sections
	private void spawnEnemy() {
		Hostile e;
		
		// random integer to determine type of enemy spawned
		int spawnType = r.nextInt(1);
		
		// generate random spawn x and y
		int spawnX = r.nextInt(spawnBounds.x, spawnBounds.x + spawnBounds.width);
		int spawnY = r.nextInt(spawnBounds.y, spawnBounds.y + spawnBounds.height);
		
		switch(spawnType) {
		case 0: // most common enemy, spawned at random location in section
			e = new SimpleEnemy(spawnX, spawnY);
		default: // spawn basic enemy if random integer is outside of range
			e = new SimpleEnemy(spawnX, spawnY);
		}
		
		// add new enemy to object handler arrays
		ObjectHandler.addEnemy(e);
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
	
	public Rectangle getSpawnBounds() {
		return spawnBounds;
	}
	
	public void setSpawnBounds(Rectangle spawnBounds) {
		this.spawnBounds = spawnBounds;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
