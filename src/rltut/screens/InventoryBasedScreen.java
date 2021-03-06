package rltut.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import rltut.Constants;
import rltut.Creature;
import rltut.Item;
import asciiPanel.AsciiPanel;

public abstract class InventoryBasedScreen implements Screen {

	protected Screen leftScreen(){ return this; }
	protected Screen rightScreen(){ return this; }
	protected Creature player;
	private String letters;
	
	protected abstract String getVerb();
	protected abstract boolean isAcceptable(Item item);
	protected abstract Screen use(Item item);
	
	public InventoryBasedScreen(Creature player){
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public void displayOutput(AsciiPanel terminal) {
		ArrayList<String> lines = getList();
		
		int y = Constants.MENU_OFFSET - lines.size();
		int x = 4;
		
		for (String line : lines){
			terminal.write(line, x, y++);
		}
		
		terminal.clear(' ', 0, Constants.MENU_OFFSET, 80, 1);
		terminal.write("Que quieres " + getVerb() + "?", 2, Constants.MENU_OFFSET);
		terminal.writeCenter(leftScreen().getScreenName() + " <--" + this.getScreenName() + "--> " + rightScreen().getScreenName(), Constants.MENU_OFFSET + 1);
		
		terminal.repaint();
	}
	
	private ArrayList<String> getList() {
		ArrayList<String> lines = new ArrayList<String>();
		Item[] inventory = player.inventory().getItems();
		
		for (int i = 0; i < inventory.length; i++){
			Item item = inventory[i];
			
			if (item == null || !isAcceptable(item))
				continue;
			
			String line = letters.charAt(i) + " - " + item.glyph() + " " + player.nameOf(item);
			
			if(item == player.weapon() || item == player.armor() || item == player.helment()
					|| item == player.shield())
				line += " (equipado)";
			
			lines.add(line);
		}
		return lines;
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		Item[] items = player.inventory().getItems();
		
		if (letters.indexOf(c) > -1 
				&& items.length > letters.indexOf(c)
				&& items[letters.indexOf(c)] != null
				&& isAcceptable(items[letters.indexOf(c)])) {
			return use(items[letters.indexOf(c)]);
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else if(key.getKeyCode() == KeyEvent.VK_LEFT){
			return leftScreen();
		} else if(key.getKeyCode() == KeyEvent.VK_RIGHT || key.getKeyCode() == KeyEvent.VK_TAB){
			return rightScreen();
		} else if(key.getKeyChar() >= '1' && key.getKeyChar() <= '9'){
			int mappedKey = Integer.parseInt(key.getKeyChar()+"") - 1;
			for(int i = 0; i < keyMap.length; i++){
				if(keyMap[i] == this){
					keyMap[i] = null;
				}
			}
			keyMap[mappedKey] = this;
			return this;
		} else {
			return this;
		}
	}
}
