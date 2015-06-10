package rltut;

import java.util.ArrayList;

public class StringUtils {
	
	public static String woundsToString(Creature creature){
		if(creature.hp() < creature.woundSeverity() && creature.getWorstWound() >= 3)
			return "Muerto";
		
		if(creature.woundSeverity() == 0)
			return creature.inmaculado() ? "Inmaculado" : "Sano";
		
		if(creature.hp() * 0.5f >= creature.woundSeverity() && creature.getWorstWound() <= 2)
			return "Lastimado";
		
		if(creature.hp() * 0.5f >= creature.woundSeverity() && creature.getWorstWound() <= 4 && creature.getWorstWound() > 2)
			return "Malherido";
		
		if(creature.hp() >= creature.woundSeverity() && creature.hp() * 0.5f < creature.woundSeverity()
				&& creature.getWorstWound() <= 4 && creature.getWorstWound() > 2)
			return "Moribundo";
		
		return "Sano";
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
	
	/**
	 * @param position El texto con la posiscion del brazo
	 * @param additive Si "true" añade "de" al genero (de el, del / de la, la)
	 * 
	 * Entregas una posicion del cuerpo y le añade un posesivo y el genero
	 * */
	public static String genderizeBodyPosition(String position, String additive){
		if(position == "brazo" || position =="pecho") 
			return (additive == "de" ? "del " : "el ") + position;
		else
			return (additive == "de" ? "de la " : "la ") + position;
	}
	
	/**
	 * @param gender El genero de la palabra
	 * @param text Texto a genderizar (de el, del / de la, la)
	 * @param possesive Añade un posesivo (de)
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
}
