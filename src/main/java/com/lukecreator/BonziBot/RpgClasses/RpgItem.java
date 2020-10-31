package com.lukecreator.BonziBot.RpgClasses;

import java.awt.Color;
import java.io.Serializable;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class RpgItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final RpgItem DEFAULTSWORD = new RpgItem("Bronze Sword", "It may not be a super amazing weapon, but it can get the job done!", RpgItemType.Weapon, 4);
	public static final RpgItem DEFAULTHEALTH = new RpgItem("Water Bottle", "Heals you up a bit if you're in trouble!", RpgItemType.Consumable, 6);
	public enum RpgStatus {
		Damaging,
		NoMana
	}
	public enum RpgItemType {
		Resource, Weapon, Single_Use_Weapon, Spell, Consumable
	}
	
	public int power; // This can be the damage, health to restore, price it sells for, etc...
	public int manaConsumption;
	
	public RpgStatus effect;
	public int effectTurns;
	
	public RpgItemType type; // Use RpgItem#typeName() to get an appropriate name for display.
	public String name;
	public String description;
	
	public RpgItem(String name, String description, RpgItemType type, int power) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.power = power;
	}
	public RpgItem(String name, String description, RpgItemType type, int power, int manaConsumption) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.power = power;
		this.manaConsumption = manaConsumption;
	}
	public RpgItem(String name, String description, RpgItemType type, RpgStatus stat, int statTurns, int power, int manaConsumption) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.power = power;
		this.manaConsumption = manaConsumption;
		
		this.effect = stat;
		this.effectTurns = statTurns;
	}
	public String typeName() {
		return type.toString().replace('_', ' ');
	}
	public boolean usesMana() {
		return (type == RpgItemType.Spell && manaConsumption > 0);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Name: " + name + "\n");
		sb.append("Type: " + typeName() + "\n");
		if(type == RpgItemType.Consumable) {
			sb.append("Restores: " + power);
		} else if(type == RpgItemType.Resource) {
			sb.append("Sells for: " + power);
		} else if(type == RpgItemType.Single_Use_Weapon ||
			type == RpgItemType.Weapon) {
			sb.append("Damage: " + power);
		} else if(type == RpgItemType.Spell) {
			sb.append("Damage: " + power + "\nConsumes: " + manaConsumption + " mana.");
		}
		return sb.toString();
	}
	public String toDescriptionString() {
		StringBuilder sb = new StringBuilder("Name: " + name + "\n");
		sb.append("Type: " + typeName() + "\n");
		if(type == RpgItemType.Consumable) {
			sb.append("Restores: " + power);
		} else if(type == RpgItemType.Resource) {
			sb.append("Sells for: " + power);
		} else if(type == RpgItemType.Single_Use_Weapon ||
			type == RpgItemType.Weapon) {
			sb.append("Damage: " + power);
		} else if(type == RpgItemType.Spell) {
			sb.append("Damage: " + power + "\nConsumes: " + manaConsumption + " mana.");
		}
		sb.append("\nDescription: " + description);
		return sb.toString();
	}
	public MessageEmbed toEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.magenta);
		eb.setTitle("Item Info");
		eb.setDescription(toDescriptionString());
		return eb.build();
	}
}
