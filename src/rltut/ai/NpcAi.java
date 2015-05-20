package rltut.ai;

import java.awt.Color;
import java.util.List;

import rltut.Creature;
import rltut.Item;
import rltut.Message;
import rltut.Tile;
import rltut.screens.ShopScreen;

public class NpcAi extends CreatureAi {
	
	private List<Message> introduction;
	public NpcAi setIntroduction(List<Message> introduction) { this.introduction = introduction; return this; }
	
	private List<Message> greetings;
	public NpcAi setGreetings(List<Message> greetings) { this.greetings = greetings; return this; }
		
	private List<Message> goodbye;
	public NpcAi setGoodbye(List<Message> goodbye) { this.goodbye = goodbye; return this; }
	
	private List<Message> onreceive;
	public NpcAi setOnreceive(List<Message> onreceive) { this.onreceive = onreceive; return this; }
	
	private List<Message> ongive;
	public NpcAi setOngive(List<Message> ongive) { this.ongive = ongive; return this; }
	
	private Tile permittedTile;
	private Color talkColor;
	private boolean introduced = false;
	
	public NpcAi(Creature creature, String job, Color talkColor) {
		super(creature);
		creature.setJob(job);
		this.talkColor = talkColor;
	}
	
	public void setPermittedTile(Tile tile){
		this.permittedTile = tile;
	}
	
	public void onReceive(Item item, Creature from){
		int randomMsg = (int)(Math.random() * onreceive.size());
		
		Color temp_color = onreceive.get(randomMsg).color() != talkColor ? onreceive.get(randomMsg).color() : talkColor;
		creature.talkAction(temp_color, "(%s): %s", creature.job().toLowerCase(), onreceive.get(randomMsg).message());
	}
	
	public void onGive(Item item, Creature to){
		int randomMsg = (int)(Math.random() * ongive.size());
		
		Color temp_color = ongive.get(randomMsg).color() != talkColor ? ongive.get(randomMsg).color() : talkColor;
		creature.talkAction(temp_color, "(%s): %s", creature.job().toLowerCase(), ongive.get(randomMsg).message());
	}
	
	public void onFarewell(){
		int randomMsg = (int)(Math.random() * goodbye.size());
		
		Color temp_color = goodbye.get(randomMsg).color() != talkColor ? goodbye.get(randomMsg).color() : talkColor;
		creature.talkAction(temp_color, "(%s): %s", creature.job().toLowerCase(), goodbye.get(randomMsg).message());
	}
	
	public void onTalkedTo(Creature talker){
		if(!introduced){
			Color temp_color = introduction.get(0).color() != talkColor ? introduction.get(0).color() : talkColor;
			creature.talkAction(temp_color, "(%s): %s", creature.job().toLowerCase(), introduction.get(0).message());
			introduction.remove(0);
			
			if(introduction.isEmpty()){
				introduced = true;
				introduction.clear();
			}else{
				return;
			}
		}else{
			int randomMsg = (int)Math.random() * greetings.size();
			
			Color temp_color = greetings.get(randomMsg).color() != talkColor ? greetings.get(randomMsg).color() : talkColor;
			creature.talkAction(temp_color, "(%s): %s", creature.job().toLowerCase(), greetings.get(randomMsg).message());
		}
		talker.setShopScreen(new ShopScreen(talker, creature));
	}
	
	public void onUpdate(){
		if(creature.getCreaturesWhoSeeMe().size() < 2){
			if(permittedTile != null){
				wanderOn(permittedTile);
			}else{
				wander();
			}
		}
	}
}
