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
		screenWidth = ApplicationMain.WORLD_WIDTH;
		screenHeight = ApplicationMain.WORLD_HEIGHT;
		
		messages = new ArrayList<Message>();
		worldList = new HashMap<String, World>();
		
		Wound.instantiateWounds();
		createWorld();
		
		fov = new FieldOfView(world);
		
		StuffFactory factory = new StuffFactory(world);
				
		createCreatures(factory);
		createRocks(factory);
		
		worldList.put(ApplicationMain.STARTING_MAP, world);
	}

	private void createCreatures(StuffFactory factory){
		player = factory.newPlayer(messages, fov);
		factory.newZombie(0, player);
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
		displayWounds(terminal, player.wounds());
				
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
		
	private void displayWounds(AsciiPanel terminal, List<Wound> wounds){
		for(int i = 0; i < wounds.size(); i++){
			Color[] arrayColor = new Color[]{	
					Color.ORANGE,
					new Color(249,120,0),
					new Color(200, 80, 0),
					new Color(211,0,0),
					new Color(255,0,0)
			};

			terminal.write(wounds.get(i).severity() + " ", (i*2)+1, ApplicationMain.MENU_OFFSET, arrayColor[wounds.get(i).severity() - 1 >= arrayColor.length ? arrayColor.length : wounds.get(i).severity() - 1]);
		}
		//String stats = String.format(" %3d/%3d hp   %d/%d mana   %8s", player.hp(), player.hp(), player.mana(), player.maxMana(), hunger());
		//terminal.write(stats, 1, ApplicationMain.MENU_OFFSET);
	}

	private void displayMessages(AsciiPanel terminal, List<Message> messages) {
		int top = ApplicationMain.MENU_OFFSET + 2;
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
			case KeyEvent.VK_A: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: player.moveBy( 1, 0, 0); break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W: player.moveBy( 0,-1, 0); break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: player.moveBy( 0, 1, 0); break;
			case KeyEvent.VK_TAB: subscreen = new EquipScreen(player, new DropScreen(player), new ExamineScreen(player)); break;
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

	@Override
	public String getScreenName() {
		return null;
	}
}
