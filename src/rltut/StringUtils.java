package rltut;

public class StringUtils {
	
	/**
	 * @param text Texto a convertir
	 * @param capitalize Si true devuelve en mayuscula
	 * 
	 * Vuelve en segunda persona el texto que envias
	 * */
	public static String makeSecondPerson(String text, boolean capitalize){
		String[] words = text.split(" ");
		words[0] = words[0] + "s";
		
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
