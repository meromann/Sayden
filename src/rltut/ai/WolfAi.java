package rltut.ai;

import java.awt.Point;

import rltut.Creature;
import rltut.StringUtils;

public class WolfAi extends CreatureAi {
	private Creature player;
	
	public WolfAi(Creature creature, Creature player) {
		super(creature);
		this.player = player;
		this.modifyDesire(0, StringUtils.randInt(0, 100));
		this.modifyDesire(1, StringUtils.randInt(0, 80));
	}
	
	private void propagate(int desire, int amount){
		for(Creature c : creature.getCreaturesWhoSeeMe()){
			if(c.name().indexOf("lobo") != -1)
				c.getCreatureAi().modifyDesire(desire, amount);
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
