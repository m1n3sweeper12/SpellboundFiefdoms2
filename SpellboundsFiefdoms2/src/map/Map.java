package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import objects.GameObject;
import objects.ObjectHandler;
import utils.Colors;

public class Map {
	
	private ArrayList<MapSection> sections;
	public static int mapWidth, mapHeight;
	public static int sectionWidth, sectionHeight;
	public static final int TILE_SIZE = 48;
	
	public Map() {
		sections = new ArrayList<MapSection>();
	}
	
	public void tick() {
		// TODO: optimize map section updating
		for(MapSection s : sections) {
			s.tick();
		}
	}
	
	public void render(Graphics2D g) {
		// TODO: optimize map section rendering
		for(MapSection s : sections) {
			s.render(g);
		}
	}
	
	public void renderMiniMap(Graphics2D g, ObjectHandler oh) {
		// top-left corner of mini map
		int miniStartX = 50;
		int miniStartY = 50;
		int scale = 3;
		// draw background
		g.setColor(Colors.black);
		g.fillRect(miniStartX - scale, miniStartY - scale,
				mapWidth*sectionWidth*scale + scale*2, mapHeight*sectionHeight*scale + scale*2);
		// draw tiles if section unlocked
		for(MapSection s : sections) {
			// draw rectangle over section if locked
			if(s.isLocked()) {
				g.setColor(Colors.orange.darker());
				g.fillRect(miniStartX + s.getX()*sectionWidth*scale, miniStartY + s.getY()*sectionHeight*scale,
						Map.sectionWidth*scale, Map.sectionHeight*scale);
				g.setColor(Colors.orange.darker().darker());
				g.drawRect(miniStartX + s.getX()*sectionWidth*scale, miniStartY + s.getY()*sectionHeight*scale,
						Map.sectionWidth*scale, Map.sectionHeight*scale);
			} else { // draw tiles if not locked
				for(Tile t : s.getTiles()) {
					g.setColor(t.getColor());
					int x = t.getBounds().x/Map.TILE_SIZE;
					int y = t.getBounds().y/Map.TILE_SIZE;
					g.fillRect(miniStartX + x*scale, miniStartY + y*scale, scale, scale);
				}
			}
		}
		
		// draw objects
		for(GameObject obj : oh.getObjects()) {
			g.setColor(obj.getColor());
			g.fillOval(miniStartX + obj.getCenterX()/Map.TILE_SIZE*scale,
					miniStartY + obj.getCenterY()/Map.TILE_SIZE*scale, scale*2, scale*2);
		}
	}
	
	// method to load map from file
	public void loadMap(String path) {
		// load data from map file into string
		Scanner in;
		try {
			in = new Scanner(new File(path));
			// get map width and height (in map sections)
			mapWidth = in.nextInt();
			mapHeight = in.nextInt();
			
			// determine number of locks for map sections
			int numSections = mapWidth*mapHeight;
			boolean[] locks = new boolean[numSections];
			
			// get locks for each section
			for(int i = 0; i < locks.length; i++) {
				int lock = in.nextInt();
				if(lock == 0) {
					locks[i] = false;
				} else {
					locks[i] = true;
				}
			}
			
			// get section width and height (in tiles)
			sectionWidth = in.nextInt();
			sectionHeight = in.nextInt();
			
			// fill sections
			// index variable to track locked sections
			int index = 0;
			
			// loop through every section in map
			for(int y = 0; y < mapHeight; y++) {
				for(int x = 0; x < mapWidth; x++) {
					MapSection temp = new MapSection();
					// set values for position
					temp.setX(x);
					temp.setY(y);
					// set locked section
					temp.setLocked(locks[index]);
					temp.setBounds(new Rectangle(x*Map.sectionWidth*Map.TILE_SIZE,
							y*Map.sectionHeight*Map.TILE_SIZE, sectionWidth*TILE_SIZE, sectionHeight*TILE_SIZE));
					// loop through every tile in section array
					for(int yy = 0; yy < sectionHeight; yy++) {
						for(int xx = 0; xx < sectionWidth; xx++) {
							// get tile id
							int tileID = in.nextInt();
							// get tile position
							int tileX = x*sectionWidth + xx;
							int tileY = y*sectionHeight + yy;
							// depending on tile ID, initialize proper tile
							switch(tileID) {
							case 0: // TEMP, not solid
								temp.getTiles().add(new Tile(tileX*TILE_SIZE, tileY*TILE_SIZE, TILE_SIZE, TILE_SIZE,
										false, null, Colors.green));
								break;
							case 1: // TEMP, solid
								temp.getTiles().add(new Tile(tileX*TILE_SIZE, tileY*TILE_SIZE, TILE_SIZE, TILE_SIZE,
										true, null, Colors.blue));
								break;
							}
						}
					}
					// increment lock index
					index++;
					// add section to array list
					sections.add(temp);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<MapSection> getSections() {
		return sections;
	}
	
}
