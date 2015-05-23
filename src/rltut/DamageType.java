package rltut;

public class DamageType {
	public static DamageType BLUNT = new DamageType("contundente", "BLUNT");
	public static DamageType SLICE = new DamageType("corte", "SLICE");
	public static DamageType PIERCING = new DamageType("penetrante", "PIERCING");
	
	private int power = 0;
	public int power() { return power; }
	public DamageType addPower(int pow) { this.power += pow; return this; }
	
	private String name;
	public String name() { return name; }
	
	private String woundType;
	public String wondType() { return woundType; }
		
	public DamageType(String name, String woundType){
		this.name = name;
		this.woundType = woundType;
	}
}
