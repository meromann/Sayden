package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item extends Thing{
	
	public String name() { return ""+getData(RPG.NAME); }//broken ? name+ " " + ((char)getData("gender") == 'M' ? "roto" : "rota") : name; 
	public String nameElLa() { return ((char)getData(RPG.GENDER) == 'M' ? "el " : "la ") + name(); }
	public String nameUnUna() { return ((char)getData(RPG.GENDER) == 'M' ? "un " : "una ") + name(); }
	
	private List<Spell> writtenSpells;
	public List<Spell> writtenSpells() { return writtenSpells; }
	public void addWrittenSpell(String name, int manaCost, Effect effect){
		writtenSpells.add(new Spell(name, manaCost, effect));
	}
	
	public Wound getWound(DamageType type, BodyPart bodyPart, Creature target) { return null; }
	
	/**Constructor para los items intrinsecos (arma / armadura de la criatura, pelaje, puños, etc)*/
	public Item(char gender, String name){
		this.data = new HashMap<String, Object>();
		
		setData(RPG.GENDER, gender);
		setData(RPG.NAME, name);
	}
	
	public Item(char glyph, char gender, Color color, String name, String appearance){
		this.data = new HashMap<String, Object>();
		
		setData(RPG.GENDER, gender);
		setData(RPG.NAME, name);
		setData(RPG.APPEARANCE, appearance == null ? name : appearance);
		setData(RPG.GLYPH, glyph);
		setData(RPG.COLOR, color);
		setData(RPG.ORIGINAL_COLOR, color);
		
		this.writtenSpells = new ArrayList<Spell>();
	}
	
	public String details() {
		String details = "";		
		return details;
	}
}
