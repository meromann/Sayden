package rltut.ai;

import java.awt.Color;
import java.util.HashMap;

import rltut.Point;
import rltut.BodyPart;
import rltut.Constants;
import rltut.Creature;
import rltut.DamageType;
import rltut.Item;
import rltut.StringUtils;
import rltut.Wound;
import rltut.Item.ItemType;

public class WolfAi extends CreatureAi {
	private Creature player;
	
	public WolfAi(Creature creature, Creature player) {
		super(creature);
		this.player = player;
		this.modifyDesire(0, StringUtils.randInt(0, 100));
		this.modifyDesire(1, StringUtils.randInt(0, 80));
		
		if(Math.random() < 1){
			setFlag("IsHungry", true);
			creature.modifyColor(Color.RED);
		}
	}
	
	public Wound getWoundAttack(DamageType type, BodyPart bodyPart, Creature target) { 
		if(bodyPart.position() == BodyPart.HEAD.position() && !target.hasWound("Mordida a la yugular"))
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
		else
			return null;
	}
	
	private void propagate(int desire, int amount){
		for(Creature c : creature.getCreaturesWhoSeeMe()){
			if(c.name().indexOf("lobo") != -1)
				c.ai().modifyDesire(desire, amount);
		}
	}
	
	public void onAttack(Creature target){
		if(player.hp() <= creature.maxHp()){
			propagate(0, 10);
		}else{
			propagate(1, 10);
		}
	}
	
	public void onGetAttacked(Creature attacker){
		if(player.hp() > creature.hp()){
			propagate(1, 10);
		}else{
			propagate(0, 10);
		}
	}
	
	public void onUpdate(){
		if(getFlag("IsHungry")){
			HashMap<Item, Point> items = creature.getItemsArroundMe();
			
			if(!items.isEmpty()){
				for(Item i : items.keySet()){
					if(i.itemType() == ItemType.EDIBLE)
						moveTo(items.get(i));
				}
			}
			if(creature.canSee(player.x, player.y, player.z)){
				hunt(player);
			}else{
				wander();
			}
		}
		if(canSee(player.x, player.y, player.z) &&
				(Point.distance(creature.x, creature.y, player.x, player.y) <= 3 
					|| getDesire(0) > getDesire(1))){
			hunt(player);
		}else if(canSee(player.x, player.y, player.z) && 
				getDesire(1) > getDesire(0)){
			flee(player);
		}else{
			wander();
		}
	}
}
