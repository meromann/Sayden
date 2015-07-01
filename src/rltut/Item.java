package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Item {
	public static enum ItemType{
		SHIELD,
		WEAPON,
		ARMOR,
		HELMENT,
		STATIC,
		EDIBLE,
		READABLE,
		INTRINSIC,
		UNEQUIPPABLE
	}
	
	private ItemType itemType;
	public ItemType itemType() { return itemType; }
	
	private char gender;
	public char gender() { return gender; }
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private String name;
	public String name() { return broken ? name+ " " + (gender == 'M' ? "roto" : "rota") : name; }
	
	private String appearance;
	public String appearance() { return appearance; }

	private int rangedAttackValue;
	public int rangedAttackValue() { return rangedAttackValue; }
	public void modifyRangedAttackValue(int amount) { rangedAttackValue += amount; }
	
	private Effect quaffEffect;
	public Effect quaffEffect() { return quaffEffect; }
	public void setQuaffEffect(Effect effect) { this.quaffEffect = effect; }
	
	private List<Spell> writtenSpells;
	public List<Spell> writtenSpells() { return writtenSpells; }
	
	public Wound getWound(DamageType type, BodyPart bodyPart, Creature target) { return null; }
	
	private List<DamageType> damageTypes;
	public List<DamageType> damageTypes() { return damageTypes; }
	public Item addDamageType(DamageType newDamage, int power) { 
		DamageType newType = new DamageType(newDamage.name(), newDamage.wondType(), newDamage.causeOfDeath());
		this.damageTypes.add(newType.addPower(power)); 
		return this; 
	}
	
	private boolean broken = false;
	public boolean broken() { return broken; }
	public void makeBroken(boolean val) { this.broken = val; }
	
	private int playerBonusDamage;
	public int playerBonusDamage() { return playerBonusDamage; }
	public void modifyPlayerBonusDamage(int amount) { playerBonusDamage += amount; }
	
	private int perceivedValue;
	public int perceivedValue() { return perceivedValue; }
	public void setPerceivedValue(int newValue) { this.perceivedValue = newValue; }
	
	public void addWrittenSpell(String name, int manaCost, Effect effect){
		writtenSpells.add(new Spell(name, manaCost, effect));
	}
	
	/**Constructor para los items intrinsecos (arma / armadura de la criatura, pelaje, puños, etc)*/
	public Item(ItemType type, char gender, String name){
		this.itemType = type;
		this.gender = gender;
		this.name = name;
		this.damageTypes = new ArrayList<DamageType>();
		this.playerBonusDamage = 0;
	}
	
	public Item(ItemType type, char glyph, char gender, Color color, String name, String appearance, int value){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.gender = gender;
		this.appearance = appearance == null ? name : appearance;
		this.writtenSpells = new ArrayList<Spell>();
		this.damageTypes = new ArrayList<DamageType>();
		this.perceivedValue = value;
		this.itemType = type;
		this.playerBonusDamage = 0;
	}
	
	public Item(ItemType type, char glyph, char gender, Color color, String name, String appearance){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.gender = gender;
		this.appearance = appearance == null ? name : appearance;
		this.writtenSpells = new ArrayList<Spell>();
		this.damageTypes = new ArrayList<DamageType>();
		this.perceivedValue = 0;
		this.itemType = type;
		this.playerBonusDamage = 0;
	}
	
	public String details() {
		String details = "";
		
		for(int i = 0; i < damageTypes.size(); i++){
			details += "  "+damageTypes.get(i).name() + ": " + damageTypes.get(i).power();
		}
		
		/*if (attackValue != 0)
			details += "  ataque:" + attackValue;

		if (thrownAttackValue != 1)
			details += "  arrojadizo:" + thrownAttackValue;
		
		if (rangedAttackValue > 0)
			details += "  rango:" + rangedAttackValue;
		
		if (defenseValue != 0)
			details += "  defensa:" + defenseValue;

		if (foodValue != 0)
			details += "  comida:" + foodValue;*/
		
		return details;
	}
}
