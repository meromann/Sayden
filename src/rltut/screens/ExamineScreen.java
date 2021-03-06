package rltut.screens;

import rltut.Constants;
import rltut.Creature;
import rltut.Item;
import rltut.StringUtils;

public class ExamineScreen extends InventoryBasedScreen {

	@Override
	protected Screen leftScreen(){
		return new EquipScreen(player);
	}
	
	@Override
	protected Screen rightScreen(){
		int scrollX = Math.max(0, Math.min(player.x - Constants.WORLD_WIDTH / 2, player.getWorld().width() - Constants.WORLD_WIDTH));
		int scrollY = Math.max(0, Math.min(player.y - Constants.WORLD_HEIGHT / 2, player.getWorld().height() - Constants.WORLD_HEIGHT));
		return new PreLookScreen(player, "Observando", player.x - scrollX, player.y - scrollY);
	}
	
	public ExamineScreen(Creature player) {
		super(player);
	}

	@Override
	protected String getVerb() {
		return "examinar";
	}

	@Override
	protected boolean isAcceptable(Item item) {
		return true;
	}

	@Override
	protected Screen use(Item item) {
		String article = StringUtils.checkGender(item.gender(), false, player.isPlayer());
		player.notify("Es " + article + " " + player.nameOf(item) + "." + item.details());
		return null;
	}

	@Override
	public String getScreenName() {
		return "EXAMINAR";
	}
}
