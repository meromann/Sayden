package rltut;
import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rltut.ai.NpcAi;

public class MapLoader {
	
	private int mapWidth = 0;
	private int mapHeight = 0;
	/** Variable usada para recortar el XML con la informacion del mapa*/
	private String mapReference;
	/** Tileset cargado del XML*/
	private LinkedList<Tile> tileSet;
	/** Lista con los npcs que se agregarán al mapa recien creado */
	private LinkedList<Creature> npcs;
	
	private World world;
	private Tile[][][] tiles;
	
	public World preBuild(String mapName)
	{
		tileSet = new LinkedList<Tile>();
		npcs = new LinkedList<Creature>();
		
		if( checkFile(mapName) ){
			String[] lines = mapReference.split("\n");
			
			//En caso de que el mapa sea mas chico que el mundo soportado
			mapHeight = lines.length < ApplicationMain.WORLD_HEIGHT ? ApplicationMain.WORLD_HEIGHT : lines.length;
			mapWidth = lines[0].length() < ApplicationMain.WORLD_HEIGHT ? ApplicationMain.WORLD_HEIGHT : lines[0].length();
			
			//TODO: Resolver z_index!!
			tiles = new Tile[mapWidth][mapHeight][1];
			
			for(int y = 0; y < lines.length; y++){
				for(int x = 0; x < lines[y].length(); x++){
					char stringIndex = lines[y].replace("\n", "").replace("\r", "").replace("\t", "").charAt(x);
										
					if(!(stringIndex >= '0' && stringIndex <= '9')){
						//En caso de que arranquemos con letras
						//Este es un fix puesto que cada numero identifica un tile, lo que delimita cada mapa a usar
						//9 tiles, gracias a esto tambien se podran usar letras, siendo la a = 10
						stringIndex -= 87;	// -87 para que a = 10
						tiles[x][y][0] = tileSet.get((int)stringIndex);
					}else{
						//Los primeros números
						tiles[x][y][0] = tileSet.get(Integer.parseInt(stringIndex+""));
					}
				}
			}
			
			world = new World(tiles, mapName);	//Creamos el mapa
			world.add(npcs);	//Le añadimos a los npc YA POPULADOS en checkFile()
		}

		return world;
	}
	
	/** Esta funcion se encarga de leer el mapa y popular los objetos correspondientes
	 *  dentro del juego. Esto incluye npcs y los tiles usados en cada mapa.*/
	private boolean checkFile(String mapName){
		 try {
			File file = new File("maps/"+mapName+".xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodeLst = doc.getElementsByTagName("version");
			
			//Aqui se cargan las diferentes versiones del mapa (para soporte de diferentes tiles de mapas)
			//TODO: Implementar z_index
			for (int s = 0; s < nodeLst.getLength(); s++) {
			    Node fstNode = nodeLst.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			    	
			    	Element firstPersonElement = (Element)fstNode;
			    	
	                //---------------MAP-----------------
			    	NodeList mapElementList = firstPersonElement.getElementsByTagName("map");
			    	Element mapElement;
			    	
			    	if(mapElementList.getLength() > 1){
			    		Random rand = new Random();
			    		int mapNro = rand.nextInt((mapElementList.getLength() - 1) + 1);
			    		
			    		mapElement = (Element)mapElementList.item(mapNro);
			    	}else{
				    	mapElement = (Element)mapElementList.item(0);
			    	}
	                
	                NodeList textMNList = mapElement.getChildNodes();
	                
	                mapReference = ((Node)textMNList.item(0)).getNodeValue().trim();
	                mapReference = mapReference.replace("\r", "").replace("\t", "");
			    }
			}
			  
		    nodeLst = doc.getElementsByTagName("tiles");

			for (int s1 = 0; s1 < nodeLst.getLength(); s1++) {
			    Node scndNode = nodeLst.item(s1);
			    
			    if (scndNode.getNodeType() == Node.ELEMENT_NODE) {
			    	
			    	Element firstPersonElement = (Element)scndNode;
			    	
			    	//---------------TILE----------------
			    	NodeList tileElementList = firstPersonElement.getElementsByTagName("tile");
			    				    	
			    	for(int t = 0; t < tileElementList.getLength(); t++){
			    		Element tileElement = (Element)tileElementList.item(t);
			    		
			    		Tile tile = analyzeTile(tileElement);
			            
			    		tileSet.add(t, tile);
			    	}
			    }
			}
			
			nodeLst = doc.getElementsByTagName("npcs");

			for (int s1 = 0; s1 < nodeLst.getLength(); s1++) {
			    Node scndNode = nodeLst.item(s1);
			    
			    if (scndNode.getNodeType() == Node.ELEMENT_NODE) {
			    	
			    	Element firstPersonElement = (Element)scndNode;
			    	
			    	//---------------NPCS----------------
			    	NodeList npcElementList = firstPersonElement.getElementsByTagName("npc");
			    				    	
			    	for(int t = 0; t < npcElementList.getLength(); t++){
			    		Element npcElement = (Element)npcElementList.item(t);
			    		
			    		Creature npc = analyzeCreature(npcElement);
			            
			    		npcs.add(npc);
			    	}
			    }
			}
		 } catch (Exception e) {
		    System.out.println("MAP LOADER ERROR: " + e.getLocalizedMessage());
		    e.printStackTrace();
		    return false;
		 }
		 
		 return true;
	}
	
