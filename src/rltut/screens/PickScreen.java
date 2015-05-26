package rltut.screens;

import rltut.Creature;
import rltut.Item;

public class PickScreen extends InventoryBasedScreen {

	public PickScreen(Creature player) {
		super(player);
	}

	@Override
	protected String getVerb() { 
		return "elegir"; 
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
		return "ELEGIR";
	}
}
