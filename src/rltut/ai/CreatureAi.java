package rltut.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rltut.BodyPart;
import rltut.Creature;
import rltut.DamageType;
import rltut.FieldOfView;
import rltut.Item;
import rltut.LevelUpController;
import rltut.Line;
import rltut.Message;
import rltut.Path;
import rltut.Point;
import rltut.Tile;
import rltut.Wound;

public class CreatureAi {
	protected Creature creature;
	private Map<String, String> itemNames;
	
	private int[] desires = {0,//Aggression
							 0,//Fear
							 0 //Unknown
							 };
	
	private Map<String, Boolean> flags;
	public boolean getFlag(String flag){ return flags.get(flag) == null ? false : flags.get(flag); }
	public void addFlag(String flag, boolean value) { flags.put(flag, value); }
	public void setFlag(String flag, boolean value) { 
		if(getFlag(flag))
			flags.put(flag, value);
		else
			addFlag(flag, value);
	}
	
	public void setNeutral(){
		addFlag("IsHostile", false);
		addFlag("IsNeutral", true);
	}
	
	public void setNpc(){
		addFlag("IsHostile", false);
		addFlag("IsNeutral", true);
		addFlag("IsNpc", true);
	}
	
	public void setHostile(){
		if(creature.isPlayer())
			return;
		addFlag("IsHostile",true);
	}
	
	public boolean isHostileTo(Creature b){
		if(creature == b) return false;
		
		boolean aHostile = creature.ai().getFlag("IsHostile");
		boolean bHostile = b.ai().getFlag("IsHostile");
		
		if(aHostile&&bHostile) return false;
		if(creature.isPlayer()&&bHostile) return true;
		if(b.isPlayer()&&aHostile) return true;
		if(creature.ai().getFlag("IsInsane") || b.ai().getFlag("IsInsane")) return true;
		if(creature.name() == b.name()) return false;
		if(creature.ai().getFlag("IsNeutral") || creature.ai().getFlag("IsNeutral")) return false;
		
		if ((aHostile)&&(!bHostile)) return true;
		if ((bHostile)&&(!aHostile)) return true;
		
		return false;
	}
	
	/**
	 * @param desire
	 * 0 Aggression
	 * 1 Fear
	 * 2 Unkonw
	 * */
	public int getDesire(int desire) { return desires[desire]; }
	/**
	 * @param desire
	 * 0 Aggression
	 * 1 Fear
	 * 2 Unkonw
	 * */
	public void modifyDesire(int desire, int amount) { this.desires[desire] += amount; }
	
	public void propagate(int desire, int amount){
		for(Creature c : creature.getCreaturesWhoSeeMe()){
			if(c.name().indexOf(creature.originalName()) != -1)
				c.ai().modifyDesire(desire, amount);
		}
	}
	
	public Wound getWound(DamageType type, BodyPart bodyPart, Creature target) { return null; }
	
	public Wound getWoundAttack(DamageType type, BodyPart bodyPart, Creature target) { return null; }
	
	public CreatureAi(Creature creature){
		this.creature = creature;
		this.creature.setCreatureAi(this);
		this.itemNames = new HashMap<String, String>();
		this.flags = new HashMap<String, Boolean>();
	}
	
	public String getName(Item item){
		String name = itemNames.get(item.name());
		
		return name == null ? item.appearance() : name;
	}
	
	public void setName(Item item, String name){
		itemNames.put(item.name(), name);
	}
	
	public void onTalkTo(Creature talker){	}
	
	/** Esta funcion se llama luego de que una unidad entre en una casilla*/
	public void onEnter(int x, int y, int z, Tile tile){
		if (tile.isGround()){
			creature.x = x;
			creature.y = y;
			creature.z = z;
			
			for (int i = 0; i < creature.wounds().size(); i++){
				Wound wound = creature.wounds().get(i);
				wound.onMove(creature);
			}
		}
	}
			
	/** Esta funcion se llama antes de que una unidad efectue un ataque sobre otra*/
	public void onBeforeAttack(Creature target){}
	
	/** Esta funcion se llama luego de que una unidad efectue un ataque sobre otra satisfactoriamente*/
	public void onAttack(Creature target){}
	
	public void onGetAttacked(Creature attacker){}
	
	
	/** Esta funcion se llama luego de que el player toca una tecla*/
	public void onUpdate(){}
	
	/** Esta funcion se llama luego de que el player toca una tecla*/
	public void updateFow(FieldOfView fov){}
	
	/** Esta funcion se llama al notificar algo a la criatura*/
	public void onNotify(Message message){}
	
	public boolean canSee(int wx, int wy, int wz) {
		if (creature.z != wz)
			return false;
		
		if ((creature.x-wx)*(creature.x-wx) + (creature.y-wy)*(creature.y-wy) > creature.visionRadius()*creature.visionRadius())
			return false;
		
		for (Point p : new Line(creature.x, creature.y, wx, wy)){
			if (creature.realTile(p.x, p.y, wz).isGround() || p.x == wx && p.y == wy)
				continue;
			
			return false;
		}
		
		return true;
	}
	
