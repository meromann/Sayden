package rltut.screens;

import rltut.Creature;
import rltut.Flags;
import rltut.Item;

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
		return item.getBooleanData(Flags.IS_ARMOR) ||
				item.getBooleanData(Flags.IS_HELMENT) ||
				item.getBooleanData(Flags.IS_SHIELD) ||
				item.getBooleanData(Flags.IS_WEAPON);
	}

	protected Screen use(Item item) {
		player.equip(item);
		return null;
	}

	public String getScreenName() {
		return "EQUIPAR";
	}
}
