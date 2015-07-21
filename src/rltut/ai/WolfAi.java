package rltut.ai;

import java.awt.Color;
import java.util.HashMap;

import asciiPanel.AsciiPanel;
import rltut.Flags;
import rltut.Point;
import rltut.BodyPart;
import rltut.Creature;
import rltut.DamageType;
import rltut.Item;
import rltut.RPG;
import rltut.StringUtils;
import rltut.StuffFactory;
import rltut.Wound;

public class WolfAi extends CreatureAi {
	private Creature player;
	private StuffFactory factory;
	
	private int hunger = 0;
	private int growTime = 400;
	
	public WolfAi(Creature creature, Creature player, StuffFactory factory) {
		super(creature);
		this.player = player;
		this.factory = factory;
		this.modifyDesire(0, StringUtils.randInt(0, 100));
		this.modifyDesire(1, StringUtils.randInt(0, 80));
		
		if(Math.random() < 1){
			setFlag("IsHungry", true);
			creature.setData(RPG.COLOR, Color.RED);
			creature.setName(creature.name() + " hambriento");
			hunger = 10;
			return;
		}
		
		creature.modifyAttackSpeed(100);
		
		if(Math.random() < 0.15){
			setFlag("IsCub", true);
			creature.setData(RPG.COLOR, AsciiPanel.brightBlue);
			creature.setName("lobezno");
			modifyDesire(1, 50);
			return;
		}
				
		if(Math.random() < 0.15){
			setFlag("IsPregnant", true);
			creature.setData(RPG.COLOR, AsciiPanel.cyan);
			creature.setName("loba");
			creature.modifyMovementSpeed(100);
			modifyDesire(1, 50);
			return;
		}
	}
	
	public Wound getWound(DamageType type, BodyPart bodyPart, Creature target) {
		/*if(bodyPart.position() == BodyPart.ARMS.position()
				&& type.wondType() == DamageType.SLICE.wondType()){
			return new Wound("Pierna amputada", "pierna amputada", Constants.WOUND_PERMANENT, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR, "El ataque es certero y separa %s!", 
							creature.isPlayer() ? "tu pierna":"la pierna "+ StringUtils.genderizeCreature(creature.gender(), creature.name(),false));
					creature.notify(Constants.WOUND_COLOR,"[amputas la pierna]");
					//creature.force_drop(new Item(ItemType.STATIC,Math.random() < 0.5 ? (char)192 : (char)217, 'F', creature.color(), "pierna de lobo separada", null));
					creature.removeBodyPart(creature.getBodyPart(BodyPart.LEGS.position()));
				}
			};
		}*/
		return null;
	}
	
	public Wound getWoundAttack(DamageType type, BodyPart bodyPart, Creature target) { 
		/*if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Mordida a la yugular"))
			return new Wound("Mordida a la yugular", "sangrado al moverse", 10, type, bodyPart){
				public void onApply(Creature creature, Creature applier){
					creature.notifyArround(Constants.WOUND_COLOR, "El lobo logra morder tu yugular, inflingiendo un horrible corte!");
					creature.notifyArround(Constants.WOUND_COLOR, "Logras detener el sangrado aplicando presion...");
					creature.notify(Constants.WOUND_COLOR,"[sangrado al moverse]");
				}
				public void onMove(Creature creature){
					creature.bleed(1);
					creature.notify(Constants.WOUND_COLOR, "Al moverte no puedes atender tu herida en el cuello!");
					creature.modifyHp(-1, "Mueres desangrado");
				}
			};
		else*/
			return null;
	}
		
	public void onAttack(Creature target){
		if(player.hp() <= creature.maxHp()){
			propagate(0, 10);
		}else{
			propagate(1, 10);
		}
	}
	
	public void onGetAttacked(Creature attacker){
		if(getFlag("IsPregnant")){
			propagate(0, 80);
			creature.doAction("aulla de dolor y panico, con terror en su mirada y un bebe en el vientre...");
			return;
		}
		if(getFlag("IsCub")){
			propagate(0, 50);
			creature.doAction("gime desamparado, con terror en su mirada");
			return;
		}
		if(player.hp() > creature.hp()){
			propagate(1, 10);
		}else{
			propagate(0, 10);
		}
	}
	
	public void onUpdate(){
		if(getFlag("IsCub")){
			if(creature.canSee(player.x, player.y, player.z)){
				flee(player);
				return;
			}else{
				growTime--;
				
				if(growTime < 1){
					setFlag("IsCub", false);
					creature.setData(RPG.COLOR, creature.getData(RPG.ORIGINAL_COLOR));
					creature.setName(creature.originalName());
				}
				wander();
				return;
			}
		}
		if(getFlag("IsPregnant")){
			if(creature.canSee(player.x, player.y, player.z)){
				flee(player);
				return;
			}else{
				growTime--;
				
				if(growTime < 1){
					factory.newMaleWolf(creature.z, player);
				}
				
				wander();
				return;
			}
		}
		if(getFlag("IsHungry")){
			Item check = creature.item(creature.x, creature.y, creature.z);

			if(check != null
					&& check.getBooleanData(Flags.IS_EDIBLE)){
								
				if(Point.distance(creature.x, creature.y, player.x, player.y) < 2){
					hunt(player);
					return;
				}else if(Point.distance(creature.x, creature.y, player.x, player.y) <= 3){
					creature.doAction("eriza su pelo y te observa de reojo...");
				}
				
				creature.doAction("consume avidamente %s", check.nameElLa());
				
				hunger--;
				
				if(hunger < 1){
					setFlag("IsHungry", false);
					creature.setData(RPG.COLOR, creature.getData(RPG.ORIGINAL_COLOR));
					creature.world().remove(creature.x, creature.y, creature.z);
					creature.setName(creature.originalName());
				}
			}

			HashMap<Item, Point> items = creature.getItemsArroundMe();
			
			if(!items.isEmpty()){
				for(Item i : items.keySet()){
					if(i.getBooleanData(Flags.IS_EDIBLE))
						moveTo(items.get(i));
				}
			}

			if(creature.canSee(player.x, player.y, player.z)){
				hunt(player);
				return;
			}else{
				wander();
				return;
			}
		}
		if(canSee(player.x, player.y, player.z) &&
				(Point.distance(creature.x, creature.y, player.x, player.y) <= 3 
					|| getDesire(0) > getDesire(1))){
			hunt(player);
			return;
		}else if(canSee(player.x, player.y, player.z) && 
				getDesire(1) > getDesire(0)){
			flee(player);
			return;
		}else{
			wander();
			return;
		}
	}
}
