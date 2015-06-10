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
	public static final String MESSAGE_DODGE = "falla el ataque";				//doAction(MESSAGE_DODGE)
	public static final Color MESSAGE_DODGE_COLOR = AsciiPanel.brightBlue;
	public static final String MESSAGE_DODGE_WARNING = "prepara para atacar";	//doAction(MESSAGE_DODGE_WARNING)
	
	public static final int STARTING_VISION_RADIUS = 9;
	
	public static final int LVL1_DURATION = 20;
	public static final int LVL2_DURATION = 42;
	public static final int LVL3_DURATION = 80;
	public static final int INCURABLE = -1;
	
	protected static final Color MESSAGE_KILL_COLOR = AsciiPanel.brightRed;
	protected static final Color BLOOD_COLOR = AsciiPanel.red;
	
	public static final int SEV_LEG_PENALTY = 100;
	public static final int BROKEN_LEG_PENALTY = 50;
	public static final int BROKEN_ARM_PENALTY = 50;
	public static final int BACK_TRAUMA_PENALTY = 100;
	public static final float BROKEN_ARM_DROP_CHANCE = 0.3f;	
	public static final float HEAD_TRAUMA_WANDER_CHANCE = 0.3f;
	public static final float BACK_TRAUMA_SKIP_CHANCE = 0.3f;
	public static final float DISMEMBER_CHANCE_LIMB_OVER_KILL = 0.70f;			//brazo < chance

}
