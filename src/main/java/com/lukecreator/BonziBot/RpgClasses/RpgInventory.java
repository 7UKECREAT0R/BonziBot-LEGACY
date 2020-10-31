package com.lukecreator.BonziBot.RpgClasses;

import java.awt.Color;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lukecreator.BonziBot.RpgClasses.RpgItem.RpgItemType;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class RpgInventory implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<RpgItem> contents;
	
	public RpgInventory() {
		contents = new ArrayList<RpgItem>();
	}
	public RpgInventory(RpgItem... items) {
		contents = new ArrayList<RpgItem>(Arrays.asList(items));
	}
	
	public void addItem(RpgItem item) {
		contents.add(item);
	}
	public void removeItem(RpgItem item) {
		if(hasItemBasic(item)) {
			contents.remove(item);
		} else if(hasItemAdvanced(item)) {
			RpgItem i = searchForItem(item.name, item.description);
			if(i == null) { return; }
			contents.remove(i);
		}
	}
	
	/*
	 * Alias for hasItemAdvanced(RpgItem item).
	 */
	public boolean hasItem(RpgItem item) {
		return hasItemAdvanced(item);
	}
	public boolean hasItemBasic(RpgItem item) {
		int index = contents.indexOf(item);
		return index != -1;
	}
	public boolean hasItemAdvanced(RpgItem item) {
		if(!hasItemBasic(item)) {
			List<RpgItem> itms = new ArrayList<RpgItem>();
			contents.forEach(i -> {
				if(i.name.equalsIgnoreCase(item.name)) {
					itms.add(i);
					return; // This will only cancel the current forEach iteration.
				}
				if(i.description.equalsIgnoreCase(item.description)) {
					itms.add(i);
					return; // Same for this.
				}
			});
			return !itms.isEmpty();
		} else {
			return true;
		}
	}
	
	/*
	 * Searches for an item with EITHER the same name or description as specified.
	 */
	public RpgItem searchForItem(String name, String description) {
		List<RpgItem> itms = new ArrayList<RpgItem>();
		contents.forEach(i -> {
			if(i.name.equalsIgnoreCase(name)) {
				itms.add(i);
				return;
			}
			if(i.description.equalsIgnoreCase(description)) {
				itms.add(i);
				return;
			}
		});
		if(!itms.isEmpty()) {
			return itms.get(0);
		} else {
			return null;
		}
	}
	/*
	 * Returns an embedobject that represents this inventory. Does error handling automatically.
	 */
	public MessageEmbed toEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.magenta);
		eb.setTitle("Current Inventory:");
		
		if(contents.isEmpty()) {
			eb.addField("No Items!", "Your backpack is empty... Probably should fill it with some stuff.", false);
			return eb.build();
		}
		for(RpgItem item : contents) {
			if(item.type == RpgItemType.Weapon) {
				eb.addField(item.name, "Type: Weapon\nDamage: " + item.power, false);
				continue;
			} else if(item.type == RpgItemType.Single_Use_Weapon) {
				eb.addField(item.name, "Type: Single Use Weapon\nDamage: " + item.power, false);
				continue;
			} else if(item.type == RpgItemType.Consumable) {
				eb.addField(item.name, "Type: Consumable\nRestores: " + item.power, false);
				continue;
			} else if(item.type == RpgItemType.Resource) {
				eb.addField(item.name, "Type: Resource\nSells for " + item.power + " coins.", false);
				continue;
			} else if(item.type == RpgItemType.Spell) {
				eb.addField(item.name, "Type: Spell\nDamage: " + item.power, false);
				continue;
			} else {
				eb.addField("Invalid Item (name: " + item.name + ")", "This item seems to exist in another dimension or something idk, report it to the devs using b:report for a reward!", false);
				continue;
			}
		}
		return eb.build();
	}
	/*
	 * Returns a random RPG item in this inventory. If the inventory is empty, this will return null.
	 */
	public RpgItem getRandomItem() {
		if(contents.isEmpty()) {
			return null;
		}
		SecureRandom rand = new SecureRandom();
		return contents.get(rand.nextInt(contents.size()));
	}
	
	// These are shortcuts, not actual implementations.
	public boolean isEmpty() {
		return contents.isEmpty();
	}
	public RpgItem get(int i) {
		return contents.get(i);
	}
	public int size() {
		return contents.size();
	}
}