	/** Analiza el esquema de datos presente en los npcs y los traduce en un objeto */
	private Creature analyzeCreature(Element creatureElement){
		NodeList textWNList = creatureElement.getChildNodes();
		
		String npcName = ((Node)textWNList.item(0)).getNodeValue().trim();
		String npcJob = !creatureElement.getAttribute("trabajo").isEmpty() ? creatureElement.getAttribute("trabajo") : "ciudadano";
		
		int glyphN = Integer.parseInt((creatureElement.getAttribute("glyphN").isEmpty() ? "0" : creatureElement.getAttribute("glyphN")));
		char glyphC = creatureElement.getAttribute("glyphC").isEmpty() ? ' ' : creatureElement.getAttribute("glyphC").charAt(0);
		char gender = creatureElement.getAttribute("gender").isEmpty() ? 'M' : creatureElement.getAttribute("gender").charAt(0);
		char actualGlyph = '@';
		
		int startPosX = Integer.parseInt(creatureElement.getAttribute("x"));
		int startPosY = Integer.parseInt(creatureElement.getAttribute("y"));
		int startPosZ = Integer.parseInt(creatureElement.getAttribute("z"));
		
		int permittedTile = Integer.parseInt((creatureElement.getAttribute("permittedTile").isEmpty() ? "-1" : creatureElement.getAttribute("permittedTile")));
			
		Color npcColor = Color.white;
		Color talkColor = Color.white;
		
		if(glyphC != ' ' || glyphN > 0){
			actualGlyph = (char) (glyphN > 0 ? glyphN : glyphC);
		}
		
		if(!creatureElement.getAttribute("color").isEmpty()){		
			String color = creatureElement.getAttribute("color");
			String[] rgb = color.split("-");
						
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			
			npcColor = new Color(r,g,b);
		}
		
		if(!creatureElement.getAttribute("talkColor").isEmpty()){		
			String color = creatureElement.getAttribute("talkColor");
			String[] rgb = color.split("-");
						
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			
			talkColor = new Color(r,g,b);
		}else{
			talkColor = npcColor;
		}
		
		Creature npc = new Creature(world, gender, actualGlyph, npcColor, npcName, 10, "BLUNT");
		new NpcAi(npc, npcJob, talkColor);
		npc.x = startPosX;
		npc.y = startPosY;
		npc.z = startPosZ;
		
		if(creatureElement.getElementsByTagName("inventory").getLength() > 0){
			
		}
		
		if(creatureElement.getElementsByTagName("dialogue").getLength() > 0){
			NodeList dialogueNode = creatureElement.getElementsByTagName("dialogue");
			
			Element dialogueList = (Element) dialogueNode.item(0);
			
			NodeList introduction = dialogueList.getElementsByTagName("introduction");
			NodeList greetings = dialogueList.getElementsByTagName("greetings");
			NodeList goodbye = dialogueList.getElementsByTagName("goodbye");
			NodeList ongive = dialogueList.getElementsByTagName("ongive");
			NodeList onreceive = dialogueList.getElementsByTagName("onreceive");
			
			((NpcAi) npc.getCreatureAi()).setIntroduction(retrieveMessageData(introduction, npc.color()));
			((NpcAi) npc.getCreatureAi()).setGreetings(retrieveMessageData(greetings, npc.color()));
			((NpcAi) npc.getCreatureAi()).setGoodbye(retrieveMessageData(goodbye, npc.color()));
			((NpcAi) npc.getCreatureAi()).setOngive(retrieveMessageData(ongive, npc.color()));
			((NpcAi) npc.getCreatureAi()).setOnreceive(retrieveMessageData(onreceive, npc.color()));
		}
		
		if(permittedTile > 0){
			((NpcAi) npc.getCreatureAi()).setPermittedTile(tileSet.get(permittedTile));
		}
		
		return npc;
	}
	
