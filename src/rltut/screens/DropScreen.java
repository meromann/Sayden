package rltut.screens;

import rltut.Constants;
import rltut.Creature;
import rltut.Item;

public class DropScreen extends InventoryBasedScreen {
		
	@Override
	protected Screen leftScreen(){
		int scrollX = Math.max(0, Math.min(player.x - Constants.WORLD_WIDTH / 2, player.world().width() - Constants.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - Constants.WORLD_HEIGHT / 2, player.world().height() - Constants.WORLD_HEIGHT));
		return new ThrowScreen(player, player.x - scrollX, player.y - scrollY);
	}
	
	@Override
	protected Screen rightScreen(){
		return new EquipScreen(player);
	}
	
	public DropScreen(Creature player) {
		super(player);
	}

	@Override
	protected String getVerb() { 
		return "soltar"; 
	}

	@Override
	protected boolean isAcceptable(Item item) { 
		return true; 
	}
	
	@Override
	protected Screen use(Item item) { 
		player.drop(item); 
		return null;
	}

	@Override
	public String getScreenName() {
		return "SOLTAR";
	}
}
