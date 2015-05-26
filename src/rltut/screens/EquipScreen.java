package rltut.screens;

import rltut.Creature;
import rltut.Item;
import rltut.Item.ItemType;

public class EquipScreen extends InventoryBasedScreen {

	public EquipScreen(Creature player, Screen left, Screen right) {
		super(player);
		super.leftScreen = left;
		super.rightScreen = right;
	}

	protected String getVerb() {
		return "equiparte";
	}

	protected boolean isAcceptable(Item item) {
		return item.itemType() == ItemType.ARMOR ||
				item.itemType() == ItemType.HELMENT ||
				item.itemType() == ItemType.SHIELD ||
				item.itemType() == ItemType.WEAPON;
	}

	protected Screen use(Item item) {
		player.equip(item);
		return null;
	}

	public String getScreenName() {
		return "EQUIPAR";
	}
}
