package rltut;

import java.util.HashMap;
import java.util.Map;

public class Wound {
	private static int LVL1_DURATION = 20;
	private static int LVL2_DURATION = 38;
	
	protected int duration;
	
	public boolean isHealed() { return duration < 1; }
	
	private int severity;
	public int severity() { return severity; }
	
	private String name;
	public String name() { return name; }
	
	private static String genderizePosition(String position, String additive){
		if(position == "brazo" || position =="pecho") 
			return (additive == "de" ? "del " : "el ") + position;
		else
			return (additive == "de" ? "de la " : "la ") + position;
	}
	private String position;
	public String position() { return position; }
	public Wound setPosition(String position) { this.position = position; return this; }
	
	public static Map<String, Wound> TYPES;
	
	private Wound reference;
	
	public static void instantiateWounds(){
		TYPES = new HashMap<String, Wound>();
		//******************BLUNT 1**************************************
		TYPES.put("BLUNT1-ANY",new Wound(LVL1_DURATION,1,"moreton"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea inflingiendo un moreton en "+genderizePosition(this.position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 2**************************************
		TYPES.put("BLUNT2-ANY",new Wound(LVL2_DURATION,2,"contusion"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea generando una contusion en "+genderizePosition(this.position(), null));
				if(Math.random() < 0.3){
					creature.modifyActionPoints(-100, "aturdido");
					creature.modifyStatusColor(Constants.MESSAGE_STATUS_EFFECT_COLOR);
					creature.notifyArround(Constants.MESSAGE_STATUS_EFFECT_COLOR, "El impacto "+ (creature.isPlayer() ? "te aturde" : 
						("aturde " + (creature.gender() == 'M' ? "al " : "a la ") + creature.name())), creature);
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************SLICE 1**************************************
		TYPES.put("SLICE1-ANY",new Wound(LVL1_DURATION,1,"raspadura"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra causar una ligera raspadura en "+genderizePosition(this.position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************PIERCING 1**************************************
		TYPES.put("PIERCING1-ANY",new Wound(LVL1_DURATION,1,"tajo"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("roza " + (creature.intrinsicArmor().gender() == 'M' ? "el " : "la ") +
						creature.intrinsicArmor().name() + " "+genderizePosition(this.position(), "de"));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
	}
	
	public Wound(int duration, int severity, String name){
		this.duration = duration;
		this.severity = severity;
		this.name = name;
	}
	
	public Wound(Wound other){
		this.duration = other.duration; 
		this.name = other.name;
		this.severity = other.severity;
		this.position = other.position;
		this.reference = other;
	}
	
	public void update(Creature creature){
		duration--;
		if(reference != null)
			reference.update(creature);
	}
	
	public void onApply(Creature creature, Creature applier){
		if(reference != null)
			reference.onApply(creature, applier);
	}
	
	public void onFinish(Creature creature){ }
}
