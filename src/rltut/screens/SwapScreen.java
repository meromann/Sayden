package rltut.screens;

import rltut.Creature;
import rltut.Item;

public class SwapScreen extends InventoryBasedScreen {

	private Creature buyer;
	
	public SwapScreen(Creature player, Creature buyer) {
		super(player);
		this.buyer = buyer;
	}

	@Override
	protected String getVerb() { 
		if(!buyer.isPlayer())
			return "entregar";
		return "pedir"; 
	}

	@Override
	protected boolean isAcceptable(Item item) { 
		if(!buyer.isPlayer())
			return true;
		
		return item.perceivedValue() > buyer.ai().relationship() ? false : true;
	}
	
	@Override
	protected Screen use(Item item) { 
		if(isAcceptable(item)){
			player.inventory().remove(item);
			buyer.inventory().add(item);
			player.ai().upRelationship(item.perceivedValue());
		}
		buyer.ai().onReceive(item, player);	
		player.ai().onGive(item, buyer);
		
		return null;
	}

	@Override
	public String getScreenName() {
		return "CANJEAR";
	}
}
