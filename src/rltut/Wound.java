package rltut;

import java.util.ArrayList;
import java.util.Collections;

import rltut.Item.ItemType;

public class Wound {
	protected int duration;
	public int duration() { return duration; }
	
	protected int originalDuration;
	public void resetDuration() { this.duration = this.originalDuration; }
	
	public boolean isHealed() { return duration < 0 ? false : duration < 1; }
	
	private DamageType type;
	public DamageType type() { return type; }
	
	private BodyPart bodyPart;
	public BodyPart bodyPart() { return bodyPart; }
	public Wound setBodyPart(BodyPart position) { this.bodyPart = position; return this; }
	
	private Wound reference;
	
	private String name;
	public String name() { return name; }
	public void modifyName(String newName) { this.name = newName; }
	
	private String description;
	public String description() { return description; }
	
	public static Wound getWound(DamageType type, BodyPart bodyPart, Creature target) {
		ArrayList<Wound> possibleWounds = new ArrayList<Wound>();
		
		if(type.wondType() == DamageType.PIERCING.wondType()){
			possibleWounds.add(new Wound("Estocada", "[bonus de herida por estocada]", 2, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					if(!creature.hasWound("Estocada"))
						creature.getBodyPart(this.bodyPart().position()).setPiercingLvl(0);
					
					int piercingLvl = creature.getBodyPart(this.bodyPart().position()).piercingLvl() + 1;
					creature.notifyArround(Constants.WOUND_COLOR, "Slash! %s%s con un rapido movimiento! (x%s)", 
							applier.isPlayer() ? "Impactas" : StringUtils.capitalize(StringUtils.genderizeCreature(applier.gender(), applier.name(), false)),
									creature.isPlayer() ? " te impacta" : StringUtils.formatTextToGender("", creature), piercingLvl);
					creature.notify(Constants.WOUND_COLOR,"["+piercingLvl+" bonus de herida por estocada]");
					creature.modifyHp(-piercingLvl, "Mueres por multiples estocadas al cuerpo");
					creature.getBodyPart(this.bodyPart().position()).setPiercingLvl(piercingLvl);
				}
			});
			if(bodyPart.position() == BodyPart.HEAD.position())
				possibleWounds.add(new Wound("Filo al ojo", "[reduccion vision]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "Con un movimiento descendente %s!", 
								applier.isPlayer() ? "hundes tu arma en el ojo " + StringUtils.genderizeCreature(creature.gender(), creature.name(), true) : StringUtils.genderizeCreature(applier.gender(), applier.name(), true) + " recibes un impacto en el ojo!");
						creature.notify(Constants.WOUND_COLOR,"[reduccion vision]");
						creature.modifyVisionRadius(-8);

					}
					public void onFinish(Creature creature){
						creature.modifyVisionRadius(8);
					}
				});
			if(bodyPart.position() == BodyPart.BACK.position())
				possibleWounds.add(new Wound("Estoque a la nuca", "[5 puntos de herida]", 1, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "Tomando%s del pelo %s craneo!", 
								creature.isPlayer() ? "te" : StringUtils.formatTextToGender("", creature), applier.isPlayer() ? "atraviezas su" : "atraviesas tu");
						creature.notify(Constants.WOUND_COLOR,"[5 puntos de herida]");
						creature.modifyHp(-5, "Mueres al ser perforado en la nuca, atravesando el craneo");

					}
				});
			if(bodyPart.position() == BodyPart.CHEST.position())
				possibleWounds.add(new Wound("Estoque al corazon", "[5 puntos de herida]", 1, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "Con un rapido movimiento"+
								(applier.isPlayer() ? StringUtils.genderizeCreature(creature.gender(), creature.name(), false) : "")
								+" hunde!");
						creature.notify(Constants.WOUND_COLOR,"[5 puntos de herida]");
						creature.modifyHp(-5, "Mueres al ser perforado en el corazon");
					}
				});
		}
		
		if(type.wondType() == DamageType.SLICE.wondType()){
			possibleWounds.add(new Wound("Corte superficial", "[sangrado por 1 punto]", 1, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR, "Con un movimiento impreciso %s!", StringUtils.formatTextToGender("inflije un corte", creature, "s"));
					creature.notify(Constants.WOUND_COLOR,"[sangrado por 1 punto]");
				}
				public void update(Creature creature){
					super.update(creature);
					creature.bleed(1);
					creature.modifyHp(-1, "Mueres desangrado");
				}
			});
			possibleWounds.add(new Wound("Corte peligroso", "[sangrado por 2 puntos]", 2, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR, "Con un movimiento ascendente %s!", StringUtils.formatTextToGender("inflije un corte", creature, "s"));
					creature.notify(Constants.WOUND_COLOR,"[sangrado por 2 puntos]");
				}
				public void update(Creature creature){
					super.update(creature);
					creature.bleed(1);
					creature.modifyHp(-1, "Mueres desangrado");
				}
			});
			possibleWounds.add(new Wound("Corte profundo", "[sangrado por 3 puntos]", 3, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR, "Con un movimiento elegante y preciso %s!", StringUtils.formatTextToGender("inflije un profundo corte", creature, "s"));
					creature.notify(Constants.WOUND_COLOR,"[sangrado por 3 puntos]");
				}
				public void update(Creature creature){
					super.update(creature);
					creature.bleed(1);
					creature.modifyHp(-1, "Mueres desangrado");
				}
			});
			
			if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Corte en la cien"))
				possibleWounds.add(new Wound("Corte en la cien", "[enceguecimiento]", Constants.WOUND_DURATION_LOW, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "El ataque genera un corte en %s y la sangre cae hacia los ojos!", 
								StringUtils.formatTextToGender("cabeza", creature, "td"));
						creature.notify(Constants.WOUND_COLOR,"[enceguecimiento]");
						creature.modifyVisionRadius(-20);
					}
					public void onFinish(Creature creature){
						creature.modifyVisionRadius(20);
					}
				});
			if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Corte al cuello"))
				possibleWounds.add(new Wound("Corte al cuello", "[3 puntos de herida y 3 desangre]", 3, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "Con un peligroso movimiento el ataque alcanza %s!", 
								StringUtils.formatTextToGender("cuello", creature, "td"));
						creature.notify(Constants.WOUND_COLOR,"[3 puntos de herida y 3 desangre]");
						creature.modifyHp(-3, "Tu cabeza cae rodando al suelo");
					}
					public void onFinish(Creature creature){
						creature.bleed(1);
						creature.modifyHp(-1, "Mueres desangrado");
					}
				});
			
			if(bodyPart.position() == BodyPart.ARMS.position() && target.hasBodyPart(BodyPart.IZQ_ARM.position()))
				possibleWounds.add(new Wound("Brazo amputado", "[brazo habil amputado]", Constants.WOUND_PERMANENT, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "El ataque es certero y separa %s!", 
								creature.isPlayer() ? "tu brazo":"el brazo "+ StringUtils.genderizeCreature(creature.gender(), creature.name(),false));
						creature.notify(Constants.WOUND_COLOR,"[brazo habil amputado]");
						creature.force_drop(new Item(ItemType.STATIC,(char)191, 'M', creature.color(), "brazo humano separado", null));
						creature.removeBodyPart(creature.getBodyPart(BodyPart.IZQ_ARM.position()));
					}
				});
			
			if(bodyPart.position() == BodyPart.ARMS.position() && target.hasBodyPart(BodyPart.DER_ARM.position()))
				possibleWounds.add(new Wound("Brazo amputado", "[brazo amputado]", Constants.WOUND_PERMANENT, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "El ataque es certero y separa %s!", 
								creature.isPlayer() ? "tu brazo":"el brazo "+ StringUtils.genderizeCreature(creature.gender(), creature.name(),false));
						creature.notify(Constants.WOUND_COLOR,"[brazo amputado]");
						creature.force_drop(new Item(ItemType.STATIC,(char)218, 'M', creature.color(), "brazo humano separado", null));
						creature.removeBodyPart(creature.getBodyPart(BodyPart.DER_ARM.position()));
					}
				});
			
			if(bodyPart.position() == BodyPart.LEGS.position() && target.hasBodyPart(BodyPart.LEGS.position()))
				possibleWounds.add(new Wound("Pierna amputada", "[amputas la pierna]", Constants.WOUND_PERMANENT, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "El ataque es certero y separa %s!", 
								creature.isPlayer() ? "tu pierna":"la pierna "+ StringUtils.genderizeCreature(creature.gender(), creature.name(),false));
						creature.notify(Constants.WOUND_COLOR,"[amputas la pierna]");
						creature.force_drop(new Item(ItemType.STATIC,Math.random() < 0.5 ? (char)192 : (char)217, 'F', creature.color(), "pierna humana separada", null));
						creature.removeBodyPart(creature.getBodyPart(BodyPart.LEGS.position()));
					}
				});
		}
		
		if(type.wondType() == DamageType.BLUNT.wondType()){
			//Hueso roto
			possibleWounds.add(new Wound("Hueso roto","[-1 vida maxima]", Constants.WOUND_DURATION_HIGH, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR,"El impacto %s!", StringUtils.formatTextToGender("rompe un hueso", creature));
					creature.notify(Constants.WOUND_COLOR,"[-1 vida maxima]");
					creature.modifyMaxHp(-1);
				}
				public void onFinish(Creature creature){
					creature.modifyMaxHp(1);
				}
			});
			//Contusion
			possibleWounds.add(new Wound("Contusion","[impacto produce dolor]", Constants.WOUND_DURATION_HIGH, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR,"El golpe %s!", StringUtils.formatTextToGender("produce una contusion severa", creature));
					creature.notify(Constants.WOUND_COLOR,"[impacto produce dolor]");
					creature.modifyActionPoints(-100, "adolorido");
				}
			});
			//Mareos
			if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Mareado"))
				possibleWounds.add(new Wound("Mareado","[mareos severos al moverse]", Constants.WOUND_DURATION_LOW, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"El golpe en la cabeza %s!", StringUtils.formatTextToGender("produce severos mareos", creature));
						creature.notify(Constants.WOUND_COLOR,"[mareos severos al moverse]");
					}
					public void onBeforeMove(Creature creature){
						creature.getCreatureAi().wander();
						creature.notify("Estas mareado!");
					}
				});
			//Creaneo destrozado
			if(bodyPart.position() == BodyPart.HEAD.position())
				possibleWounds.add(new Wound("Craneo destrozado","[3 puntos de herida y -2 vida maxima]", Constants.WOUND_PERMANENT, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Con un gran impacto en la cabeza %s!", StringUtils.formatTextToGender("provoca una fisura", creature));
						creature.notify(Constants.WOUND_COLOR,"[3 puntos de herida y -2 vida maxima]");
						creature.modifyHp(-3, "Craneo destrozado por un impacto contundente");
						creature.modifyMaxHp(-2);
					}
					public void onFinish(Creature creature){
						creature.modifyMaxHp(2);
					}
				});
			//Contusion en brazo
			if(bodyPart.position() == BodyPart.ARMS.position())
				possibleWounds.add(new Wound("Contusion en el brazo", "[50 penalizador velocidad ataque]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al golpear %s!", StringUtils.formatTextToGender("provoca una contusion en el brazo", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[50 penalizador velocidad ataque]");
						creature.modifyAttackSpeed(50);
					}
					public void onFinish(Creature creature){
						creature.modifyAttackSpeed(-50);
					}
				});
			//Fractura en brazo habil
			if(bodyPart.position() == BodyPart.ARMS.position() && !target.hasWound("Brazo habil fracturado"))
				possibleWounds.add(new Wound("Brazo habil fracturado", "[suelta el arma al ser golpeado]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al golpear %s!", StringUtils.formatTextToGender("fractura el brazo habil", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[suelta el arma al ser golpeado]");						
					}
					public void onGetAttack(Creature creature, Creature attacker, BodyPart part){
						if(part.position() == BodyPart.ARMS.position())
							creature.drop(creature.weapon(), "");
					}
				});
			//Fractura en brazo secundario
			if(bodyPart.position() == BodyPart.ARMS.position() && !target.hasWound("Brazo tosco fracturado"))
				possibleWounds.add(new Wound("Brazo tosco fracturado", "[suelta el escudo al ser golpeado]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al golpear %s!", StringUtils.formatTextToGender("fractura el brazo tosco", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[suelta el escudo al ser golpeado]");						
					}
					public void onGetAttack(Creature creature, Creature attacker, BodyPart part){
						if(part.position() == BodyPart.ARMS.position())
							creature.drop(creature.shield(), "");
					}
				});
			//Fractura en piernas
			if(bodyPart.position() == BodyPart.LEGS.position())
				possibleWounds.add(new Wound("Contusion en la pierna", "[50 penalizador velocidad movimiento]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al golpear %s!", StringUtils.formatTextToGender("provoca una contusion en la pierna", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[50 penalizador velocidad movimiento]");
						creature.modifyMovementSpeed(50);
					}
					public void onFinish(Creature creature){
						creature.modifyMovementSpeed(-50);
					}
				});
			//Rodilla dislocada
			if(bodyPart.position() == BodyPart.LEGS.position())
				possibleWounds.add(new Wound("Rodilla dislocada", "[1 punto de herida al moverse]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al impactar %s!", StringUtils.formatTextToGender("expone el hueso de la rodilla", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[1 punto de herida al moverse]");
					}
					public void onMove(Creature creature){
						creature.modifyHp(-1, "El hueso expuesto de tu pierna fracturada es demasiado, mueres en agonia");
					}
				});
		}
		
		Collections.shuffle(possibleWounds);
		
		if(!possibleWounds.isEmpty())
			return possibleWounds.get(0);
		else
			return null;
	}
	
	public Wound(String name, String description, int duration, DamageType type, BodyPart bodyPart){
		this.duration = duration;
		this.originalDuration = duration;
		this.type = type;
		this.bodyPart = bodyPart;
		this.name = name;
		this.description = description;
	}
	
	public Wound(Wound other){
		this.duration = other.duration();
		this.originalDuration = other.duration();
		this.type = other.type();
		this.bodyPart = other.bodyPart();
		this.reference = other;
	}
	
	public void update(Creature creature){
		if(duration > 0)
			duration--;
		
		if(reference != null)
			reference.update(creature);
	}
	
	public void onBeforeAttack(Creature creature){}
	
	public void onAttack(Creature creature){}
	
	public void onGetAttack(Creature creature, Creature attacker, BodyPart part){}
	
	public void onBeforeMove(Creature creature){}

	public void onMove(Creature creature) {}
	
	public void onApply(Creature creature, Creature applier){
		if(reference != null)
			reference.onApply(creature, applier);
	}
	
	public void onFinish(Creature creature){
		if(reference != null)
			reference.onFinish(creature);
	}
}