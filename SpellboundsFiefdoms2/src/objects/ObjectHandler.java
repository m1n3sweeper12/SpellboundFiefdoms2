package objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import core.Main;
import map.Map;
import map.MapSection;
import map.Tile;
import objects.attacks.Attack;
import objects.hostiles.Hostile;
import objects.hostiles.SimpleEnemy;
import objects.items.Item;
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
	private ArrayList<GameObject> objects;
	// list of all hostile mobs
	private ArrayList<Hostile> hostiles;
	// list of all passive mobs
	private ArrayList<Passive> passives;
	// list of all items
	private ArrayList<Item> items;
	// list of all weapons
	private ArrayList<Weapon> weapons;
	
	// variable for player, since it has specialized functions
	private Player player;
	
	// camera to follow player
	private Camera cam;
	
	// TEMP
	private SimpleEnemy enemy1;
	private NPCTest npc1;
	
	public ObjectHandler() {
		// initialize array lists
		objects = new ArrayList<GameObject>();
		hostiles = new ArrayList<Hostile>();
		passives = new ArrayList<Passive>();
		items = new ArrayList<Item>();
		weapons = new ArrayList<Weapon>();
		
		// TODO: load objects from file
		
		// TEMP
		player = new Player(100, 100);
		objects.add(player);
		
		cam = new Camera(0, 0);
		
		// TEMP
		enemy1 = new SimpleEnemy(900, 900);
		objects.add(enemy1);
		hostiles.add(enemy1);
		
		// TEMP
		WandWeapon wand = new WandWeapon(200, 200, true);
		objects.add(wand);
		items.add(wand);
		weapons.add(wand);
		
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
		
//		for(Weapon w : weapons) {
//			Attack a = w.getAttack();
//			if(!objects.contains(a) && a != null) objects.add(a);
//			if(!attacks.contains(a) && a != null) attacks.add(a);
//		}
//		
//		objects.removeIf(attack ->  attack.getID() == 4 && !attacks.contains(attack));
//		attacks.removeIf(attack -> attack.getTime() <= 0);
		
		for(Item i : items) {
			// check for player-item collisions
			// add item to player inventory if inventory is not full
			if(player.getBounds().intersects(i.getBounds())) {
				if(player.getInventory().addItem(i)) {
					i.setPickedUp(true);
					player.setHeldItem(i);
				}
			}
			
			// check for hostile-item collisions
			// add item to hostile inventory if inventory is not full
			for(Hostile h : hostiles) {
				if(h.getBounds().intersects(i.getBounds())) {
					if(h.getInventory().addItem(i)) {
						i.setPickedUp(true);
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
		
		// remove item from objects and items lists if picked up
		items.removeIf(i -> i != null && i.isPickedUp());
		objects.removeIf(o -> o != null && o.getID() == 3 && ((Item)o).isPickedUp());
		
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
	
	public ArrayList<GameObject> getObjects() {
		return objects;
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
