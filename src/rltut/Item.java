package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Item {
	
	private char gender;
	public char gender() { return gender; }
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private String name;
	public String name() { return name; }

	private String appearance;
	public String appearance() { return appearance; }
	
	private int foodValue;
	public int foodValue() { return foodValue; }
	public void modifyFoodValue(int amount) { foodValue += amount; }

	private int attackValue;
	public int attackValue() { return attackValue; }
	public void modifyAttackValue(int amount) { attackValue += amount; }

	private int defenseValue;
	public int defenseValue() { return defenseValue; }
	public void modifyDefenseValue(int amount) { defenseValue += amount; }

	private int thrownAttackValue;
	public int thrownAttackValue() { return thrownAttackValue; }
	public void modifyThrownAttackValue(int amount) { thrownAttackValue += amount; }

	private int rangedAttackValue;
	public int rangedAttackValue() { return rangedAttackValue; }
	public void modifyRangedAttackValue(int amount) { rangedAttackValue += amount; }
	
	private Effect quaffEffect;
	public Effect quaffEffect() { return quaffEffect; }
	public void setQuaffEffect(Effect effect) { this.quaffEffect = effect; }
	
	private List<Spell> writtenSpells;
	public List<Spell> writtenSpells() { return writtenSpells; }
	
	private List<DamageType> damageTypes;
	public List<DamageType> damageTypes() { return damageTypes; }
	public Item addDamageType(DamageType newDamage, int power) { 
		DamageType newType = new DamageType(newDamage.name(), newDamage.wondType());
		this.damageTypes.add(newType.addPower(power)); 
		return this; 
	}
	
	private int perceivedValue;
	public int perceivedValue() { return perceivedValue; }
	public void setPerceivedValue(int newValue) { this.perceivedValue = newValue; }
	
	public void addWrittenSpell(String name, int manaCost, Effect effect){
		writtenSpells.add(new Spell(name, manaCost, effect));
	}
	
	public Item(char glyph, char gender, Color color, String name, String appearance, int value){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.gender = gender;
		this.appearance = appearance == null ? name : appearance;
		this.thrownAttackValue = 1;
		this.writtenSpells = new ArrayList<Spell>();
		this.damageTypes = new ArrayList<DamageType>();
		this.perceivedValue = value;
	}
	
	public Item(char glyph, char gender, Color color, String name, String appearance){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.gender = gender;
		this.appearance = appearance == null ? name : appearance;
		this.thrownAttackValue = 1;
		this.writtenSpells = new ArrayList<Spell>();
		this.damageTypes = new ArrayList<DamageType>();
		this.perceivedValue = 0;
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
