package rltut;

import java.util.ArrayList;
import java.util.Collections;

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
	
	private String description;
	public String description() { return description; }
	
	public static Wound getWound(DamageType type, BodyPart bodyPart, Creature target) {
		ArrayList<Wound> possibleWounds = new ArrayList<Wound>();
		
		if(type.wondType() == DamageType.BLUNT.wondType()){
			//Deshorientacion
			possibleWounds.add(new Wound("Deshorientacion","[-2 vision | 20 penalizador presicion]", Constants.WOUND_DURATION_LOW, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR,"El impacto %s!", StringUtils.formatTextToGender("deshorienta", creature));
					creature.notify(Constants.WOUND_COLOR,"[-2 vision | 20 penalizador presicion]");
					creature.modifyVisionRadius(-2);
					creature.modifyMissChance(20);
				}
				public void onFinish(Creature creature){
					creature.modifyVisionRadius(2);
					creature.modifyMissChance(-20);
				}
			});
			//Adolorido
			if(!target.hasWound("Profundo dolor"))
				possibleWounds.add(new Wound("Profundo dolor","[perder accion | 10% chances perder accion]", Constants.WOUND_DURATION_LOW, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"El impacto %s!", StringUtils.formatTextToGender("genera un dolor agonizante", creature));
						creature.notify(Constants.WOUND_COLOR,"[perder accion | 10%% chances perder accion]");
						creature.resetActionPoints();
						
						if(creature.isPlayer())
							creature.modifyActionPoints(-100, "adolorido");
					}
					public void onBeforeMove(Creature creature){
						if(Math.random() < 0.10){
							creature.resetActionPoints();
							if(creature.isPlayer())
								creature.modifyActionPoints(-100, "adolorido");
						}
					}
					public void onBeforeAttack(Creature creature){
						if(Math.random() < 0.10){
							creature.resetActionPoints();
							if(creature.isPlayer())
								creature.modifyActionPoints(-100, "adolorido");
						}
					}
				});
			//Contusion
			if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Contusion"))
				possibleWounds.add(new Wound("Contusion","[30% chances mareos al moverse]", Constants.WOUND_DURATION_LOW, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"El golpe en la cabeza %s!", StringUtils.formatTextToGender("genera una contusion", creature));
						creature.notify(Constants.WOUND_COLOR,"[30%% chances mareos al moverse]");
					}
					public void onBeforeMove(Creature creature){
						if(Math.random() < 0.3){
							creature.getCreatureAi().wander();
							creature.notify("Estas mareado!");
						}
					}
				});
			//Fractura en brazo
			if(bodyPart.position() == BodyPart.ARMS.position())
				possibleWounds.add(new Wound("Fractura", "[20 penalizador velocidad ataque]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR,"Al golpear %s!", StringUtils.formatTextToGender("fractura el brazo", creature, "sd"));
						creature.notify(Constants.WOUND_COLOR,"[20 penalizador velocidad ataque]");
						creature.modifyAttackSpeed(20);
					}
					public void onFinish(Creature creature){
						creature.modifyAttackSpeed(-20);
					}
				});
			//Fractura en piernas
			if(bodyPart.position() == BodyPart.LEGS.position())
				possibleWounds.add(new Wound("Fractura", "[20 penalizador velocidad movimiento]", Constants.WOUND_DURATION_MID, type, bodyPart){
					public void onApply(Creature creature, Creature applier){
						creature.notifyArround(Constants.WOUND_COLOR, "Al golpear %s!", StringUtils.formatTextToGender("fractura la pierna", creature, "s"));
						creature.notify(Constants.WOUND_COLOR,"[20 penalizador velocidad movimiento]");
						creature.modifyMovementSpeed(20);
					}
					public void onFinish(Creature creature){
						creature.modifyMovementSpeed(-20);
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
	
	public void onBeforeMove(Creature creature){}
	
	public void onApply(Creature creature, Creature applier){
		if(reference != null)
			reference.onApply(creature, applier);
	}
	
	public void onFinish(Creature creature){
		if(reference != null)
			reference.onFinish(creature);
	}
}