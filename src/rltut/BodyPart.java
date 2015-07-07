package rltut;

import rltut.Item.ItemType;

public class BodyPart {
	//creature.force_drop(new Item(ItemType.STATIC,(char)191, 'M', creature.color(), "brazo separado", null));
	public static final BodyPart HEAD = new BodyPart("cabeza");
	public static final BodyPart CHEST = new BodyPart("pecho");
	public static final BodyPart BACK = new BodyPart("espalda");
	public static final BodyPart ARMS = new BodyPart("brazo");
	public static final BodyPart LEGS = new BodyPart("pierna");
	
	public static final BodyPart DER_ARM = new BodyPart("brazo derecho"){
		public void onRemove(Creature creature) {
			creature.drop(creature.shield());
			creature.setShield(
				new Item(ItemType.UNEQUIPPABLE, 'M', "brazo inhabilitado").addDamageType(DamageType.BLUNT, 1)
			);
		}
	};
	public static final BodyPart IZQ_ARM = new BodyPart("brazo izquierdo"){
		public void onRemove(Creature creature) {
			creature.drop(creature.weapon());
			creature.setWeapon(
				new Item(ItemType.UNEQUIPPABLE, 'M', "brazo inhabilitado").addDamageType(DamageType.BLUNT, 1)
			);
		}
	};
	public static final BodyPart DER_LEG = new BodyPart("pierna derecha"){
		public void onRemove(Creature creature) {
			//creature.force_drop(new Item(ItemType.STATIC,(char)192, 'F', creature.color(), "pierna separada", null));
			creature.modifyMovementSpeed(Constants.SEV_LEG_PENALTY);
		}
	};
	public static final BodyPart IZQ_LEG = new BodyPart("pierna izquierda"){
		public void onRemove(Creature creature) {
			//creature.force_drop(new Item(ItemType.STATIC,(char)192, 'F', creature.color(), "pierna separada", null));
			creature.modifyMovementSpeed(Constants.SEV_LEG_PENALTY);
		}
	};
	
	private int piercingLvl;
	public int piercingLvl() { return piercingLvl; }
	public void modifyPiercingLvl(int amount) { this.piercingLvl += amount; }
	public void setPiercingLvl(int set) { this.piercingLvl = set; }
	
	private String position;
	public String position() { return position; }
	
	public BodyPart(String position){
		this.position = position;
	}
	
	public void onRemove(Creature creature){}
}
