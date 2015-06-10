package rltut;

import java.util.HashMap;
import java.util.Map;

public class Wound {
	public static Map<String, Wound> TYPES;
		
	public static void instantiateWounds(){
		TYPES = new HashMap<String, Wound>();
		//******************BLUNT 1**************************************
		TYPES.put("BLUNT1-ANY",new Wound(Constants.LVL1_DURATION,1,"moreton"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea inflingiendo un moreton en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 2**************************************
		TYPES.put("BLUNT2-ANY",new Wound(Constants.LVL2_DURATION,2,"contusion"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea generando una contusion en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
				if(Math.random() < 0.1){
					creature.modifyActionPoints(-100, "aturdido");
					creature.modifyStatusColor(Constants.MESSAGE_STATUS_EFFECT_COLOR);
					creature.notifyArround(Constants.MESSAGE_STATUS_EFFECT_COLOR, "El impacto "+ (creature.isPlayer() ? "te aturde" : 
						("aturde " + (creature.gender() == 'M' ? "al " : "a la ") + creature.name())), creature);
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 3**************************************
		TYPES.put("BLUNT3-ANY",new Wound(Constants.LVL3_DURATION,3,"fisura"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea generando una severa fisura en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
				if(Math.random() < 0.4){
					creature.modifyActionPoints(-200, "bajo gran dolor");
					creature.modifyStatusColor(Constants.MESSAGE_STATUS_EFFECT_COLOR);
					creature.notifyArround(Constants.MESSAGE_STATUS_EFFECT_COLOR, "El dolor "+ (creature.isPlayer() ? "es muy intenso" : 
						((creature.gender() == 'M' ? "del " : "de la ") + creature.name() + " es muy intenso")), creature);
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 4 ARM**********************************
		TYPES.put("BLUNT4-brazo",new Wound(Constants.INCURABLE,4,"fractura expuesta"){
			public void onApply(Creature creature, Creature applier){
				Item itemToDrop = creature.shield();
				applier.doAction("impacta con fuerza, fracturando horriblemente "+
						(creature.isPlayer() ? "tu brazo" : 
							StringUtils.genderizeBodyPosition(this.bodyPart().position(), null) + " " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true)));
				creature.drop(itemToDrop, "");
				creature.resetActionPoints();
				creature.modifyAttackSpeed(Constants.BROKEN_ARM_PENALTY);
			}
			public void update(Creature creature){
				Item itemToDrop = creature.shield();
				if(itemToDrop != null){
					if(Math.random() < Constants.BROKEN_ARM_DROP_CHANCE){
						creature.notifyArround((creature.isPlayer() ? "Tu" : "El") +
							"brazo fracturado "+ (creature.isPlayer() ? "" : StringUtils.genderizeCreature(creature.gender(), creature.name(), false))
								+"no soporta el peso "+(StringUtils.genderizeCreature(itemToDrop.gender(), itemToDrop.name(), true)));
						creature.drop(itemToDrop, "");
					}
				}
			}
			public void onFinish(Creature creature){
				creature.modifyAttackSpeed(-Constants.BROKEN_ARM_PENALTY);
			}
		});
		//******************BLUNT 4 LEG**********************************
		TYPES.put("BLUNT4-pierna",new Wound(Constants.INCURABLE,4,"fractura expuesta"){
			public void onApply(Creature creature, Creature applier){
				creature.modifyMovementSpeed(Constants.BROKEN_LEG_PENALTY);
				applier.doAction("impacta con fuerza, fracturando "+
						(creature.isPlayer() ? "tu pierna" : 
							StringUtils.genderizeBodyPosition(this.bodyPart().position(), null) + " " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true)) + ", exponiendo un hueso ensangrentado");
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.modifyMovementSpeed(-Constants.BROKEN_LEG_PENALTY);
			}
		});
		//******************BLUNT 4 HEAD*********************************
		TYPES.put("BLUNT4-cabeza",new Wound(Constants.INCURABLE,4,"traumatismo"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("impacta con fuerza en " + 
						(creature.isPlayer() ? "tu cabeza" : "la cabeza " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true))
						+ " generando un severo traumatismo");
			}
			public void update(Creature creature){
				if(Math.random() > Constants.HEAD_TRAUMA_WANDER_CHANCE){
					creature.getCreatureAi().wander();
					creature.doAction("esta deshorientado");
				}
			}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 4 BACK*********************************
		TYPES.put("BLUNT4-espalda",new Wound(Constants.INCURABLE,4,"fractura vertebral"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra golpear con fuerza "+
						(creature.isPlayer() ? "tu espalda" : 
							"la espalda " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true)) + ", fracturando algunos huesos");
			}
			public void update(Creature creature){
				if(Math.random() > Constants.BACK_TRAUMA_SKIP_CHANCE){
					creature.modifyActionPoints(-Constants.BACK_TRAUMA_PENALTY, "muy adolorido por sus fracturas en la espalda");
				}
			}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 5 KO HEAD******************************
		TYPES.put("BLUNT5-cabeza",new Wound(Constants.INCURABLE,5,"craneo destrozado"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction(Constants.MESSAGE_KILL_COLOR, "destruye " + 
			(applier.isPlayer() ? StringUtils.genderizeBodyPosition(this.bodyPart().position(), null)+ " " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true) : "tu cabeza") + " con el impacto!!!");
				creature.kill("cabeza aplastada", "muere instantaneamente, con "+(creature.isPlayer() ? "tu" : "su") +" craneo destrozado");
				//creature.force_drop(new Item(ItemType.STATIC, (char)248, 'F', creature.color(), "cabeza de "+creature.name()+"", null));
				creature.bleed(2);
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************BLUNT 5 KO CHEST*****************************
		TYPES.put("BLUNT5-pecho",new Wound(3,5,"corazon detenido"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction(Constants.MESSAGE_KILL_COLOR, "golpea con presicion en el pecho " + 
						(!creature.isPlayer() ? StringUtils.genderizeCreature(creature.gender(), creature.name(), true) : "") + ", deteniendo "
						+(creature.isPlayer() ? "tu" : "su") +" corazon!!!");
			}
			public void update(Creature creature){
				creature.getCreatureAi().wander();
				creature.doAction("trastabilla, oprimiendo "+(creature.isPlayer() ? "tu": "su") + " pecho");
			}
			public void onFinish(Creature creature){
				creature.kill("corazon detenido", "muere en agonia, oprimiendo su corazon");
			}
		});
		//******************BLUNT 5 KO BACK******************************
		TYPES.put("BLUNT5-espalda",new Wound(6,5,"columna destrozada"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction(Constants.MESSAGE_KILL_COLOR, "impacta con fuerza en el centro de la columna " + 
						(creature.isPlayer() ? "dejandote paralitico " : "paralizando "+ (creature.gender() == 'M' ? "al " : "a la ") + creature.name() )
							 +"!!!");
				creature.modifyActionPoints(-9999, "completamente paralitico");
			}
			public void update(Creature creature){
				if(Math.random() > 0.6)
					creature.doAction("grita paralizado en completa agonia!");
			}
			public void onFinish(Creature creature){
				creature.resetActionPoints();
				creature.kill("paralisis", "perece por el insoportable dolor");
			}
		});
		//******************BLUNT 5 KO ARM*******************************
		TYPES.put("BLUNT5-brazo",new Wound(Constants.INCURABLE,5,"brazo fracturado"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction(Constants.MESSAGE_KILL_COLOR, "impacta con fuerza en el brazo fracturandolo y exponiendo el hueso!!!");
				creature.removeBodyPart("brazo");
				creature.bleed(1);
				creature.resetActionPoints();
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.doAction("observa incredulo el milagro...");
			}
		});
		//******************BLUNT 5 KO LEG*******************************
		TYPES.put("BLUNT5-pierna",new Wound(Constants.INCURABLE,5,"pierna fracturada"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction(Constants.MESSAGE_KILL_COLOR, "impacta con fuerza en la rodilla, exponiendo el hueso de la pierna!!!");
				creature.removeBodyPart("pierna");
				creature.bleed(1);
				creature.resetActionPoints();
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.doAction("observa incredulo el milagro...");
			}
		});
		//******************SLICE 1**************************************
		TYPES.put("SLICE1-ANY",new Wound(Constants.LVL1_DURATION,1,"raspadura"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra causar una ligera raspadura en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
		//******************PIERCING 1**************************************
		TYPES.put("PIERCING1-ANY",new Wound(Constants.LVL1_DURATION,1,"tajo"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("roza " + (creature.intrinsicArmor().gender() == 'M' ? "el " : "la ") +
						creature.intrinsicArmor().name() + " "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), "de"));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});
	}
	
	protected int duration;
	public int duration() { return duration; }
	protected int orignalDuration;
	
	public boolean isHealed() { return duration < 0 ? false : duration < 1; }
	
	private int severity;
	public int severity() { return severity; }
	
	private String name;
	public String name() { return name; }
	
	private BodyPart bodyPart;
	public BodyPart bodyPart() { return bodyPart; }
	public Wound setBodyPart(BodyPart position) { this.bodyPart = position; return this; }
	
	private Wound reference;
	
	public Wound(int duration, int severity, String name){
		this.duration = duration;
		this.severity = severity;
		this.name = name;
	}
	
	public Wound(Wound other){
		this.duration = other.duration();
		this.name = other.name();
		this.severity = other.severity();
		this.bodyPart = other.bodyPart();
		this.reference = other;
	}
	
	public void update(Creature creature){
		if(duration > 0)
			duration--;
		
		if(reference != null)
			reference.update(creature);
	}
	
	public void onApply(Creature creature, Creature applier){
		if(reference != null)
			reference.onApply(creature, applier);
	}
	
	public void onFinish(Creature creature){
		if(reference != null)
			reference.onFinish(creature);
	}
}
