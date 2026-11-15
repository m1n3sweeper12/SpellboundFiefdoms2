package objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import core.Main;
import map.Map;
import map.MapSection;
import map.Tile;
import objects.attacks.Attack;
import objects.hostiles.Hostile;
import objects.items.Armor;
import objects.items.Item;
import objects.items.Material;
import objects.items.WandWeapon;
import objects.items.Weapon;
import objects.passives.NPCTest;
import objects.passives.Passive;

public class ObjectHandler {
	// variable for size of objects
	public static final int OBJ_SIZE = Map.TILE_SIZE;
	
	// variable for size of items
	public static final int ITEM_SIZE = Map.TILE_SIZE/2;
	
	// list of all objects in the game
	private static ArrayList<GameObject> objects;
	// list of all hostile mobs
	private static ArrayList<Hostile> hostiles;
	// list of all passive mobs
	private static ArrayList<Passive> passives;
	// list of all items
	private static ArrayList<Item> items;
	// list of all weapons
	private static ArrayList<Weapon> weapons;
	
	// variable for player, since it has specialized functions
	private Player player;
	
	// camera to follow player
	private Camera cam;
	
	// TEMP
	private NPCTest npc1;
	private Weapon testWeapon;
	
	public ObjectHandler() {
		// initialize array lists
		objects = new ArrayList<GameObject>();
		hostiles = new ArrayList<Hostile>();
		passives = new ArrayList<Passive>();
		items = new ArrayList<Item>();
		weapons = new ArrayList<Weapon>();
		
		// TODO: load objects from file
		loadPlayer();
		
		cam = new Camera(0, 0);
		
		// TEMP
		WandWeapon wand = new WandWeapon(null, 200, 200, true);
		objects.add(wand);
		items.add(wand);
		weapons.add(wand);
		
		// TEMP
		testWeapon = new Weapon(null, 1500, 1200, 1, true, "Test Weapon");
		objects.add(testWeapon);
		items.add(testWeapon);
		weapons.add(testWeapon);
		
		// TEMP
		npc1 = new NPCTest(500, 700);
		objects.add(npc1);
		passives.add(npc1);
	}
	
	public void tick(Map m) {
		// TODO: optimize object updating
		// update objects
		for(GameObject o : objects) {
			o.tick();
			o.objectTick();
			checkTileCollisions(m, o);
		}
		
		// activate/deactivate map sections based on player position
		for(MapSection ms : m.getSections()) {
			if(ms.getBounds().intersects(player.getRoamBoundary())) {
				ms.setActive(true);
			} else {
				ms.setActive(false);
			}
		}
		
		for(Item i : items) {
			// check for player-item collisions
			// add item to player inventory if inventory is not full
			if(player.getBounds().intersects(i.getBounds())) {
				if(player.getInventory().addItem(i)) {
					i.setPickedUp(true);
					i.setParent(player);
					player.setHeldItem(i);
				}
			}
			
			// check for hostile-item collisions
			// add item to hostile inventory if inventory is not full
			for(Hostile h : hostiles) {
				if(h.getBounds().intersects(i.getBounds())) {
					if(h.getInventory().addItem(i)) {
						i.setPickedUp(true);
						i.setParent(h);
						h.setHeldItem(i);
					}
				}
			}
		}
		
		// handle hostile states
		for(Hostile h : hostiles) {
			if(h.getBounds().intersects(player.getBounds())) {
				h.setState("attack");
				h.attack(player);
			} else if(h.getRoamBoundary().intersects(player.getBounds())) {
				h.setState("chase");
				h.chase(player);
			} else {
				h.setState("roam");
				h.roam();
			}
		}
		
		// remove hostile from hostiles and objects lists if dead
		hostiles.removeIf(h -> h.getHP() <= 0);
		objects.removeIf(o -> o.getHP() <= 0 && o.getID() == 1); // don't remove player
		
		// remove hostile from hostiles and objects lists if outside of map
		hostiles.removeIf(h -> !(Map.mapBounds.contains(new Point(h.getCenterX(), h.getCenterY()))));
		objects.removeIf(o -> !(Map.mapBounds.contains(new Point(o.getCenterX(), o.getCenterY()))));
		
		// remove item from objects and items lists if picked up
		items.removeIf(i -> i != null && i.isPickedUp());
		objects.removeIf(o -> o != null && o.getID() == 3 && ((Item)o).isPickedUp());
		
		// delete attacks if collides with object
		for(Weapon w : weapons) {
			for(GameObject o : objects) {
				if(o.equals(w)) continue; // don't remove if object is weapon
				if(o.equals(w.getParent())) continue; // don't remove if object is holding weapon
				for(Attack a : w.getAttacks()) {
					if(a.getBounds().intersects(o.getBounds())) {
						o.hurt(a.getDamage());
					}
				}
				w.getAttacks().removeIf(a -> a.getBounds().intersects(o.getBounds()));
			}
		}
		
		// check for hostile-player attack collision
		for(Hostile h : hostiles) {
			if(h.getBaseAttack() != null) {
				if(h.getBaseAttack().getBounds().intersects(player.getBounds())) {
					player.hurt(h.getBaseAttack().getDamage());
					h.setBaseAttack(null);
				}
			}
		}
		
		// update camera position
		cam.tick(player, Main.screen_width, Main.screen_height);
		
		// sort objects to layer properly
		objects.sort(Comparator.comparing(GameObject::getY));
	}
	
