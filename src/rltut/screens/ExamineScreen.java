package rltut.screens;

import rltut.Creature;
import rltut.Item;

public class ExamineScreen extends InventoryBasedScreen {

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
		String article = player.checkGender(item.gender(), false);
		player.notify("Es " + article + " " + player.nameOf(item) + "." + item.details());
		return null;
	}

	@Override
	public String getScreenName() {
		return "EXAMINAR";
	}
}
