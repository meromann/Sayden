package rltut;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import asciiPanel.AsciiPanel;

public class TileData {
	public static TileData GRASS = new TileData(',', AsciiPanel.green, "Pasto cubierto ligeramente en rocio, se siente bien a los pies desnudos.", true);
	public static TileData OPEN_DOOR = new TileData((char)9, AsciiPanel.brightBlack, "Dura puerta de madera abierta.", true).addInteraction(Interaction.CHANGES);
	public static TileData CLOSE_DOOR = new TileData((char)10, AsciiPanel.brightBlack, "Dura puerta de madera cerrada.", false, false).addInteraction(Interaction.CHANGES);
	public static TileData FLOOR = new TileData((char)250, AsciiPanel.yellow, "Piso de tierra y roca.", true);
	public static TileData WALL = new TileData((char)177, AsciiPanel.yellow, "Muro de tierra y roca.", false, false);
	public static TileData TOWN_WALL = new TileData((char)177, Color.yellow, "Un alto muro de madera, usado como perímetro.", false, false);
	public static TileData BUILD_WALL = new TileData((char)177, Color.gray, "Un robusto muro de madera.", false, false);
	public static TileData SECRET_BUILD_WALL = new TileData((char)177, Color.gray, "Un robusto muro de madera...un segundo...", false, false);
	public static TileData TOWN_FLOOR = new TileData((char)250, Color.gray, "Una simple plancha de madera que sirve de piso.", true);
	public static TileData STAIRS_UP = new TileData('<', AsciiPanel.white, "Una escalera de piedra que asciende.", true).addInteraction(Interaction.STAIRS_UP);
	public static TileData STAIRS_DOWN = new TileData('>', AsciiPanel.white, "Una escalera de piedra que desciende.", true).addInteraction(Interaction.STAIRS_DOWN);
	public static TileData PORTAL_LEFT = new TileData((char)27, AsciiPanel.brightWhite, "Camino a otro mapa.", true);
	public static TileData PORTAL_RIGHT = new TileData((char)26, AsciiPanel.brightWhite, "Camino a otro mapa.", true);
	public static TileData PORTAL_DOWN = new TileData((char)25, AsciiPanel.brightWhite, "Camino a otro mapa.", true);
	public static TileData PORTAL_UP = new TileData((char)24, AsciiPanel.brightWhite, "Camino a otro mapa.", true);
	public static TileData FENCE = new TileData((char)26, AsciiPanel.brightWhite, "Una valla de metal, vieja y oxidada.", true, false);

	public static final TileData BOUNDS = new TileData('x', AsciiPanel.brightBlack, "Limites del mundo.", false, false);
	public static final TileData UNKNOWN = new TileData(' ', AsciiPanel.white, "(desconocido)", true);
	
	static final Map<String, TileData> VALUES_BY_NAME;
	static {
	    final Map<String, TileData> valuesByName = new HashMap<>();
	    valuesByName.put("OPEN_DOOR", OPEN_DOOR);
	    valuesByName.put("CLOSE_DOOR", CLOSE_DOOR);
	    valuesByName.put("FLOOR", FLOOR);
	    valuesByName.put("WALL", WALL);
	    valuesByName.put("TOWN_WALL", TOWN_WALL);
	    valuesByName.put("BUILD_WALL", BUILD_WALL);
	    valuesByName.put("SECRET_BUILD_WALL", SECRET_BUILD_WALL);
	    valuesByName.put("TOWN_FLOOR", TOWN_FLOOR);
	    valuesByName.put("STAIRS_UP", STAIRS_UP);
	    valuesByName.put("STAIRS_DOWN", STAIRS_DOWN);
	    valuesByName.put("PORTAL_LEFT", PORTAL_LEFT);
	    valuesByName.put("PORTAL_RIGHT", PORTAL_RIGHT);
	    valuesByName.put("PORTAL_DOWN", PORTAL_DOWN);
	    valuesByName.put("PORTAL_UP", PORTAL_UP);
	    valuesByName.put("FENCE", FENCE);
	    valuesByName.put("GRASS", GRASS);
	    
	    VALUES_BY_NAME = Collections.unmodifiableMap(valuesByName);
	}
	
	public enum Interaction{
		STAIRS_DOWN,
		STAIRS_UP,
		CHANGES
	}
		
	private char glyph;
	public char glyph() { return this.glyph;}
	public void setGlyph(char glyph) { this.glyph = glyph; }
	
	private Color color;
	public Color color() { return this.color; }
	public void setColor(Color color) { this.color = color; }
	
	private String details;
	public String details() { return details; }
	public void setDetails(String details) { this.details = details; }
	
	private String action;
	public String action() { return action; }
	public void setAction(String action) { this.action = action; }
	
	private boolean seeTrough;
	public boolean seeTrough() { return seeTrough; }
	public void makeSeeTrough(boolean seeTrough) { this.seeTrough = seeTrough; }
	
	private boolean isGround;
	public boolean isGround() {	return isGround; }
	public void makeGround(boolean isGround) { this.isGround = isGround; }
	
	private String portal_to;
	public String getPortalTo() { return portal_to; }
	public TileData setPortalTo(String portal_to) { this.portal_to = portal_to; return this; }
	
	private Point portal_position;
	public Point getPortalPos() { return portal_position; }
	public void setPortalPos(Point point) { this.portal_position = point; }
	
	private Interaction interaction;
	public Interaction interaction() { return interaction; }
	public void setInteraction(Interaction interaction) { this.interaction = interaction; }
	
	private TileData change_to;
	public TileData change_to() { return change_to; }
	public void setChangeTo(TileData tile) { this.change_to = tile; }
	public TileData setChangeTo1(TileData tile) { this.change_to = tile; return this; }
	
	private TileData addInteraction(Interaction interaction) { this.interaction = interaction; return this; }
	
	TileData(char glyph, Color color, String details, boolean seeTrough, boolean isGround){
		this.glyph = glyph;
		this.color = color;
		this.details = details;
		this.seeTrough = seeTrough;
		this.isGround = isGround;
		this.portal_to = null;
		this.change_to = new TileData();
	}
	
	TileData(char glyph, Color color, String details, boolean seeTrough){
		this.glyph = glyph;
		this.color = color;
		this.details = details;
		this.seeTrough = seeTrough;
		this.portal_to = null;
		this.isGround = true;
		this.change_to = new TileData();
	}
	
	TileData(){
		this.glyph = '?';
		this.color = Color.PINK;
		this.details = "INITIALIZE_ERROR";
		this.seeTrough = false;
		this.isGround = true;
		this.portal_to = null;
	}
}
