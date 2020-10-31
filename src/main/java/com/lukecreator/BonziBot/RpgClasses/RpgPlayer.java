package com.lukecreator.BonziBot.RpgClasses;

import java.io.Serializable;

import com.lukecreator.BonziBot.RpgClasses.RpgItem.RpgItemType;
import com.lukecreator.BonziBot.RpgClasses.RpgItem.RpgStatus;

public class RpgPlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public long ownerID;
	public RpgInventory inv;
	
	public int health;
	public int maxHealth;
	
	public int mana;
	public int maxMana;
	
	// State variables
	public RpgItem equippedItem;
	public boolean wasAttacked;
	public RpgStatus effect;
	public int effectTurns;
	
	// Constructor
	public RpgPlayer(long ownerID) {
		this.ownerID = ownerID;
		
		inv = new RpgInventory();
		inv.addItem(RpgItem.DEFAULTSWORD);
		inv.addItem(RpgItem.DEFAULTHEALTH);
		
		health = 20;
		maxHealth = health;
		
		mana = 10;
		maxMana = mana;
		
		equippedItem = null;
		wasAttacked = false;
	}
	
	// Methods
	/*
	 * Damages this player by <amount>. Returns if the player was killed.
	 */
	public boolean damage(int amount) {
		wasAttacked = true;
		health-=amount;
		return isDead();
	}
	/*
	 * Heals the player using an item. Returns true if succeeded, false if the item is not in their inventory.
	 */
	public boolean consume(RpgItem item) {
		if(!inv.hasItem(item)) {
			return false;
		}
		inv.removeItem(item);
		equippedItem = null;
		health+=item.power;
		if(health > maxHealth)
			health = maxHealth;
		return true;
	}
	/*
	 * Allows this user to be attacked + Restores 1 mana (counts as a "turn").
	 * Returns if dead.
	 */
	public boolean retaliate() {
		if(effectTurns <= 0) {
			effect = null;
		}
		if(effect != null && effectTurns > 0) {
			effectTurns--;
			if(effectTurns <= 0) {
				effect = null;
			}
		}
		wasAttacked = false;
		boolean rm = false;
		if(mana < maxMana) {
			mana++;
			rm = true;
		}
		return tryEffectDamage(rm);
	}
	
	/*
	 * Check and apply effect damage.
	 */
	public boolean tryEffectDamage(boolean restoredMana) {
		if(effect != null) {
			switch(effect) {
			case Damaging:
				health-=1;
			case NoMana:
				// remove 1 mana if restored, otherwise don't.
				mana -= restoredMana ? 1 : 0;
			}
		}
		return isDead();
	}
	
	/*
	 * This will automatically perform the required manipulations on the player when using an item. Takes a single use weapon, or a spell. For consumables, use RpgPlayer#consume
	 */
	public void attackWith(RpgItem item) {
		if(item.type == RpgItemType.Single_Use_Weapon) {
			inv.removeItem(item);
			equippedItem = null;
		} else if(item.type == RpgItemType.Spell) {
			mana -= item.manaConsumption;
			if(mana < 0) {
				mana = 0;
				System.out.print("Mana was consumed past 0...?\n");
			}
			if(item.effect != null) {
				effect = item.effect;
				effectTurns = item.effectTurns;
			}
		}
	}
	/*
	 * "respawns" this Player.
	 */
	public void dead() {
		health = maxHealth;
		mana = maxMana;
		wasAttacked = false;
	}
	
	// State Methods
	public boolean isDead() {
		return health <= 0;
	}
	public boolean hasEnoughMana(RpgItem spell) {
		return mana >= spell.manaConsumption;
	}
}
