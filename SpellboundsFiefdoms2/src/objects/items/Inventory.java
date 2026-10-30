package objects.items;

import java.util.ArrayList;

public class Inventory {
	
	private int numSlots;
	private ArrayList<Item> items;
	
	public Inventory(int numSlots) {
		this.numSlots = numSlots;
		// initialize item array list with number of slots
		items = new ArrayList<Item>(numSlots);
		// fill array list with null items
		for(int i = 0; i < numSlots; i++) {
			items.add(null);
		}
	}
	
	// method to get list of items
	public ArrayList<Item> getItems() {
		return items;
	}
	
	// method to get an item at a given index
	public Item getItem(int index) {
		return items.get(index);
	}
	
	public int getNumSlots() {
		return numSlots;
	}
	
	// method to add new item to list
	public boolean addItem(Item item) {
		// loop through array of items and find empty slot
		// if no empty slot, can't pick up item
		for(int i = 0; i < numSlots; i++) {
			if(items.get(i) == null) {
				item.setPickedUp(true);
				items.set(i, item);
				return true;
			}
		}
		return false;
	}
	
}
