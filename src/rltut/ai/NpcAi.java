package rltut.ai;

import java.awt.Color;
import java.util.List;

import rltut.Creature;
import rltut.Message;
import rltut.TileData;

public class NpcAi extends CreatureAi {
	
	private List<Message> introduction;
	public NpcAi setIntroduction(List<Message> introduction) { this.introduction = introduction; return this; }
	
	private List<Message> greetings;
	public NpcAi setGreetings(List<Message> greetings) { this.greetings = greetings; return this; }
	private int lastGreetingMessage = -1;
	
	private TileData permittedTile;
	private String name;
	private String job;
	private Color talkColor;
	private boolean introduced = false;
	
	public NpcAi(Creature creature, String name, String job, Color talkColor) {
		super(creature);
		this.name = name;
		this.job = job;
		this.talkColor = talkColor;
	}
	
	public void setPermittedTile(TileData tile){
		this.permittedTile = tile;
	}
	
	public void onTalkedTo(Creature talker){
		if(!introduced){
			Color temp_color = introduction.get(0).color() != talkColor ? introduction.get(0).color() : talkColor;
			creature.talkAction(temp_color, "[%s - %s] %s", name, job, introduction.get(0).message());
			introduction.remove(0);
			
			if(introduction.isEmpty()){
				introduced = true;
				introduction.clear();
			}
			
		}else{
			
			int randomMsg = 0;
			
			do {
				randomMsg = (int)(Math.random() * (greetings.size()) + 1);
			} 
			while (randomMsg != lastGreetingMessage);
			
			Color temp_color = greetings.get(randomMsg).color() != talkColor ? greetings.get(randomMsg).color() : talkColor;
			lastGreetingMessage = randomMsg;
			creature.talkAction(temp_color, "[%s - %s] %s", name, job, greetings.get(randomMsg).message());
		}
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
