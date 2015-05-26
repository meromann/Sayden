package rltut.screens;

import rltut.Creature;
import rltut.Item;
import rltut.Item.ItemType;

public class EatScreen extends InventoryBasedScreen {

	public EatScreen(Creature player) {
		super(player);
		leftScreen = this;
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
