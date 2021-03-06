package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class StringUtils {
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static Point pointBetweenPoints(int x1, int y1, int x2, int y2){
		return new Point((int)((x1 + x2) * 0.5), (int)((y1 + y2) * 0.5), 0);
	}
	
	public static int positionBetweenCoordinates(int x1, int x2){
		return (int)((x1 + x2) * 0.5);
	}
	
	public static int positionBetweenCoordinates(int x1, int x2, String text){
		return (int)(((x1 + x2) * 0.5) - text.length() * 0.5);
	}
	
	
	public static Point pointBetweenPoints(int x1, int y1, int x2, int y2, String text){
		return new Point((int)(((x1 + x2) * 0.5) - text.length() * 0.5), (int)((y1 + y2) * 0.5), 0);
	}
	
	public static boolean hasPunctuation(String message){
		return message.endsWith(".") ||  message.endsWith("!") ||  message.endsWith("?") ||  message.endsWith("]")
				 ||  message.endsWith(")")  ||  message.endsWith("}");
	}
	
	public static String speedToString(int speed){
		if(speed <= 0)
			return "inmovil";
		
		if(speed > 0 && speed <= 50)
			return "atletico";
		
		if(speed > 50 && speed <= 100)
			return "rapido";
		
		if(speed > 100 && speed <= 200)
			return "normal";
		
		if(speed > 200 && speed <= 300)
			return "lento";
		
		if(speed > 300)
			return "muy lento";
		
		return "inmovil";
	}
	
	public static ArrayList<Message> splitPhraseByLimit(Message text, int limit){
		String[] words = text.message().split(" ");
		ArrayList<Message> array = new ArrayList<Message>();
		int i = 0;
		while (words.length > i) {
		    String line = "";
		    while ( words.length > i && line.length() + words[i].length() < limit ) {
		        line += " "+words[i];
		        i++;
		    }
		    array.add(new Message(line, text.color()));
		}
		return array;
	}
	
	public static ArrayList<Message> splitPhraseByLimit(String text, int limit){
		String[] words = text.split(" ");
		ArrayList<Message> array = new ArrayList<Message>();
		int i = 0;
		while (words.length > i) {
		    String line = "";
		    while ( words.length > i && line.length() + words[i].length() < limit ) {
		        line += " "+words[i];
		        i++;
		    }
		    array.add(new Message(line, Color.WHITE));
		}
		return array;
	}
	
	/**
	 * @param position El texto con la posiscion del brazo
	 * @param additive Si "true" a�ade "de" al genero (de el, del / de la, la)
	 * 
	 * Entregas una posicion del cuerpo y le a�ade un posesivo y el genero
	 * */
	public static String genderizeBodyPosition(String position, String additive){
		if(position == "brazo" || position =="pecho") {
			return (additive == "tu" ? "tu " : additive == "de" ? "del " : "el ") + position;
		}else{
			return (additive == "tu" ? "tu " : additive == "de" ? "de la " : "la ") + position;
		}
	}
	
	/**
	 * @param text El texto a formatear
	 * @param creature La criatura de referencia
	 * 
	 * Formatea el texto de la siguiente manera:
	 * text = "golpea"
	 * "te golpea" (si la referencia es el player)
	 * "golpea a el lobo" / "golpea a la cucaracha" (si la referencia es una criatura comun"
	 * */
	public static String formatTextToGender(String text, Creature creature){
		return creature.isPlayer() ? "te " + text : text + " " + (creature.gender() == 'M' ? "al " + creature.name() : "a la " + creature.name());
	}
	
	/**
	 * 
	 * @param text El texto a formatear
	 * @param creature La criatura de referencia
	 * @param effect puede ser "s" o "S" para a�adirle la S en caso de que sea el player
	 * @param effect puede ser "d" para a�adir "de" en lugar de "al / a la"
	 * @return
	 */
	public static String formatTextToGender(String text, Creature creature, String effect){		
		if(effect.indexOf("s") != -1 && !creature.isPlayer())
			text = makeSecondPerson(text, false);
		if(effect.indexOf("S") != -1 && !creature.isPlayer())
			text = makeSecondPerson(text, true);
		
		return creature.isPlayer() ? (effect.indexOf("t") != -1 ? "te " : "tu ") + text : text + " " + (creature.gender() == 'M' ? (effect.indexOf("d") == -1 ? "al " : "del ") + 
				creature.name() :  (effect.indexOf("d") == -1 ? "a la " : "de la ") + creature.name());
	}
	
	/**
	 * @param gender El genero de la palabra
	 * @param text Texto a genderizar (de el, del / de la, la)
	 * @param possesive A�ade un posesivo (de)
	 * 
	 * Entregas un texto y le agrega posesivo y genero
	 * */
	public static String genderizeCreature(char gender, String text, boolean possesive){
		if(gender == 'M') 
			return (possesive ? "del " : "el ") + text;
		else
			return (possesive ? "de la " : "la ") + text;
	}
	
	/**
	 * @param text Texto a convertir
	 * @param capitalize Si true devuelve en mayuscula
	 * 
	 * Vuelve en segunda persona el texto que envias
	 * */
	public static String makeSecondPerson(String text, boolean capitalize){
		String[] words = text.split(" ");
		
		if(words[0].endsWith(",")){
			words[0] = words[0].substring(0, words[0].length()-1) + "s,";
		}else{
			words[0] = words[0] + "s";
		}
		
		if(capitalize)
			words[0] = capitalize(words[0]);
		
		StringBuilder builder = new StringBuilder();
		for (String word : words){
			builder.append(" ");
			builder.append(word);
		}
		
		return builder.toString().trim();
	}
	
	public static String capitalize(String text){
		return Character.toUpperCase(text.charAt(0))+""+text.substring(1);
	}
	/**
	 * 
	 * @param g (el genero en char, puede ser 'M' o 'F', mayuscula o minuscula)
	 * @param knowsObject (si false devuelve apersonales un - una)
	 * @param isPlayer (en lugar de su, el la)
	 * @return Devuelve el articulo correspondiente al genero, si la accion
	 * ocurre en una criatura en lugar del player devuelve el posesivo "su".
	 */
	public static String checkGender(char g, boolean knowsObject, boolean isPlayer){
		g = Character.toUpperCase(g);
		
		if(knowsObject){
			if(isPlayer){
				return (g == 'M' ? "el" : "la");
			}else{
				return "su";
			}
		}else{
			return (g == 'M' ? "un" : "una");
		}
	}
	
	public static String replaceNTilde(String text){
		return text.replace('�', (char)164).replace('�', (char)165);
	}
}
