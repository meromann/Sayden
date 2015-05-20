package rltut.screens;

import rltut.Item;

public class Option {
	private String description;
	public String description() { return description; }
	
	private Screen selectScreen;
	public Screen selectScreen() { return selectScreen; }
	
	private Item item;
	public Item item() { return item; }
	public void setItem(Item item) { this.item = item; }
	
	public Option(String description, Screen returnScreen){
		this.description = description;
		this.selectScreen = returnScreen;
	}
}
