package rltut.ai;

import rltut.Creature;
import rltut.StuffFactory;

public class FungusAi extends CreatureAi {
	private StuffFactory factory;
	private int spreadcount;
	public int spreadlimit = 5;
	
	public FungusAi(Creature creature, StuffFactory factory) {
		super(creature);
		this.factory = factory;
	}
	
	public FungusAi(Creature creature, StuffFactory factory, int spreadlimit) {
		super(creature);
		this.factory = factory;
		this.spreadlimit = spreadlimit;
	}

	public void onUpdate(){
		if (spreadcount < spreadlimit && Math.random() < 0.01)
			return;
			//spread();
	}
	
	/*private void spread(){
		int x = creature.x + (int)(Math.random() * 11) - 5;
		int y = creature.y + (int)(Math.random() * 11) - 5;
		
		if (!creature.canEnter(x, y, creature.z))
			return;
		
		creature.doAction("genera otro hongo");
		
		Creature child = factory.newFungus(creature.z, 3);
		child.x = x;
		child.y = y;
		child.z = creature.z;
		spreadcount++;
	}*/
}
