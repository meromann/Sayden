package rltut;

import java.awt.Color;

import asciiPanel.AsciiPanel;

public class Constants {
	public static final Color BACKGROUND_COLOR = AsciiPanel.black;
	
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
	public static final String MESSAGE_DODGE = "falla el ataque!";				//doAction(MESSAGE_DODGE)
	public static final Color MESSAGE_CRIT_COLOR = Color.RED;
	public static final String MESSAGE_CRIT = "impacta con gran presicion!";		//doAction(MESSAGE_CRIT)
	public static final String MESSAGE_PARTIAL_HIT = "efectua un ataque impresiso!";		//doAction(MESSAGE_CRIT)
	public static final Color MESSAGE_DODGE_COLOR = AsciiPanel.brightBlue;
	public static final String MESSAGE_DODGE_WARNING = "prepara para atacar";	//doAction(MESSAGE_DODGE_WARNING)
	public static final String MESSAGE_COUNTER = "aprovecha la oportunidad para atacar";
	public static final Color WOUND_COLOR = new Color(255,144,0);
	protected static final Color BLOOD_COLOR = AsciiPanel.red;

	public static final int STARTING_VISION_RADIUS = 9;
	
	public static final int WOUND_DURATION_LOW = 10;
	public static final int WOUND_DURATION_MID = 20;
	public static final int WOUND_DURATION_HIGH = 30;
	public static final int WOUND_PERMANENT = -1;
		
	public static final int SEV_LEG_PENALTY = 100;
	public static final float DISMEMBER_CHANCE_LIMB_OVER_KILL = 0.70f;			//brazo < chance
}
