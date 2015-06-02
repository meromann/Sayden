package rltut.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import rltut.Constants;
import rltut.Creature;

public class PreLookScreen extends TargetBasedScreen {
	
	@Override
	protected Screen leftScreen(){
		return new ExamineScreen(player);
	}
	
	@Override
	protected Screen rightScreen(){
		return new EatScreen(player);
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.clear(' ', 0, 23, 80, 1);
		terminal.write("Presiona enter para observar", 2, Constants.MENU_OFFSET);
		terminal.writeCenter(leftScreen().getScreenName() + " <--" + this.getScreenName() + "--> " + rightScreen().getScreenName(), Constants.MENU_OFFSET + 1);
	}	
	
	public PreLookScreen(Creature player, String caption, int sx, int sy) {
		super(player, caption, sx, sy);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		int scrollX = Math.max(0, Math.min(player.x - Constants.WORLD_WIDTH / 2, player.getWorld().width() - Constants.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - Constants.WORLD_HEIGHT / 2, player.getWorld().height() - Constants.WORLD_HEIGHT));

		switch (key.getKeyCode()){
		case KeyEvent.VK_LEFT: return leftScreen();
		case KeyEvent.VK_RIGHT: return rightScreen();
		case KeyEvent.VK_ENTER: return new LookScreen(player, "Observando", player.x - scrollX, player.y - scrollY);
		case KeyEvent.VK_ESCAPE: return null;
		}
		
		if(key.getKeyChar() >= '1' && key.getKeyChar() <= '9'){
			int mappedKey = Integer.parseInt(key.getKeyChar()+"") - 1;
			for(int i = 0; i < keyMap.length; i++){
				if(keyMap[i] == this){
					keyMap[i] = null;
				}
			}
			keyMap[mappedKey] = this;
			return this;
		} 
		
		return this;
	}
	
	@Override
	public String getScreenName() {
		return "OBSERVAR";
	}
}
