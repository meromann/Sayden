package rltut;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
	STATIC('?', AsciiPanel.white, "Tile estático", true),
	INTERACTABLE('?', AsciiPanel.white, "Tile interactuable", true),
	
	OPEN_DOOR((char)9, AsciiPanel.brightBlack, "Dura puerta de madera cerrada.", true),
	CLOSE_DOOR((char)10, AsciiPanel.brightBlack, "Umbral de una puerta de madera.", false),
	FLOOR((char)250, AsciiPanel.yellow, "Piso de tierra y roca.", true),
	WALL((char)177, AsciiPanel.yellow, "Muro de tierra y roca.", false),
	TOWN_WALL((char)177, Color.yellow, "Un alto muro de madera, usado como perímetro.", false),
	BUILD_WALL((char)177, Color.gray, "Un robusto muro de madera.", false),
	SECRET_BUILD_WALL((char)177, Color.gray, "Un robusto muro de madera...un segundo...", false),
	TOWN_FLOOR((char)250, Color.gray, "Una simple plancha de madera que sirve de piso.", true),
	GRASS(',', AsciiPanel.green, "Pasto verde, de que otro color va a ser?.", true),
	BOUNDS('x', AsciiPanel.brightBlack, "Limites del mundo.", false),
	STAIRS_DOWN('>', AsciiPanel.white, "Una escalera de piedra que desciende.", true),
	STAIRS_UP('<', AsciiPanel.white, "Una escalera de piedra que asciende.", true),
	PORTAL_LEFT((char)27, AsciiPanel.brightWhite, "Camino a otro mapa.", true),
	PORTAL_RIGHT((char)26, AsciiPanel.brightWhite, "Camino a otro mapa.", true),
	PORTAL_DOWN((char)25, AsciiPanel.brightWhite, "Camino a otro mapa.", true),
	PORTAL_UP((char)24, AsciiPanel.brightWhite, "Camino a otro mapa.", true),
	FENCE_VERTICAL((char)186, AsciiPanel.white, "Una valla de metal, vieja y oxidada.", true),
	FENCE_HORIZONTAL((char)205, AsciiPanel.white, "Una valla de metal, vieja y oxidada.", true),
	FENCE_CONNECTION_R_U((char)188, AsciiPanel.white, "Una valla de metal, vieja y oxidada.", true),
	UNKNOWN(' ', AsciiPanel.white, "(desconocido)", true);
	
	private TileData overrideData = null;
	public TileData getOverrideData() { return overrideData; }
	public void setOverrideData(TileData override) { this.overrideData = override; }
	
	private char glyph;
	public char glyph() { return overrideData == null ? glyph : overrideData.glyph(); }
	public void changeGlyph(char c) { this.glyph = c; }
	
	private Color color;
	public Color color() { return color; }
	public void changeColor(Color color) { this.color = color; }
	
	private String description;
	public String details(){ return overrideData == null ? description : overrideData.details(); }
	public void changeDetails(String description){ this.description = description; }
	
	private boolean seeTrough;
	public boolean isSeeTrough() { return seeTrough; }
	public void makeSeeTrough(boolean b) { this.seeTrough = b; }
	
	private boolean destructible;
	public boolean is_destructible() { return destructible; }

	private String destructible_action;
	public String get_destructible_action() { return destructible_action; }
	
	private String portal_to;
	public String getPortalTo() { return portal_to; }
	public Tile setPortalTo(String portal_to) { this.portal_to = portal_to; return this; }
	
	private Point portal_position;
	public Point getPortalPos() { return portal_position; }
	public void setPortalPos(Point point) { this.portal_position = point; }
	
	private String action;
	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	
	private Tile change_to = null;
	public Tile getChangeTo() { return change_to; }
	public void setChangeTo(Tile t) { this.change_to = t; }
	
	private String interactable;
	public String getInteractable() { return interactable; }
	public void setInteraction(String interaction) { this.interactable = interaction; }
	
	Tile(char glyph, Color color, String description, boolean seeTrough){
		this.glyph = glyph;
		this.color = color;
		this.description = description;
		this.seeTrough = seeTrough;
		this.portal_to = null;
	}

	private boolean isGround = false;
	public boolean isGround() {
		if(isGround){
			return true;
		}
		return (this != TOWN_WALL && this != WALL && this != BOUNDS
				&& this != BUILD_WALL && this != CLOSE_DOOR	&& this != SECRET_BUILD_WALL
					&& this != FENCE_VERTICAL && this != FENCE_HORIZONTAL && this != FENCE_CONNECTION_R_U);
	}
	public void makeGround(boolean b){ isGround = b; }
	
	public boolean isPortal() {
		return this == PORTAL_LEFT
				|| this == PORTAL_UP
						|| this == PORTAL_DOWN
								|| this == PORTAL_RIGHT;
	}

	public boolean isDoor() {
		return this == OPEN_DOOR || this == CLOSE_DOOR
				|| this == SECRET_BUILD_WALL;
	}
	
	public boolean isClosedDoor() {
		return this == CLOSE_DOOR || this == SECRET_BUILD_WALL;
	}
	
	public boolean isDiggable() {
		return this == Tile.WALL;
	}
}