	/** Analiza el esquema de datos presente en los dialogos de los npc, los añade a una coleccion y los
	 * devuelve para agregarlos a la criatura */
	private List<Message> retrieveMessageData(NodeList messageNode, Color defaultColor){
		List<Message> messageList = new LinkedList<Message>();

		for(int i = 0; i < messageNode.getLength(); i++){
			Element message = (Element) messageNode.item(i);
			
			Color messageColor = defaultColor;
			String messageText = message.getTextContent().trim();
			
			if(!message.getAttribute("color").isEmpty()){	
				String color = message.getAttribute("color");
				String[] rgb = color.split("-");
							
				int r = Integer.parseInt(rgb[0]);
				int g = Integer.parseInt(rgb[1]);
				int b = Integer.parseInt(rgb[2]);
				
				messageColor = new Color(r,g,b);
			}
			
			Message introMessage = new Message(messageText, messageColor);
			
			messageList.add(introMessage);
		}
		
		return messageList;
	}
	
	/** Analiza el esquema de datos presente en los tiles y lo traduce en un objeto */
	private Tile analyzeTile(Element tileElement){
		NodeList textWNList = tileElement.getChildNodes();
		String tileName = ((Node)textWNList.item(0)).getNodeValue().trim();
		Tile tile = null;
		
		if(Tile.VALUES_BY_NAME.containsKey(tileName)){
			tile = Tile.VALUES_BY_NAME.get(tileName);
		}else{
			tile = new Tile();
		}
				
		String portal_to = tileElement.getAttribute("viaja");
		String details = tileElement.getAttribute("details");
		int glyphN = Integer.parseInt((tileElement.getAttribute("glyphN").isEmpty() ? "0" : tileElement.getAttribute("glyphN")));
		char glyphC = tileElement.getAttribute("glyphC").isEmpty() ? ' ' : tileElement.getAttribute("glyphC").charAt(0);
		String interactable = tileElement.getAttribute("interactable");
		String action = tileElement.getAttribute("action");
		String cloneOf = tileElement.getAttribute("clone");
		
		if(cloneOf != null && !cloneOf.isEmpty()){
			Tile patient_zero = Tile.VALUES_BY_NAME.get(cloneOf);
			
			tile.setGlyph(patient_zero.glyph());
			tile.setColor(patient_zero.color());
			tile.setDetails(patient_zero.details());
			tile.makeGround(patient_zero.isGround());
			tile.makeSeeTrough(patient_zero.seeTrough());
			tile.setInteraction(patient_zero.interaction());
		}
		
		if(interactable.indexOf("change") != -1){
			NodeList textToList = tileElement.getElementsByTagName("tile_to");
			Element tileToName = (Element) textToList.item(0);
				  			  	
			Tile tile_to = analyzeTile(tileToName);
			
			tile_to.setChangeTo(tile);
			tile.setChangeTo(tile_to);
		}
		
		if(portal_to != null && !portal_to.isEmpty()){
		  	int x = Integer.parseInt(tileElement.getAttribute("x"));
		  	int y = Integer.parseInt(tileElement.getAttribute("y"));
		  	int z = Integer.parseInt(tileElement.getAttribute("z"));
		  	
		  	Point p = new Point(x,y,z);
		  	
		  	tile.setPortalTo(portal_to);
		  	tile.setPortalPos(p);
		}
		
		if(!tileElement.getAttribute("isGround").isEmpty()){
			tile.makeGround(Boolean.parseBoolean(tileElement.getAttribute("isGround")));
		}
		 
		if(!tileElement.getAttribute("seeThrough").isEmpty()){
			tile.makeSeeTrough(Boolean.parseBoolean(tileElement.getAttribute("isGround")));
		}
		 
		if(!tileElement.getAttribute("color").isEmpty()){		
			String color = tileElement.getAttribute("color");
			String[] rgb = color.split("-");
						
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			
			Color change_color = new Color(r,g,b);
		    tile.setColor(change_color);
		}
		 
		if(!action.isEmpty()){
			tile.setAction(action);
		}

		if(!details.isEmpty()){
		 	tile.setDetails(details);
		}
		
		if(glyphC != ' ' || glyphN > 0){
			tile.setGlyph((char) (glyphN > 0 ? glyphN : glyphC));
		}
		 
		return tile;
	}
}
