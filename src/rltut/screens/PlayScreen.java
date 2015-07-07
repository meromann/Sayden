package rltut.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asciiPanel.AsciiPanel;
import rltut.Constants;
import rltut.Creature;
import rltut.Item;
import rltut.MapLoader;
import rltut.FieldOfView;
import rltut.Message;
import rltut.StringUtils;
import rltut.StuffFactory;
import rltut.Tile;
import rltut.World;
import rltut.Wound;

public class PlayScreen implements Screen {
	private Map<String, World> worldList;
	private World world;
	private Creature player;
	private int screenWidth;
	private int screenHeight;
	private List<Message> messages;
	private FieldOfView fov;
	private Screen subscreen;
		
	public PlayScreen(){
		screenWidth = Constants.WORLD_WIDTH;
		screenHeight = Constants.WORLD_HEIGHT;
		
		messages = new ArrayList<Message>();
		worldList = new HashMap<String, World>();
		
		createWorld();
		
		fov = new FieldOfView(world);
		
		StuffFactory factory = new StuffFactory(world);
				
		createCreatures(factory);
		createRocks(factory);
		
		worldList.put(Constants.STARTING_MAP, world);
	}

	private void createCreatures(StuffFactory factory){
		player = factory.newPlayer(messages, fov);
		factory.newMaleWolf(0, player);
		factory.newMaleWolf(0, player);
		factory.newMaleWolf(0, player);
	}

	private void createRocks(StuffFactory factory) {
		for (int i = 0; i < world.width() * world.height() / 70; i++){
			factory.newRock(0);
		}
	}
	
	private void createWorld(){
		world = new MapLoader()
					.preBuild(Constants.STARTING_MAP);
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		displayWounds(terminal, player.wounds());
				
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private void displayWounds(AsciiPanel terminal, List<Wound> wounds){
		for(int i = 0; i < wounds.size(); i++){	//Si la herida tiene tiempo mostramelo con su color, sino ponele X y de color rojo
			terminal.write((wounds.get(i).duration() > 0 ? wounds.get(i).duration() : "X") + " ", (i*2)+1, Constants.MENU_OFFSET, wounds.get(i).duration() > 0 ? Constants.MESSAGE_STATUS_EFFECT_COLOR : Color.RED);
		}
		String stats = String.format("Mov %s (%3d) Ataq %s (%3d)", StringUtils.speedToString(player.movementSpeed()), player.movementSpeed(), StringUtils.speedToString(player.attackSpeed()), player.attackSpeed());
		//String stats = String.format(" %3d/%3d hp   %d/%d mana   %8s", player.hp(), player.hp(), player.mana(), player.maxMana(), hunger());
		terminal.write(stats, 1, Constants.SCREEN_HEIGHT - 1);
		
		String hp = String.format("%s/%s", player.hp(), player.maxHp());
		terminal.write(hp, Constants.SCREEN_WIDTH - hp.length() - 2, Constants.SCREEN_HEIGHT - 1);
	}
	
	private void displayMessages(AsciiPanel terminal, List<Message> messages) {
		int top = Constants.MENU_OFFSET + 2;
		
		for (int i = 0; i < messages.size(); i++){
			if(messages.get(i).message().length() >= Constants.SCREEN_WIDTH){
				ArrayList<Message> toAdd = StringUtils.splitPhraseByLimit(messages.get(i), Constants.SCREEN_WIDTH);
				messages.remove(i);
				messages.addAll(i, toAdd);
			}
		}

		if(top + messages.size() >= Constants.SCREEN_HEIGHT - 1){
			top -= ((top + messages.size() + 1) - Constants.SCREEN_HEIGHT);
		}
		
		for (int i = 0; i < messages.size(); i++){
			terminal.writeCenter(messages.get(i).message(), top + i, messages.get(i).color());
		}
		
		if (subscreen == null || player.shopScreen() != null)
			messages.clear();
	}

	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;

				if (player.canSee(wx, wy, player.z))
					terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z), world.backgroundColor(wx, wy, player.z));
				else
					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray, Color.BLACK);
			}
		}
	}
	
	public int getScrollX() { return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth)); }
	
	public int getScrollY() { return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight)); }
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		
		if (player.hp() < 1)
			return new LoseScreen(player);
		
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()){
			case KeyEvent.VK_SPACE: player.moveBy(0, 0, 0); break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: player.moveBy( 1, 0, 0); break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W: player.moveBy( 0,-1, 0); break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: player.moveBy( 0, 1, 0); break;
			case KeyEvent.VK_TAB: subscreen = new EquipScreen(player); break;
			}
			/*case KeyEvent.VK_L: subscreen = new LookScreen(player, "Observando", 
					player.x - getScrollX(), 
					player.y - getScrollY()); break;
			case KeyEvent.VK_T: subscreen = new ThrowScreen(player,
					player.x - getScrollX(), 
					player.y - getScrollY()); break;
			case KeyEvent.VK_F: 
				if (player.weapon() == null || player.weapon().rangedAttackValue() == 0)
					player.notify("No tienes un arma de rango equipada.");
				else
					subscreen = new FireWeaponScreen(player,
						player.x - getScrollX(), 
						player.y - getScrollY()); break;*/
			/*case KeyEvent.VK_R: subscreen = new ReadScreen(player,
						player.x - getScrollX(), 
						player.y - getScrollY()); break;*/
			
			if(key.getKeyChar() >= '1' && key.getKeyChar() <= '9'){
				int mappedKey = Integer.parseInt(key.getKeyChar()+"") - 1;
				if(keyMap[mappedKey] != null){
					subscreen = keyMap[mappedKey];
				}
			} 
			
			switch (key.getKeyChar()){
			case 'g':
			case ',': player.pickup(); break;
			case '<': 
				if (userIsTryingToExit())
					return userExits();
				else
					player.moveBy( 0, 0, -1); break;
			case '>': player.moveBy( 0, 0, 1); break;
			case '?': subscreen = new HelpScreen(); break;
			}
		}
		
		if(player.getWorld() != this.world){
			String worldName = player.getWorld().name;
			
			if(worldList.containsKey(worldName)){
				this.world = worldList.get(worldName);
				player.setWorld(world);
			}else{
				this.world = player.getWorld();
				worldList.put(world.name, world);
			}
			
			world.add(player);
			fov.updateWorld(world);
			player.getCreatureAi().updateFow(fov);
		}
		
		if(player.shopScreen() != null && subscreen == null)
			subscreen = player.shopScreen();
		
		if (subscreen == null)
			world.update();
				
		return this;
	}

	private boolean userIsTryingToExit(){
		return player.z == 0 && world.tile(player.x, player.y, player.z).interaction() == Tile.Interaction.STAIRS_UP;
	}
	
	private Screen userExits(){
		for (Item item : player.inventory().getItems()){
			if (item != null && item.name().equals("teddy bear"))
				return new WinScreen();
		}
		player.modifyHp(0, "Died while cowardly fleeing the caves.");
		return new LoseScreen(player);
	}

	@Override
	public String getScreenName() {
		return null;
	}
}
