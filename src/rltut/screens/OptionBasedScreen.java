package rltut.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import rltut.Constants;
import rltut.Creature;
import asciiPanel.AsciiPanel;

public class OptionBasedScreen implements Screen {

	protected Creature player;
	protected String letters;
	
	public OptionBasedScreen(Creature player){
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public void displayOutput(AsciiPanel terminal) {
		ArrayList<Option> lines = player.options();
		int y = Constants.MENU_OFFSET - lines.size();
		int x = 4;

		if (lines.size() > 0)
			terminal.clear(' ', x, y, 20, lines.size());
		
		for (int i = 0; i < lines.size(); i++){
			Option line = lines.get(i);
			terminal.write(letters.charAt(i) + "-" +line.description(), x, y++);
		}
		
		terminal.clear(' ', 0, Constants.MENU_OFFSET, 20, 1);
		terminal.write("Cual es tu desicion?", 2, Constants.MENU_OFFSET);
		
		terminal.repaint();
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		ArrayList<Option> options = player.options();
		
		if (letters.indexOf(c) > -1 
				&& options.size() > letters.indexOf(c)
				&& options.get(letters.indexOf(c)) != null) {
			options.get(letters.indexOf(c)).onSelect(player);
			return null;
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else {
			return this;
		}
	}
	@Override
	public String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}
}
