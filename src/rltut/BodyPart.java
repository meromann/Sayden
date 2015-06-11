package rltut;

public class BodyPart {
	//creature.force_drop(new Item(ItemType.STATIC,(char)191, 'M', creature.color(), "brazo separado", null));
	public static final String HEAD = "cabeza";
	public static final String CHEST = "pecho";
	public static final String ARMS = "brazo";
	public static final String LEGS = "pierna";
	public static final String BACK = "espalda";
	
	private String name;
	public String name() { return name; }
	public void modifyName(String newName) { this.name = newName; }
	
	private String position;
	public String position() { return position; }
	
	public BodyPart(String name, String position){
		this.name = name;
		this.position = position;
	}
	
	public BodyPart(BodyPart clone){
		this.name = clone.name();
		this.position = clone.position();
	}
	
	public void onRemove(Creature creature){}
}
