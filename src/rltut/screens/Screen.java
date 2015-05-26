package rltut.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public interface Screen {
	public Screen[] keyMap = new Screen[9];
	
	public String getScreenName();	
	
	public void displayOutput(AsciiPanel terminal);
	
	public Screen respondToUserInput(KeyEvent key);
}
