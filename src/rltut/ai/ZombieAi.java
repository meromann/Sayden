package rltut.ai;

import rltut.Creature;

public class ZombieAi extends CreatureAi {
	private Creature player;
	
	public ZombieAi(Creature creature, Creature player) {
		super(creature);
		this.player = player;
		creature.x = player.x - 1;
		creature.y = player.y;
	}

	public void onUpdate(){
		if (creature.canSee(player.x, player.y, player.z))
			hunt(player);
		else
			wander();
	}
}
