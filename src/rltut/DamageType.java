package rltut;

import java.util.ArrayList;


public class DamageType {
	public static DamageType BLUNT = new DamageType("contundente", "BLUNT", "Mueres por un fuerte impacto de un arma contundente");
	public static DamageType SLICE = new DamageType("corte", "SLICE", "Mueres por un corte");
	public static DamageType PIERCING = new DamageType("penetrante", "PIERCING", "Mueres apu�alado");
	
	public static ArrayList<DamageType> getDamageTypes(){
		ArrayList<DamageType> allTypes = new ArrayList<DamageType>();
		
		allTypes.add(BLUNT);
		allTypes.add(SLICE);
		allTypes.add(PIERCING);
		
		return allTypes;
	}
	
	private Item itemOrigin;
	public Item itemOrigin() { return itemOrigin; }
	public void setOrigin(Item item) { this.itemOrigin = item; }
	
	private int power = 0;
	public int power() { return power; }
	public DamageType addPower(int pow) { this.power += pow; return this; }
	
	private String name;
	public String name() { return name; }
	
	private String causeOfDeath;
	public String causeOfDeath() { return causeOfDeath; }
	
	private String woundType;
	public String wondType() { return woundType; }
		
	public DamageType(String name, String woundType, String causeOfDeath){
		this.name = name;
		this.woundType = woundType;
		this.causeOfDeath = causeOfDeath;
	}
}
