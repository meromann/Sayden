package rltut;

import java.util.ArrayList;
import java.util.List;

public class DamageType {
	public static DamageType BLUNT = new DamageType("contundente", "BluntDamage", "Mueres por un fuerte impacto de un arma contundente");
	public static DamageType SLICE = new DamageType("corte", "SliceDamage", "Mueres por un corte");
	public static DamageType PIERCING = new DamageType("penetrante", "PiercingDamage", "Mueres apunialado");
	
	public static List<DamageType> getAvailableDamageTypes(){
		ArrayList<DamageType> damageTypes = new ArrayList<DamageType>();
		
		damageTypes.add(BLUNT);
		damageTypes.add(SLICE);
		damageTypes.add(PIERCING);
		
		return damageTypes;
	}
	
	private String name;
	public String name() { return name; }
	
	private String causeOfDeath;
	public String causeOfDeath() { return causeOfDeath; }
	
	private String woundType;
	public String wondType() { return woundType; }
	
	public DamageType(DamageType clone){
		this.name = clone.name();
		this.woundType = clone.wondType();
		this.causeOfDeath = clone.causeOfDeath();
	}
	
	public DamageType(String name, String woundType, String causeOfDeath){
		this.name = name;
		this.woundType = woundType;
		this.causeOfDeath = causeOfDeath;
	}
}
