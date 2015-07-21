package rltut.ai;

import java.util.List;

import rltut.Creature;
import rltut.FieldOfView;
import rltut.Item;
import rltut.Message;
import rltut.Tile;
import rltut.Wound;

public class PlayerAi extends CreatureAi {

	private List<Message> messages;
	private FieldOfView fov;
	
	public PlayerAi(Creature creature, List<Message> messages, FieldOfView fov) {
		super(creature);
		this.messages = messages;
		this.fov = fov;
	}

	public void updateFow(FieldOfView fov){
		this.fov = fov;
	}
	
	public void onEnter(int x, int y, int z, Tile tile){
		if (tile.isGround()){
			creature.x = x;
			creature.y = y;
			creature.z = z;
			
			Item item = creature.item(creature.x, creature.y, creature.z);
			if (item != null)
				creature.notify("Te topas con " + item.nameUnUna()+ ".");
			
			for (int i = 0; i < creature.wounds().size(); i++){
				Wound wound = creature.wounds().get(i);
				wound.onMove(creature);
			}
		}
		/*} else if (tile.get()) {
			creature.dig(x, y, z);
		}*/
	}
	
	public void onNotify(Message message){
		messages.add(message);
	}
	
	public boolean canSee(int wx, int wy, int wz) {
		return fov.isVisible(wx, wy, wz);
	}
	
	public void onGainLevel(){}
	
	public void onReceive(Item item, Creature from){
		from.doAction("Te entrega el %s", item.name());
	}
	
	public void onGive(Item item, Creature to){
		creature.doAction("Entregas el %s a %s", item.name(), to.name());
	}

	public Tile rememberedTile(int wx, int wy, int wz) {
		return fov.tile(wx, wy, wz);
	}
}
