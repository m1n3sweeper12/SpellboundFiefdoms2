package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.Main;
import core.Time;
import input.KeyHandler;
import map.Map;
import map.MapHandler;
import objects.GameObject;
import objects.ObjectHandler;
import objects.Player;
import objects.items.Armor;
import objects.items.Item;
import objects.items.Material;
import objects.items.Weapon;
import utils.Colors;

public class Game extends State {
	
	// boolean to track pauses,
	// removes flickering between Game and Pause states
	public static boolean canPause;
	
	// map handler instance to load maps
	private MapHandler mapHandler;
	
	// object handler instance to load objects
	private ObjectHandler objectHandler;
	
	public static boolean debugging, canToggleDebug, drawMiniMap, canToggleMiniMap;
	
	// constructor, sets canPause to true by default
	public Game(Main main) {
		super(main);
	}
	
	@Override
	public void init() {
		canPause = true;
		mapHandler = new MapHandler();
		objectHandler = new ObjectHandler();
		Time.getTimeFromFile("res/properties/time.txt");
		drawMiniMap = false;
	}

	@Override
	public void tick() {
		// tick game time
		Time.tick();
		// autosave every 5 in-game days
		if(Time.day%5 == 0 && Time.hour == 0 && Time.minute == 0 && Time.second == 0) {
			save();
		}
		
		// toggle debugging mode
		if(KeyHandler.getKey(KeyEvent.VK_Z)) {
			if(canToggleMiniMap) {
				drawMiniMap = !drawMiniMap;
				canToggleMiniMap = false;
			}
		} else {
			canToggleMiniMap = true;
		}
		
		// toggle debugging mode
		if(KeyHandler.getKey(KeyEvent.VK_X)) {
			if(canToggleDebug) {
				debugging = !debugging;
				canToggleDebug = false;
			}
		} else {
			canToggleDebug = true;
		}
		
		// handle pause state if space key pressed
		if(KeyHandler.getKey(KeyEvent.VK_SPACE)) {
			if(canPause) {
				StateHandler.state = STATES.Pause;
				Pause.canPause = false;
			}
		} else {
			canPause = true;
		}
		
		// update map
		mapHandler.tick();
		
		// update objects
		objectHandler.tick(mapHandler.getCurrMap());
	}

	@Override
	public void render(Graphics2D g) {
		// draw background
		g.setColor(Colors.black);
		g.fillRect(0, 0, main.getWidth(), main.getHeight());
		
		// translate tiles and objects to move with camera
		g.translate(-objectHandler.getCamera().getX(), -objectHandler.getCamera().getY());
		// draw map
		mapHandler.render(g);
		
		// draw objects
		objectHandler.render(g);
		
		// translate back so UI doesn't move
		g.translate(objectHandler.getCamera().getX(), objectHandler.getCamera().getY());
		
		////////
		// UI //
		////////
		
		// render mini map
		if(drawMiniMap)
			mapHandler.getCurrMap().renderMiniMap(g, objectHandler);
		
		// render time
		g.setColor(Colors.white);
		g.setFont(new Font("monospaced", Font.BOLD, 28));
		g.drawString("Day " + Time.day, main.getWidth() - 100, 30);
		g.drawString(String.format("%d:%02d", Time.hour, Time.minute), main.getWidth() - 100, 60);
		
		// get player object to render player stats
		Player p = null;
		for(GameObject o : objectHandler.getObjects()) {
			if(o.getID() == 0)
				p = (Player)o;
		}
		
		if(p != null) {
			// render player inventory
			// player inventory has 10 slots
			int startX = main.getWidth()/2 - Map.TILE_SIZE*5; // x position where inventory will start rendering
			for(int i = 0; i < 10; i++) {
				// draw background
				g.setColor(Colors.black);
				g.fillRect(startX + Map.TILE_SIZE*i - 2, main.getHeight() - 102, Map.TILE_SIZE + 4, Map.TILE_SIZE + 4);
				// draw slot boxes
				g.setColor(Colors.blue.brighter());
				g.fillRect(startX + Map.TILE_SIZE*i + 2, main.getHeight() - 98, Map.TILE_SIZE - 4, Map.TILE_SIZE - 4);
				// draw items
				Item item = null;
				if(p.getInventory().getItem(i) != null) {
					switch(p.getInventory().getItem(i).getType()) {
					case Armor:
						item = new Armor(p.getInventory().getItem(i));
						break;
					case Material:
						item = new Material(p.getInventory().getItem(i));
						break;
					case Weapon:
						item = new Weapon(p.getInventory().getItem(i));
						break;
					}
				}
				if(item != null) { // only draw item if not null
					item.setX(startX + Map.TILE_SIZE*i + Map.TILE_SIZE/4);
					item.setY(main.getHeight() - 100 + Map.TILE_SIZE/4);
					item.objectRender(g);
				}
			}
			// draw current slot highlight
			g.setColor(Colors.yellow);
			g.setStroke(new BasicStroke(2));
			g.drawRect(startX + Map.TILE_SIZE*p.getCurrSlot() + 2, main.getHeight() - 98,
					Map.TILE_SIZE - 4, Map.TILE_SIZE - 4);
			g.setStroke(new BasicStroke(1));
			
			// draw current item name
			if(p.getCurrItem() != null) {
				g.setColor(Colors.white);
				g.setFont(new Font("Monospaced", Font.BOLD, 14));
				String name = p.getCurrItem().getName();
				g.drawString(name, main.getWidth()/2 - g.getFontMetrics().stringWidth(name)/2, main.getHeight() - 125);
			}
			
			// draw stamina bar
			int stamStartY = 0;
			if(drawMiniMap) {
				stamStartY = 150;
			} else {
				stamStartY = 50;
			}
			g.setColor(Colors.black);
			g.fillRect(50, stamStartY, 204, 29);
			g.setColor(Colors.blue);
			g.fillRect(52, stamStartY + 2, p.getRunStamina(), 25);
			
			// draw health bar
			int hpStartY = 0;
			if(drawMiniMap) {
				hpStartY = 200;
			} else {
				hpStartY = 100;
			}
			g.setColor(Colors.black);
			g.fillRect(50, hpStartY, 204, 29);
			g.setColor(Colors.green);
			g.fillRect(52, hpStartY + 2, p.getHP(), 25);
			
			// draw power bar
			int powStartY = 0;
			if(drawMiniMap) {
				powStartY = 250;
			} else {
				powStartY = 150;
			}
			g.setColor(Colors.black);
			g.fillRect(50, powStartY, 204, 29);
			g.setColor(Colors.red);
			g.fillRect(52, powStartY + 2, p.getPower()*2, 25);
		}
		
		// draw debugging string if debugging
		if(debugging) {
			g.setColor(Color.red);
			g.setFont(new Font("Sans Serif", Font.BOLD, 18));
			g.drawString("DEBUGGING", main.getWidth() - 120, main.getHeight() - 25);
		}
	}
	
	public static void save() {
		Time.writeTimeToFile("res/properties/time.txt");
	}
	
}
