package rltut.screens;

import java.util.ArrayList;

import rltut.Creature;

public class ShopScreen extends OptionBasedScreen {
	
	private Creature seller;
	
	public ShopScreen(Creature player, Creature seller) {
		super(player);
		this.seller = seller;
	}

	@Override
	protected String getVerb() {
		return "hacer";
	}

	@Override
	protected Screen select() {
		return null;
	}

	@Override
	protected ArrayList<Option> getOptions() {
		ArrayList<Option> options = new ArrayList<Option>();
		Option buy = new Option(letters.charAt(0) + " - Ver inventario", new SwapScreen(seller, player));
		Option sell = new Option(letters.charAt(1) + " - Mostrar inventario", new SwapScreen(player, seller));
		options.add(buy);
		options.add(sell);
		return options;
	}

	@Override
	protected void onLeave() {
		seller.ai().onFarewell();
		player.setShopScreen(null);
	}

	@Override
	public String getScreenName() {
		return "SHOP";
	}
}
