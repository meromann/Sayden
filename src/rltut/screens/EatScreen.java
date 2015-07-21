package rltut.screens;

import rltut.Constants;
import rltut.Creature;
import rltut.Flags;
import rltut.Item;

public class EatScreen extends InventoryBasedScreen {

	@Override
	protected Screen leftScreen(){
		int scrollX = Math.max(0, Math.min(player.x - Constants.WORLD_WIDTH / 2, player.world().width() - Constants.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - Constants.WORLD_HEIGHT / 2, player.world().height() - Constants.WORLD_HEIGHT));
		return new PreLookScreen(player, "Observando", player.x - scrollX, player.y - scrollY);
	}
	
	@Override
	protected Screen rightScreen(){
		int scrollX = Math.max(0, Math.min(player.x - Constants.WORLD_WIDTH / 2, player.world().width() - Constants.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - Constants.WORLD_HEIGHT / 2, player.world().height() - Constants.WORLD_HEIGHT));
		return new ThrowScreen(player, player.x - scrollX, player.y - scrollY);
	}
	
	public EatScreen(Creature player) {
		super(player);
	}

	@Override
	protected String getVerb() {
		return "consumir";
	}

	@Override
	protected boolean isAcceptable(Item item) {
		return item.getBooleanData(Flags.IS_EDIBLE);
	}

	@Override
	protected Screen use(Item item) {
		player.eat(item);
		return null;
	}

	@Override
	public String getScreenName() {
		return "CONSUMIR";
	}
}
