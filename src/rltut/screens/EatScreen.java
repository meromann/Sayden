package rltut.screens;

import rltut.ApplicationMain;
import rltut.Creature;
import rltut.Item;
import rltut.Item.ItemType;

public class EatScreen extends InventoryBasedScreen {

	@Override
	protected Screen leftScreen(){
		int scrollX = Math.max(0, Math.min(player.x - ApplicationMain.WORLD_WIDTH / 2, player.getWorld().width() - ApplicationMain.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - ApplicationMain.WORLD_HEIGHT / 2, player.getWorld().height() - ApplicationMain.WORLD_HEIGHT));
		return new PreLookScreen(player, "Observando", player.x - scrollX, player.y - scrollY);
	}
	
	@Override
	protected Screen rightScreen(){
		int scrollX = Math.max(0, Math.min(player.x - ApplicationMain.WORLD_WIDTH / 2, player.getWorld().width() - ApplicationMain.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - ApplicationMain.WORLD_HEIGHT / 2, player.getWorld().height() - ApplicationMain.WORLD_HEIGHT));
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
		return item.itemType() == ItemType.EDIBLE;
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
