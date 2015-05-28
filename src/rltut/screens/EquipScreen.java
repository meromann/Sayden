package rltut.screens;

import rltut.Creature;
import rltut.Item;
import rltut.Item.ItemType;

public class EquipScreen extends InventoryBasedScreen {
	
	@Override
	protected Screen leftScreen(){
		return new DropScreen(player);
	}
	
	@Override
	protected Screen rightScreen(){
		return new ExamineScreen(player);
	}
	
	public EquipScreen(Creature player) {
		super(player);
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
