package rltut;

import java.awt.Color;

import asciiPanel.AsciiPanel;

public class Constants {
	public static final int SCREEN_WIDTH = 80;
	public static final int SCREEN_HEIGHT = 32;
	
	public static final int WORLD_WIDTH = 80;
	public static final int WORLD_HEIGHT = 24;
	
	public static final int MENU_OFFSET = (int)(WORLD_HEIGHT);
	
	public static final String STARTING_MAP = "Pueblo";
	
	public static final int INVENTORY_SIZE = 10;
	public static final int STARTING_ATTACK_SPEED = 100;
	public static final int STARTING_MOVE_SPEED = 100;
	
	public static final Color MESSAGE_STATUS_EFFECT_COLOR = AsciiPanel.yellow;
	public static final String MESSAGE_DODGE = "falla el ataque";				//doAction(MESSAGE_DODGE)
	public static final Color MESSAGE_DODGE_COLOR = AsciiPanel.brightBlue;
	public static final String MESSAGE_DODGE_WARNING = "prepara para atacar";	//doAction(MESSAGE_DODGE_WARNING)
	
	public static final int STARTING_VISION_RADIUS = 9;

}
