package rltut.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asciiPanel.AsciiPanel;
import rltut.ApplicationMain;
import rltut.Creature;
import rltut.Item;
import rltut.MapLoader;
import rltut.FieldOfView;
import rltut.Message;
import rltut.StuffFactory;
import rltut.Tile;
import rltut.World;

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
		screenWidth = ApplicationMain.WORLD_WIDTH;
		screenHeight = ApplicationMain.WORLD_HEIGHT;
		
		messages = new ArrayList<Message>();
		worldList = new HashMap<String, World>();
		
		createWorld();
		
		fov = new FieldOfView(world);
		
		StuffFactory factory = new StuffFactory(world);
				
		createCreatures(factory);
		createRocks(factory);
		
		worldList.put(ApplicationMain.STARTING_MAP, world);
	}

	private void createCreatures(StuffFactory factory){
		player = factory.newPlayer(messages, fov);
	}

	private void createRocks(StuffFactory factory) {
		for (int i = 0; i < world.width() * world.height() / 70; i++){
			factory.newRock(0);
		}
	}
	
	private void createWorld(){
		world = new MapLoader()
					.preBuild(ApplicationMain.STARTING_MAP);
	}
	
	public int getScrollX() { return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth)); }
	
	public int getScrollY() { return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight)); }
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		String stats = String.format(" %3d/%3d hp   %d/%d mana   %8s", player.hp(), player.maxHp(), player.mana(), player.maxMana(), hunger());
		terminal.write(stats, 1, ApplicationMain.MENU_OFFSET - 4);
		
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private String hunger(){
		if (player.food() < player.maxFood() * 0.10)
			return "Starving";
		else if (player.food() < player.maxFood() * 0.25)
			return "Hungry";
		else if (player.food() > player.maxFood() * 0.90)
			return "Stuffed";
		else if (player.food() > player.maxFood() * 0.75)
			return "Full";
		else
			return "";
	}

	private void displayMessages(AsciiPanel terminal, List<Message> messages) {
		int top = ApplicationMain.MENU_OFFSET - messages.size();
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
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		int level = player.level();
		
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()){
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_H: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT: player.moveBy( 1, 0, 0); break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_K: player.moveBy( 0,-1, 0); break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_J: player.moveBy( 0, 1, 0); break;
			case KeyEvent.VK_Y: player.moveBy(-1,-1, 0); break;
			case KeyEvent.VK_U: player.moveBy( 1,-1, 0); break;
			case KeyEvent.VK_B: player.moveBy(-1, 1, 0); break;
			case KeyEvent.VK_N: player.moveBy( 1, 1, 0); break;
			case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
			case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
			case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;
			case KeyEvent.VK_X: subscreen = new ExamineScreen(player); break;
			case KeyEvent.VK_L: subscreen = new LookScreen(player, "Observando", 
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
						player.y - getScrollY()); break;
			case KeyEvent.VK_Q: subscreen = new QuaffScreen(player); break;
			case KeyEvent.VK_R: subscreen = new ReadScreen(player,
						player.x - getScrollX(), 
						player.y - getScrollY()); break;
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
		
		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		
		if(player.shopScreen() != null && subscreen == null)
			subscreen = player.shopScreen();
		
		if (subscreen == null)
			world.update();
				
		if (player.hp() < 1)
			return new LoseScreen(player);
				
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
}
