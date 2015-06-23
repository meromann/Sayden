package rltut;

import rltut.Item.ItemType;

public class BodyPart {
	//creature.force_drop(new Item(ItemType.STATIC,(char)191, 'M', creature.color(), "brazo separado", null));
	public static final BodyPart HEAD = new BodyPart("cabeza", "HEAD");
	public static final BodyPart CHEST = new BodyPart("pecho", "CHEST");
	public static final BodyPart BACK = new BodyPart("espalda", "BACK");
	public static final BodyPart ARMS = new BodyPart("brazo", "ARM");
	public static final BodyPart LEGS = new BodyPart("pierna", "LEG");
	
	public static final BodyPart DER_ARM = new BodyPart("brazo derecho", "ARM"){
		public void onRemove(Creature creature) {
			creature.drop(creature.shield());
			creature.setShield(
				new Item(ItemType.UNEQUIPPABLE, 'M', "brazo inhabilitado").addDamageType(DamageType.BLUNT, 1)
			);
		}
	};
	public static final BodyPart IZQ_ARM = new BodyPart("brazo izquierdo", "ARM"){
		public void onRemove(Creature creature) {
			creature.drop(creature.weapon());
			creature.setWeapon(
				new Item(ItemType.UNEQUIPPABLE, 'M', "brazo inhabilitado").addDamageType(DamageType.BLUNT, 1)
			);
		}
	};
	public static final BodyPart DER_LEG = new BodyPart("pierna derecha", "LEG"){
		public void onRemove(Creature creature) {
			//creature.force_drop(new Item(ItemType.STATIC,(char)192, 'F', creature.color(), "pierna separada", null));
			creature.modifyMovementSpeed(Constants.SEV_LEG_PENALTY);
		}
	};
	public static final BodyPart IZQ_LEG = new BodyPart("pierna izquierda", "LEG"){
		public void onRemove(Creature creature) {
			//creature.force_drop(new Item(ItemType.STATIC,(char)192, 'F', creature.color(), "pierna separada", null));
			creature.modifyMovementSpeed(Constants.SEV_LEG_PENALTY);
		}
	};
	
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
