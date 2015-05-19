package rltut.ai;

import java.awt.Color;

import rltut.Creature;
import rltut.TileData;

public class NpcAi extends CreatureAi {

	private TileData permittedTile;
	private String name;
	private Color talkColor;
	private boolean introduced = false;
	
	public NpcAi(Creature creature, String name, Color talkColor) {
		super(creature);
		this.name = name;
		this.talkColor = talkColor;
	}
	
	public void setPermittedTile(TileData tile){
		this.permittedTile = tile;
	}
	
	public void onTalkTo(Creature talker){
		if(introduced){
			creature.talkAction(talkColor, "[%] Hola de nuevo %s!", name, talker.name());
		}else{
			creature.talkAction(talkColor, "[%] Bienvenido %s", name, talker.name());
			introduced = true;
		}
	}
	
	public void onUpdate(){
		if(permittedTile != null){
			wanderOn(permittedTile);
		}else{
			wander();
		}
	}
}
