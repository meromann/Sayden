package rltut.screens;

import rltut.Creature;
import rltut.Item;

public class Option {
	private String description;
	public String description() { return description; }
	
	private Creature creature;
	public Creature creature() { return creature; }
	
	private Item item;
	public Item item() { return item; }
	public void setItem(Item item) { this.item = item; }
	
	public Option(String description, Creature talker){
		this.creature = talker;
		this.description = description;
	}
	
	public void onSelect(Creature player){}
}
