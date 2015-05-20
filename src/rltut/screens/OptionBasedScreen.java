package rltut.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import rltut.Creature;
import asciiPanel.AsciiPanel;

public abstract class OptionBasedScreen implements Screen {

	protected Creature player;
	protected String letters;
	
	protected abstract String getVerb();
	protected abstract Screen select();
	protected abstract void onLeave();
	protected abstract ArrayList<Option> getOptions();
	
	public OptionBasedScreen(Creature player){
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public void displayOutput(AsciiPanel terminal) {
		ArrayList<Option> lines = getOptions();
		int y = 23 - lines.size();
		int x = 4;

		if (lines.size() > 0)
			terminal.clear(' ', x, y, 20, lines.size());
		
		for (Option line : lines){
			terminal.write(line.description(), x, y++);
		}
		
		terminal.clear(' ', 0, 23, 80, 1);
		terminal.write("Que quieres " + getVerb() + "?", 2, 23);
		
		terminal.repaint();
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		ArrayList<Option> options = getOptions();
		
		if (letters.indexOf(c) > -1 
				&& options.size() > letters.indexOf(c)
				&& options.get(letters.indexOf(c)) != null) {
			return options.get(letters.indexOf(c)).selectScreen();
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			onLeave();
			return null;
		} else {
			return this;
		}
	}
}
