package rltut;

import java.util.HashMap;
import java.util.Map;

public class Wound {
	public static Map<String, Wound> TYPES;
	
	private static Wound BLEED = new Wound(Constants.INCURABLE, 0, "desangre",
			"La perdida de sangre no es cosa leve, de continuar sufres el severo de morir desangrado"){
		public void onApply(Creature creature, Creature applier){
			creature.bleed(this.severity());
		}
	};
	private static Wound INFECTED = new Wound(Constants.INCURABLE, 0, "infeccion",
			"Una infeccion es siempre algo de temer, puede causar mareos, cansancio y en el peor de los casos simplemente la muerte"){
		public void onApply(Creature creature, Creature applier){
			creature.doAction(Constants.SICK_COLOR, "Tu herida comienza a arder, te sientes ligeramente febril...");
		}
	};
	
	public static void instantiateWounds(){
		TYPES = new HashMap<String, Wound>();
		//******************BLUNT 1**************************************
		TYPES.put("BLUNT1-ANY",new Wound(Constants.LVL1_DURATION,1,"moreton",
				"Un leve moreton en tu %s, esta herida no tiene importancia aunque no es recomendable aplicarle presion..."){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea inflingiendo un moreton en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("El moreton de tu "+this.bodyPart().position()+" ya no es una molestia");
			}
		});
		//******************SLICE 1**************************************
		TYPES.put("SLICE1-ANY",new Wound(Constants.LVL1_DURATION,1,"raspadura",
				"Una ligera raspadura en tu $s. Aparte de un ligero ardor no causa mayor molestia"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra causar una ligera raspadura en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("La raspadura de tu "+this.bodyPart().position()+" cicatriza");
			}
		});
		//******************PIERCING 1***********************************
		TYPES.put("PIERCING1-ANY",new Wound(Constants.LVL1_DURATION,1,"tajo",
				"Un leve tajo en tu %s, molesto pero sin importancia"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("roza " + (creature.isPlayer() ? "tu piel" : "la carne "+StringUtils.genderizeCreature(creature.gender(), creature.name(), true)));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("El tajo de tu "+this.bodyPart().position()+" cicatriza");
			}
		});
		//******************BLUNT 2**************************************
		TYPES.put("BLUNT2-ANY",new Wound(Constants.LVL2_DURATION,2,"contusion",
				"Una contusion morada y sanguiolienta en tu %s. De no ser cuidadoso podria tornarse en algo mas severo..."){
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
			public void onFinish(Creature creature){
				creature.notify("La contusion de tu "+this.bodyPart().position()+" se desinflama");
			}
		});
		//******************SLICE 2**************************************
		TYPES.put("SLICE2-ANY",new Wound(Constants.LVL2_DURATION,2,"raspadura",
				"Un corte abierto pero no profundo en tu %s, por suerte no parece infectado y no despide sangre"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra inflingir un corte en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
				if(Math.random() < 0.1){
					modifyDescription("Un corte abierto pero no profundo en tu %s, no parece infectado y por suerte dejo de sangrar");
					creature.notifyArround(Constants.BLOOD_COLOR, "La herida despide un chorro de sangre");
					Wound bleed = new Wound(BLEED, 1, Constants.LVL1_DURATION);
					creature.addWound(bleed, applier);
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("El corte de tu "+this.bodyPart().position()+" cicatriza");
			}
		});
		//******************PIERCING 2***********************************
		TYPES.put("PIERCING2-ANY",new Wound(Constants.LVL2_DURATION,2,"pinchazo",
				"Un pinchazo bastante profundo en tu %s, por suerte no parece estar infectado"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra penetrar " + (creature.isPlayer() ? "tu piel" : "la carne "+StringUtils.genderizeCreature(creature.gender(), creature.name(), true)));
			}
			public void update(Creature creature){
				if(Math.random() < 0.1){
					modifyDescription("Un pinchazo bastante profundo en tu %s, parece estar infectado y despide un hedor nahuseabundo");
					Wound infection = new Wound(INFECTED, 1, Constants.LVL1_DURATION);
					creature.addWound(infection, null);
					resetDuration();
				}
			}
			public void onFinish(Creature creature){
				creature.notify("La apertura de la herida punzante de tu "+this.bodyPart().position()+" se cierra");
			}
		});
		//******************BLUNT 3**************************************
		TYPES.put("BLUNT3-ANY",new Wound(Constants.LVL3_DURATION,3,"fisura",
				"Tienes un hueso fisurado en tu %s, sientes un gran dolor y temes que la herida empeore..."){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("golpea en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null) + " que cruje horriblemente");
				if(Math.random() < 0.4){
					creature.modifyActionPoints(-100, "bajo gran dolor");
					creature.modifyStatusColor(Constants.MESSAGE_STATUS_EFFECT_COLOR);
					creature.doAction(Constants.MESSAGE_STATUS_EFFECT_COLOR, "grita en completa agonia!");
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("Tu "+this.bodyPart().position()+" se deshincha y ya no sientes el dolor de tu fisura");
			}
		});
		//******************SLICE 3**************************************
		TYPES.put("SLICE3-ANY",new Wound(Constants.LVL3_DURATION,3,"raspadura",
				"Una herida profunda y abierta en tu %s, despide un hedor nauseabundo y no parece sangrar"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("logra inflingir un profundo corte en "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), null));
				if(Math.random() < 0.4){
					modifyDescription("Una herida profunda y abierta en tu %s, despide un hedor nauseabundo y un gran chorro de sangre sale de ella");
					creature.notifyArround(Constants.BLOOD_COLOR, "La herida despide un gran chorro de sangre");
					Wound bleed = new Wound(BLEED, 2, Constants.LVL1_DURATION);
					creature.addWound(bleed, applier);
				}
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){
				creature.notify("El corte de tu "+this.bodyPart().position()+" se cura y cierra por completo");
			}
		});
		//******************PIERCING 3***********************************
		TYPES.put("PIERCING3-ANY",new Wound(Constants.LVL3_DURATION,3,"acuchillamiento",
				"Una profunda incision en tu %s que despide sangre y un hedor nausabundo"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("acuchilla " + (creature.isPlayer() ? "tu piel" : "la carne "+ StringUtils.genderizeCreature(creature.gender(), creature.name(), true)));
				if(Math.random() < 0.2){
					creature.notifyArround(Constants.BLOOD_COLOR, "La herida despide un chorro de sangre");
					Wound bleed = new Wound(BLEED, 1, Constants.LVL1_DURATION);
					creature.addWound(bleed, applier);
				}
			}
			public void update(Creature creature){
				if(Math.random() < 0.2){
					Wound infection = new Wound(INFECTED, 1, Constants.LVL1_DURATION);
					creature.addWound(infection, null);
					resetDuration();
				}
			}
			public void onFinish(Creature creature){
				creature.notify("La apertura de la herida punzante de tu "+this.bodyPart().position()+" se cierra");
			}
		});
		/*//******************BLUNT 4 ARM**********************************
		TYPES.put("BLUNT4-"+BodyPart.ARMS,new Wound(Constants.INCURABLE,4,"fractura expuesta"){
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
		TYPES.put("BLUNT4-"+BodyPart.LEGS,new Wound(Constants.INCURABLE,4,"fractura expuesta"){
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
		TYPES.put("BLUNT4-"+BodyPart.HEAD,new Wound(Constants.INCURABLE,4,"traumatismo"){
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
		TYPES.put("BLUNT4-"+BodyPart.BACK,new Wound(Constants.INCURABLE,4,"fractura vertebral"){
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
		TYPES.put("BLUNT5-"+BodyPart.HEAD,new Wound(Constants.INCURABLE,5,"craneo destrozado"){
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
		TYPES.put("BLUNT5-"+BodyPart.CHEST,new Wound(3,5,"corazon detenido"){
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
		TYPES.put("BLUNT5-"+BodyPart.BACK,new Wound(6,5,"columna destrozada"){
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
		TYPES.put("BLUNT5-"+BodyPart.ARMS,new Wound(Constants.INCURABLE,5,"brazo fracturado"){
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
		TYPES.put("BLUNT5-"+BodyPart.LEGS,new Wound(Constants.INCURABLE,5,"pierna fracturada"){
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
		//******************PIERCING 1**************************************
		TYPES.put("PIERCING1-ANY",new Wound(Constants.LVL1_DURATION,1,"tajo"){
			public void onApply(Creature creature, Creature applier){
				applier.doAction("roza " + (creature.intrinsicArmor().gender() == 'M' ? "el " : "la ") +
						creature.intrinsicArmor().name() + " "+StringUtils.genderizeBodyPosition(this.bodyPart().position(), "de"));
			}
			public void update(Creature creature){}
			public void onFinish(Creature creature){}
		});*/
	}
	
	protected int duration;
	public int duration() { return duration; }
	
	protected int originalDuration;
	public void resetDuration() { this.duration = this.originalDuration; }
	
	public boolean isHealed() { return duration < 0 ? false : duration < 1; }
	
	private int severity;
	public int severity() { return severity; }
	
	private String name;
	public String name() { return name; }
	
	private String description;
	public String description() { return description; }
	public void modifyDescription(String modify) { this.description = modify; }
	
	private BodyPart bodyPart;
	public BodyPart bodyPart() { return bodyPart; }
	public Wound setBodyPart(BodyPart position) { this.bodyPart = position; return this; }
	
	private Wound reference;
	
	public Wound(int duration, int severity, String name, String description){
		this.duration = duration;
		this.originalDuration = duration;
		this.severity = severity;
		this.name = name;
		this.description = description;
	}
	
	public Wound(Wound other){
		this.duration = other.duration();
		this.originalDuration = other.duration();
		this.name = other.name();
		this.severity = other.severity();
		this.bodyPart = other.bodyPart();
		this.reference = other;
	}
	
	public Wound(Wound other, int severity, int duration){
		this.duration = duration;
		this.originalDuration = duration;
		this.name = other.name();
		this.severity = severity;
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