	public void render(Graphics2D g) {
		// TODO: optimize object rendering
		for(GameObject o : objects) {
			o.objectRender(g);
			o.render(g);
		}
	}
	
	private void loadPlayer() {
		player = new Player(100, 100);
		Scanner in;
		try {
			in = new Scanner(new File("res/properties/player.txt"));
			
			int x = in.nextInt();
			int y = in.nextInt();
			
			player = new Player(x, y);
			player.setHP(in.nextInt());
			player.setPower(in.nextInt());
			
			player.setCurrSlot(in.nextInt());
			
			// load inventory objects
			while(in.hasNext()) {
				String itemString = in.nextLine();
				if(itemString.length() > 0) {
					int itemX = Integer.parseInt(itemString.substring(0, itemString.indexOf(" ")));
					itemString = itemString.substring(itemString.indexOf(" ") + 1);
					int itemY = Integer.parseInt(itemString.substring(0, itemString.indexOf(" ")));
					itemString = itemString.substring(itemString.indexOf(" ") + 1);
					String itemType = itemString.substring(0, itemString.indexOf(" "));
					itemString = itemString.substring(itemString.indexOf(" ") + 1);
					int itemQuantity = Integer.parseInt(itemString.substring(0, itemString.indexOf(" ")));
					itemString = itemString.substring(itemString.indexOf(" ") + 1);
					boolean itemRender = Boolean.parseBoolean(itemString.substring(0, itemString.indexOf(" ")));
					itemString = itemString.substring(itemString.indexOf(" ") + 1);
					String itemName = itemString;
					
					Item i = null;
					
					switch(itemType) {
					case "Weapon":
						i = new Weapon(player, itemX, itemY, itemQuantity, itemRender, itemName);
						weapons.add((Weapon)i);
						break;
					case "Armor":
						i = new Armor(player, itemX, itemY, itemQuantity, itemRender, itemName);
						break;
					case "Material":
						i = new Material(player, itemX, itemY, itemQuantity, itemRender, itemName);
						break;
					case "WandWeapon":
						i = new WandWeapon(player, itemX, itemY, itemRender);
						weapons.add((Weapon)i);
						break;
					default:
						i = new Material(player, itemX, itemY, itemQuantity, itemRender, itemName);
						break;
					}
					
					player.getInventory().addItem(i);
					objects.add(i);
					items.add(i);
				}
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		objects.add(player);
	}
	
	public void savePlayer() {
		String save = "";
		// add player coords
		save += player.getX() + " ";
		save += player.getY() + "\n";
		// add current health
		save += player.getHP() + "\n";
		// add current power
		save += player.getPower() + "\n";
		save += player.getCurrSlot() + "\n";
		// add info for each player item
		for(Item i : player.getInventory().getItems()) {
			if(i == null) {
				continue;
			}
			String temp = "";
			temp += i.getX() + " ";
			temp += i.getY() + " ";
			temp += i.getType() + " ";
			temp += i.getQuantity() + " ";
			temp += i.isRenderingQuantity() + " ";
			temp += i.getName() + "\n";
			save += temp;
		}
		
		try {
			Files.writeString(Path.of("res/properties/player.txt"), save);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// handle tile-object collisions
	private void checkTileCollisions(Map m, GameObject o) {
		
		for(MapSection s : m.getSections()) {
			// if section is locked, set positions to edge of section
			if(s.isLocked()) {
				if(o.getTopBounds().intersects(s.getBounds())) {
					o.setY(s.getBounds().y + s.getBounds().height);
				}
				if(o.getBottomBounds().intersects(s.getBounds())) {
					o.setY(s.getBounds().y - o.getBounds().height);
				}
				if(o.getLeftBounds().intersects(s.getBounds())) {
					o.setX(s.getBounds().x + s.getBounds().width);
				}
				if(o.getRightBounds().intersects(s.getBounds())) {
					o.setX(s.getBounds().x - o.getBounds().width);
				}
			}
			
			
			for(Tile t : s.getTiles()) {
				if(t.isSolid()) { // if tile is solid, set positions to edge of tile
					if(o.getTopBounds().intersects(t.getBounds())) {
						o.setY(t.getBounds().y + t.getBounds().height);
					}
					if(o.getBottomBounds().intersects(t.getBounds())) {
						o.setY(t.getBounds().y - o.getBounds().height);
					}
					if(o.getLeftBounds().intersects(t.getBounds())) {
						o.setX(t.getBounds().x + t.getBounds().width);
					}
					if(o.getRightBounds().intersects(t.getBounds())) {
						o.setX(t.getBounds().x - o.getBounds().width);
					}
				}
			}
		}
	}
	
	public static void addEnemy(Hostile e) {
		objects.add(e);
		hostiles.add(e);
		if(e.getWeapon() != null) {
			weapons.add(e.getWeapon());
		}
	}
	
	public ArrayList<GameObject> getObjects() {
		return objects;
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
