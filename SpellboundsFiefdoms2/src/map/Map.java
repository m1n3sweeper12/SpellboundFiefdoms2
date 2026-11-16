package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import objects.GameObject;
import objects.ObjectHandler;
import utils.Colors;

public class Map {
	
	private ArrayList<MapSection> sections;
	private ArrayList<Structure> structures;
	public static int mapWidth, mapHeight;
	public static int sectionWidth, sectionHeight;
	public static final int TILE_SIZE = 48;
	public static Rectangle mapBounds;
	
	public Map() {
		sections = new ArrayList<MapSection>();
		structures = new ArrayList<Structure>();
	}
	
	public void tick() {
		// TODO: optimize map section updating
		// tick map sections
		for(MapSection s : sections) {
			s.tick();
		}
		// tick structures
		for(Structure s : structures) {
			s.tick();
		}
	}
	
	public void render(Graphics2D g) {
		// TODO: optimize map section rendering
		// render map sections
		for(MapSection s : sections) {
			s.render(g);
		}
		// render structures
		for(Structure s : structures) {
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
	public void loadMap(String name) {
		String tilePath = "res/maps/" + name + "/tiles.txt";
		String structPath = "res/maps/" + name + "/structs.txt";
		
		loadTiles(tilePath);
		loadStructures(structPath);
		
		mapBounds = new Rectangle(0, 0, mapWidth*sectionWidth*TILE_SIZE, mapHeight*sectionHeight*TILE_SIZE);
	}
	
	private void loadTiles(String tilePath) {
		// load data from map file
		Scanner in;
		try {
			in = new Scanner(new File(tilePath));
			// get map width and height (in map sections)
			mapWidth = in.nextInt();
			mapHeight = in.nextInt();
			
			// determine number of locks for map sections
			int numSections = mapWidth*mapHeight;
			boolean[] locks = new boolean[numSections];
			int[] ids = new int[numSections];
			
			// get locks for each section
			for(int i = 0; i < locks.length; i++) {
				int lock = in.nextInt();
				if(lock == 0) {
					locks[i] = false;
				} else {
					locks[i] = true;
				}
			}
			
			// get ids for each section
			for(int i = 0; i < ids.length; i++) {
				int id = in.nextInt();
				ids[i] = id;
			}
			
			// get section width and height (in tiles)
			sectionWidth = in.nextInt();
			sectionHeight = in.nextInt();
			
			// loop through map and instantiate each map section
			for(int y = 0; y < mapHeight; y++) {
				for(int x = 0; x < mapWidth; x++) {
					int index = y*mapWidth + x;
					MapSection section = new MapSection(ids[index]);
					section.setX(x);
					section.setY(y);
					section.setLocked(locks[index]);
					section.setBounds(new Rectangle(x*Map.sectionWidth*Map.TILE_SIZE,
							y*Map.sectionHeight*Map.TILE_SIZE, sectionWidth*TILE_SIZE, sectionHeight*TILE_SIZE));
					section.setSpawnBounds(new Rectangle(x*Map.sectionWidth*Map.TILE_SIZE + 10,
							y*Map.sectionHeight*Map.TILE_SIZE + 10, sectionWidth*TILE_SIZE - 20,
							sectionHeight*TILE_SIZE - 20));
					sections.add(section);
				}
			}
			
			// fill sections
			/*
			 * This section is very confusing at first.
			 * The idea is to simplify map file creation,
			 * compared to just looping through the sections directly.
			 * The y variable represents the y value of the current section.
			 * The x variable represents the x value of the current section.
			 * The yy variable represents the y value of the current tile.
			 * The xx variable represents the x value of the current tile.
			 * These values can then be used to get the correct coordinate location of the current tile id.
			 * The id is then used to add the correct tile at that coordinate to that map section.
			 * Once more complex tile ids are created, this section could get very messy, and will
			 * likely have to be broken into multiple functions.
			 */
			for(int y = 0; y < mapHeight; y++) {
				for(int yy = 0; yy < sectionHeight; yy++) {
					for(int x = 0; x < mapWidth; x++) {
						MapSection curr = sections.get(y*mapWidth + x);
						for(int xx = 0; xx < sectionWidth; xx++) {
							// get tile id
							int tileID = in.nextInt();
							// get tile position
							int tileX = x*sectionWidth + xx;
							int tileY = y*sectionHeight + yy;
							// depending on tile ID, initialize proper tile
							switch(tileID) {
							case 0: // TEMP, not solid
								curr.getTiles().add(new Tile(tileX*TILE_SIZE, tileY*TILE_SIZE, TILE_SIZE, TILE_SIZE,
										false, null, Colors.green));
								break;
							case 1: // TEMP, solid
								curr.getTiles().add(new Tile(tileX*TILE_SIZE, tileY*TILE_SIZE, TILE_SIZE, TILE_SIZE,
										true, null, Colors.black.brighter().brighter()));
								break;
							case 2: // TEMP, path tile, not solid
								curr.getTiles().add(new Tile(tileX*TILE_SIZE, tileY*TILE_SIZE, TILE_SIZE, TILE_SIZE,
										false, null, Colors.red.darker().darker()));
								break;
							}
						}
					}
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void loadStructures(String structPath) {
		// load data from structure file
		Scanner in;
		try {
			in = new Scanner(new File(structPath));
			
			// get the number of structures to load
			int numStructs = in.nextInt();
			
			// loop through each structure
			for(int i = 0; i < numStructs; i++) {
				// get structure info
				int x = in.nextInt();
				int y = in.nextInt();
				int width = in.nextInt();
				int height = in.nextInt();
				int type = in.nextInt();
				
				// create structure
				Structure s = null;
				
				switch(type) {
				case 0: // house
					// TODO: add interiors to structures
					s = new House(x, y, width, height, "");
					break;
				}
				
				// load structure tiles
				for(int yy = 0; yy < height; yy++) {
					for(int xx = 0; xx < width; xx++) {
						int tileX = (x + xx)*TILE_SIZE;
						int tileY = (y + yy)*TILE_SIZE;
						int tileID = in.nextInt();
						
						switch(tileID) {
						case 0: // empty
							break;
						case 1: // wall tile
							s.exterior[yy*width + xx] = new StructTile(1, tileX, tileY, TILE_SIZE, TILE_SIZE,
									true, null, Colors.orange);
							break;
						case 2: // door
							s.exterior[yy*width + xx] = new Door(tileX, tileY, TILE_SIZE, TILE_SIZE,
									false, null, Colors.yellow);
							break;
						case 3: // window
							s.exterior[yy*width + xx] = new StructTile(1, tileX, tileY, TILE_SIZE, TILE_SIZE,
									true, null, Colors.black);
						}
						
						
					}
				}
				
				structures.add(s);
			}
			
			in.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<MapSection> getSections() {
		return sections;
	}
	
	public ArrayList<Structure> getStructures() {
		return structures;
	}
	
}