	public void wanderOn(Tile tile){
		int mx = (int)(Math.random() * 3) - 1;
		int my = (int)(Math.random() * 3) - 1;
		
		Creature other = creature.creature(creature.x + mx, creature.y + my, creature.z);
		
		if (other != null && other.name().equals(creature.name()) 
				|| !creature.tile(creature.x+mx, creature.y+my, creature.z).isGround()
				|| !(creature.tile(creature.x+mx, creature.y+my, creature.z) == tile))
			return;
		else
			creature.moveBy(mx, my, 0);
	}
	
	public void wander(){
		int mx = (int)(Math.random() * 3) - 1;
		int my = (int)(Math.random() * 3) - 1;
		
		Creature other = creature.creature(creature.x + mx, creature.y + my, creature.z);
		
		if (other != null && other.name().equals(creature.name()) 
				|| !creature.tile(creature.x+mx, creature.y+my, creature.z).isGround())
			return;
		else
			creature.moveBy(mx, my, 0);
	}

	public void onGainLevel() {
		new LevelUpController().autoLevelUp(creature);
	}

	public Tile rememberedTile(int wx, int wy, int wz) {
		return Tile.UNKNOWN;
	}

	protected boolean canThrowAt(Creature other) {
		return creature.canSee(other.x, other.y, other.z)
			&& getWeaponToThrow() != null;
	}

	protected Item getWeaponToThrow() {
		Item toThrow = null;
		
		for (Item item : creature.inventory().getItems()){
			if (item == null || creature.weapon() == item || creature.armor() == item)
				continue;
			//TODO: Arraglar ESTO
			//if (toThrow == null || item.thrownAttackValue() > toThrow.attackValue())
				//toThrow = item;
		}
		
		return toThrow;
	}

	protected boolean canRangedWeaponAttack(Creature other) {
		return creature.weapon() != null
		    //&& creature.weapon().rangedAttackValue() > 0
		    && creature.canSee(other.x, other.y, other.z);
	}

	protected boolean canPickup() {
		return creature.item(creature.x, creature.y, creature.z) != null
			&& !creature.inventory().isFull();
	}

	public void moveTo(Point target) {
		List<Point> points = new Path(creature, target.x, target.y).points();
		
		if(points.isEmpty())
			return;
		
		int mx = points.get(0).x - creature.x;
		int my = points.get(0).y - creature.y;
		
		if(mx != 0 && my != 0){
			if(Math.random() > 0.5f){
				mx = 0;
			}else{
				my = 0;
			}
		}
		
		creature.moveBy(mx, my, 0);
	}
	
	public void moveTo(int x, int y) {
		List<Point> points = new Path(creature, x, y).points();
		
		int mx = points.get(0).x - creature.x;
		int my = points.get(0).y - creature.y;
		
		if(mx != 0 && my != 0){
			if(Math.random() > 0.5f){
				mx = 0;
			}else{
				my = 0;
			}
		}
		
		creature.moveBy(mx, my, 0);
	}
	
	public void flee(Creature target){
		List<Point> points = new Path(creature, target.x, target.y).points();
		
		int mx = points.get(0).x - creature.x;
		int my = points.get(0).y - creature.y;
		
		if(mx != 0 && my != 0){
			if(Math.random() > 0.5f){
				mx = 0;
			}else{
				my = 0;
			}
		}
		
		creature.moveBy(-mx, -my, 0);
	}
	
	public void hunt(Creature target) {
		List<Point> points = new Path(creature, target.x, target.y).points();
		
		if(points.isEmpty())
			return;
		
		int mx = points.get(0).x - creature.x;
		int my = points.get(0).y - creature.y;
		
		if(mx != 0 && my != 0){
			if(Math.random() > 0.5f){
				mx = 0;
			}else{
				my = 0;
			}
		}
		
		creature.moveBy(mx, my, 0);
	}

	protected boolean canUseBetterEquipment() {
		/* TODO: Arreglar esto tambien
		int currentWeaponRating = creature.weapon() == null ? 0 : creature.weapon().attackValue() + creature.weapon().rangedAttackValue();
		int currentArmorRating = creature.armor() == null ? 0 : creature.armor().defenseValue();
		
		for (Item item : creature.inventory().getItems()){
			if (item == null)
				continue;
			
			boolean isArmor = item.attackValue() + item.rangedAttackValue() < item.defenseValue();
			
			if (item.attackValue() + item.rangedAttackValue() > currentWeaponRating
					|| isArmor && item.defenseValue() > currentArmorRating)
				return true;
		}*/
		
		return false;
	}

	protected void useBetterEquipment() {
		/* TODO: Arreglar esto tambien e!
		int currentWeaponRating = creature.weapon() == null ? 0 : creature.weapon().attackValue() + creature.weapon().rangedAttackValue();
		int currentArmorRating = creature.armor() == null ? 0 : creature.armor().defenseValue();
		
		for (Item item : creature.inventory().getItems()){
			if (item == null)
				continue;
			
			boolean isArmor = item.attackValue() + item.rangedAttackValue() < item.defenseValue();
			
			if (item.attackValue() + item.rangedAttackValue() > currentWeaponRating
					|| isArmor && item.defenseValue() > currentArmorRating) {
				creature.equip(item);
			}
		}*/
	}
}
