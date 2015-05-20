package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import rltut.ai.CreatureAi;
import rltut.screens.Screen;

public class Creature {
	private World world;
	public World getWorld() { return world; }
	public void setWorld(World world) { this.world = world; }
	
	public int x;
	public int y;
	public int z;
	
	private char gender;
	public char gender() { return gender; }
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private Color statusColor;
	public Color statusColor() { return statusColor; }
	public void modifyStatusColor(Color newColor) { this.statusColor = newColor; }
	
	private CreatureAi ai;
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	public CreatureAi getCreatureAi() { return ai; }
	
	private String job;
	public String job() { return job; }
	public void setJob(String job) { this.job = job; }
	
	private int maxHp;
	public int maxHp() { return maxHp; }
	public void modifyMaxHp(int amount) { maxHp += amount; }
	
	private int hp;
	public int hp() { return hp; }
	
	private int attackValue;
	public void modifyAttackValue(int value) { attackValue += value; }
	public int attackValue() { 
		return attackValue
			+ (weapon == null ? 0 : weapon.attackValue())
			+ (armor == null ? 0 : armor.attackValue());
	}

	private int defenseValue;
	public void modifyDefenseValue(int value) { defenseValue += value; }
	public int defenseValue() { 
		return defenseValue
			+ (weapon == null ? 0 : weapon.defenseValue())
			+ (armor == null ? 0 : armor.defenseValue());
	}

	private int visionRadius;
	public void modifyVisionRadius(int value) { visionRadius += value; }
	public int visionRadius() { return visionRadius; }

	private String name;
	public String name() { return name; }

	private Inventory inventory;
	public Inventory inventory() { return inventory; }

	private int maxFood;
	public int maxFood() { return maxFood; }
	
	private int food;
	public int food() { return food; }
	
	private Item weapon;
	public Item weapon() { return weapon; }
	
	private Item armor;
	public Item armor() { return armor; }
	
	private int xp;
	public int xp() { return xp; }
	public void modifyXp(int amount) { 
		xp += amount;
		
		notify("%s %d puntos de xp.", amount < 0 ? "Obtienes" : "Pierdes", amount);
		
		while (xp > (int)(Math.pow(level, 1.75) * 25)) {
			level++;
			doAction("avanza al nivel %d", level);
			ai.onGainLevel();
			modifyHp(level * 2, "Muerte por tenes un nivel negativo?");
		}
	}
	
	private int level;
	public int level() { return level; }
	
	private int regenHpCooldown;
	private int regenHpPer1000;
	public void modifyRegenHpPer1000(int amount) { regenHpPer1000 += amount; }
	
	private List<Effect> effects;
	public List<Effect> effects(){ return effects; }
	
	private int maxMana;
	public int maxMana() { return maxMana; }
	public void modifyMaxMana(int amount) { maxMana += amount; }
	
	private int mana;
	public int mana() { return mana; }
	public void modifyMana(int amount) { mana = Math.max(0, Math.min(mana+amount, maxMana)); }
	
	private int regenManaCooldown;
	private int regenManaPer1000;
	public void modifyRegenManaPer1000(int amount) { regenManaPer1000 += amount; }
	
	private int actionPoints;
	public int getActionPoints() { return actionPoints; }
	public void modifyActionPoints(int actionPoints) { this.actionPoints += actionPoints; }
	
	private String causeOfDeath;
	public String causeOfDeath() { return causeOfDeath; }
	
	private Screen shopScreen;
	public Screen shopScreen() { return shopScreen; }
	public void setShopScreen(Screen shop) { this.shopScreen = shop; }
	
	public Creature(World world, char glyph, char gender, Color color, String name, int maxHp, int attack, int defense){
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.visionRadius = 9;
		this.name = name;
		this.inventory = new Inventory(20);
		this.maxFood = 1000;
		this.food = maxFood / 3 * 2;
		this.level = 1;
		this.regenHpPer1000 = 10;
		this.effects = new ArrayList<Effect>();
		this.maxMana = 5;
		this.mana = maxMana;
		this.regenManaPer1000 = 20;
		this.job = name;
		this.gender = gender;
	}
	
	/**
	 * 
	 * @param g (el genero en char, puede ser 'M' o 'F', mayuscula o minuscula)
	 * @param knowsObject (si false devuelve apersonales un - una)
	 * @return Devuelve el articulo correspondiente al genero, si la accion
	 * ocurre en una criatura en lugar del player devuelve el posesivo "su".
	 */
	public String checkGender(char g, boolean knowsObject){
		g = Character.toUpperCase(g);
		
		if(knowsObject){
			if(this.isPlayer){
				return (g == 'M' ? "el" : "la");
			}else{
				return "su";
			}
		}else{
			return (g == 'M' ? "un" : "una");
		}
	}
	
	public void moveBy(int mx, int my, int mz){
		if (mx==0 && my==0 && mz==0)
			return;
		
		Tile tile = world.tile(x+mx, y+my, z+mz);
		
		if (mz == -1){
			if (tile.interaction() == Tile.Interaction.STAIRS_DOWN) {
				doAction("sube las escaleras hacia el piso %d", z+mz+1);
			} else {
				return;
			}
		} else if (mz == 1){
			if (tile.interaction() == Tile.Interaction.STAIRS_UP) {
				doAction("baja las escaleras hacia el piso %d", z+mz+1);
			} else {
				return;
			}
		}
		
		if(tile.getPortalTo() != null &&
				!tile.getPortalTo().isEmpty()){
			doAction("mueve a un nuevo mapa: %s", tile.getPortalTo().toLowerCase());

			this.world = new MapLoader()
							.preBuild(tile.getPortalTo());
					
			this.x = tile.getPortalPos().x > 0 ? tile.getPortalPos().x : x;
			this.y = tile.getPortalPos().y > 0 ? tile.getPortalPos().y : y;
			this.z = tile.getPortalPos().z > 0 ? tile.getPortalPos().z : z;
			
			tile = world.tile(x, y, z);
			ai.onEnter(x, y, z, tile);
			
			return;
		}
		
		if(tile.interaction() == Tile.Interaction.CHANGES && !tile.isGround()){
			if(tile.change_to() != null){
				world.changeTile(x+mx, y+my, z+mz, tile.change_to());
			}
			doAction(tile.action());
			return;
		}
		
		/*if(tile.isClosedDoor()){
			open(x+mx, y+my, z+mz);
			return;
		}*/
		
		Creature other = world.creature(x+mx, y+my, z+mz);
		
		modifyFood(-1);
		
		if (other == null)
			ai.onEnter(x+mx, y+my, z+mz, tile);
		else
			other.ai.onTalkedTo(this);
			//meleeAttack(other);
	}

	public void meleeAttack(Creature other){
		commonAttack(other, attackValue(), "ataca al %s golpeando por %d", other.name);
	}

	private void throwAttack(Item item, Creature other) {
		commonAttack(other, attackValue / 2 + item.thrownAttackValue(), "arroja "+ checkGender(item.gender(), true) +" %s al %s golpeando por %d", nameOf(item), other.name);
		other.addEffect(item.quaffEffect());
	}
	
	public void rangedWeaponAttack(Creature other){
		commonAttack(other, attackValue / 2 + weapon.rangedAttackValue(), "dispara "+ checkGender(weapon.gender(), true) +" %s al %s golpeando por %d", nameOf(weapon), other.name);
	}
	
	private void commonAttack(Creature other, int attack, String action, Object ... params) {
		modifyFood(-2);
		
		int amount = Math.max(0, attack - other.defenseValue());
		
		amount = (int)(Math.random() * amount) + 1;
		
		Object[] params2 = new Object[params.length+1];
		for (int i = 0; i < params.length; i++){
			params2[i] = params[i];
		}
		params2[params2.length - 1] = amount;
		
		doAction(action, params2);
		
		other.modifyHp(-amount, "Asesinado por " + checkGender(gender, false) + " " + name);
		
		if (other.hp < 1)
			gainXp(other);
	}
	
	public void gainXp(Creature other){
		int amount = other.maxHp 
			+ other.attackValue() 
			+ other.defenseValue()
			- level;
		
		if (amount > 0)
			modifyXp(amount);
	}

	public void modifyHp(int amount, String causeOfDeath) { 
		hp += amount;
		this.causeOfDeath = causeOfDeath;
		
		if (hp > maxHp) {
			hp = maxHp;
		} else if (hp < 1) {
			doAction("perece");
			leaveCorpse();
			world.remove(this);
		}
	}
	
	private void leaveCorpse(){
		Item corpse = new Item('%', 'M', color, "cadaver de" + name, null);
		corpse.modifyFoodValue(maxHp * 5);
		world.addAtEmptySpace(corpse, x, y, z);
		for (Item item : inventory.getItems()){
			if (item != null)
				drop(item);
		}
	}
	
	public void open(int wx, int wy, int wz) {
		modifyFood(-10);
		//world.useDoor(wx, wy, wz);
		doAction("abre la puerta");
	}
	
	public void dig(int wx, int wy, int wz) {
		modifyFood(-10);
		//world.dig(wx, wy, wz);
		doAction("cava");
	}
	
	public void update(){
		modifyFood(-1);
		regenerateHealth();
		regenerateMana();
		updateEffects();
		ai.onUpdate();
	}
	
	private void updateEffects(){
		List<Effect> done = new ArrayList<Effect>();
		
		for (Effect effect : effects){
			effect.update(this);
			if (effect.isDone()) {
				effect.end(this);
				done.add(effect);
			}
		}
		
		effects.removeAll(done);
	}
	
	private void regenerateHealth(){
		regenHpCooldown -= regenHpPer1000;
		if (regenHpCooldown < 0){
			if (hp < maxHp){
				modifyHp(1, "Mueres por curarte vida?");
				modifyFood(-1);
			}
			regenHpCooldown += 1000;
		}
	}

	private void regenerateMana(){
		regenManaCooldown -= regenManaPer1000;
		if (regenManaCooldown < 0){
			if (mana < maxMana) {
				modifyMana(1);
				modifyFood(-1);
			}
			regenManaCooldown += 1000;
		}
	}
	
	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}

	public void notify(String message, Object ... params){
		Message newMessage = new Message(String.format(message, params));
		ai.onNotify(newMessage);
	}
	
	public void notify(Color color, String message, Object ... params){
		Message newMessage = new Message(String.format(message, params), color);
		ai.onNotify(newMessage);
	}
	
	public void talkAction(Color color, String message, Object ... params){
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(color, makeSecondPerson(message, false) + ".", params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " : "La " + name);
				other.notify(color, String.format("%s %s.", nombre, message), params);
			}
		}
	}
	
	public void doAction(String message, Object ... params){
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(makeSecondPerson(message, true) + ".", params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " + name : "La " + name);
				other.notify(String.format("%s %s.", nombre, message), params);
			}
		}
	}
		
	public void doAction(Item item, String message, Object ... params){
		if (hp < 1)
			return;
		
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(makeSecondPerson(message, false) + ".", params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " : "La " + name);
				other.notify(String.format("%s %s.", nombre, message), params);
			}
			other.learnName(item);
		}
	}
	
	public List<Creature> getCreaturesWhoSeeMe(){
		List<Creature> others = new ArrayList<Creature>();
		int r = 9;
		for (int ox = -r; ox < r+1; ox++){
			for (int oy = -r; oy < r+1; oy++){
				if (ox*ox + oy*oy > r*r)
					continue;
				
				Creature other = world.creature(x+ox, y+oy, z);
				
				if (other == null)
					continue;
				
				others.add(other);
			}
		}
		return others;
	}
	
	private String capitalize(String text){
		return Character.toUpperCase(text.charAt(0))+""+text.substring(1);
	}
	
	private String makeSecondPerson(String text, boolean capitalize){
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
	
	public boolean canSee(int wx, int wy, int wz){
		return (detectCreatures > 0 && world.creature(wx, wy, wz) != null
				|| ai.canSee(wx, wy, wz));
	}

	public Tile realTile(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz);
	}
	
	public Tile tile(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.tile(wx, wy, wz);
		else
			return ai.rememberedTile(wx, wy, wz);
	}

	public Creature creature(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.creature(wx, wy, wz);
		else
			return null;
	}
	
	public void pickup(){
		Item item = world.item(x, y, z);
		
		if (inventory.isFull() || item == null){
			doAction("agarra la nada");
		} else {
			doAction("levanta "+ checkGender(item.gender(), false) +" %s", nameOf(item));
			world.remove(x, y, z);
			inventory.add(item);
		}
	}
	
	public void drop(Item item){
		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("suelta "+ checkGender(item.gender(), true) +" %s", nameOf(item));
			inventory.remove(item);
			unequip(item);
		} else {
			notify("No hay lugar donde soltar"+ checkGender(item.gender(), true) +" %s.", nameOf(item));
		}
	}
	
	public void modifyFood(int amount) { 
		food += amount;
		
		if (food > maxFood) {
			maxFood = (maxFood + food) / 2;
			food = maxFood;
			notify("No puedes creer que tu estomago soporte tanto!");
			modifyHp(-1, "Muerte por comer demasiado.");
		} else if (food < 1 && isPlayer()) {
			modifyHp(-1000, "Muerto de hambre.");
		}
	}
	
	private boolean isPlayer;
	public boolean isPlayer(){ return isPlayer; }
	public void makePlayer() { this.isPlayer = true; }
	
	public void eat(Item item){
		doAction("come " + checkGender(item.gender(), true) + " " + nameOf(item));
		consume(item);
	}
	
	public void quaff(Item item){
		doAction("bebe " + checkGender(item.gender(), true) + " " + nameOf(item));
		consume(item);
	}
	
	private void consume(Item item){
		if (item.foodValue() < 0)
			notify("Asqueroso!");
		
		addEffect(item.quaffEffect());
		
		modifyFood(item.foodValue());
		getRidOf(item);
	}
	
	private void addEffect(Effect effect){
		if (effect == null)
			return;
		
		effect.start(this);
		effects.add(effect);
	}
	
	private void getRidOf(Item item){
		inventory.remove(item);
		unequip(item);
	}
	
	private void putAt(Item item, int wx, int wy, int wz){
		inventory.remove(item);
		unequip(item);
		world.addAtEmptySpace(item, wx, wy, wz);
	}
	
	public void unequip(Item item){
		if (item == null)
			return;
		
		if (item == armor){
			if (hp > 0)
				doAction("remueve " + checkGender(item.gender(), true) + " " + nameOf(item));
			armor = null;
		} else if (item == weapon) {
			if (hp > 0) 
				doAction("guarda " + checkGender(item.gender(), true) + " " + nameOf(item));
			weapon = null;
		}
	}
	
	public void equip(Item item){
		if (!inventory.contains(item)) {
			if (inventory.isFull()) {
				notify("No puedes equipar "+ checkGender(item.gender(), true) + " %s ya que el inventario esta lleno!", nameOf(item));
				return;
			} else {
				world.remove(item);
				inventory.add(item);
			}
		}
		
		if (item.attackValue() == 0 && item.rangedAttackValue() == 0 && item.defenseValue() == 0)
			return;
		
		if (item.attackValue() + item.rangedAttackValue() >= item.defenseValue()){
			unequip(weapon);
			doAction("empunia " +  checkGender(item.gender(), true) + " " + nameOf(item));
			weapon = item;
		} else {
			unequip(armor);
			doAction("viste " +  checkGender(item.gender(), true) + " " + nameOf(item));
			armor = item;
		}
	}
	
	public Item item(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.item(wx, wy, wz);
		else
			return null;
	}
	
	public String details() {
		return String.format("  level:%d  attack:%d  defense:%d  hp:%d", level, attackValue(), defenseValue(), hp);
	}
	
	public void throwItem(Item item, int wx, int wy, int wz) {
		Point end = new Point(x, y, 0);
		
		for (Point p : new Line(x, y, wx, wy)){
			if (!realTile(p.x, p.y, z).isGround())
				break;
			end = p;
		}
		
		wx = end.x;
		wy = end.y;
		
		Creature c = creature(wx, wy, wz);
		
		if (c != null)
			throwAttack(item, c);				
		else
			doAction("arroja "+ checkGender(item.gender(), true) +" %s", nameOf(item));
		
		if (item.quaffEffect() != null && c != null)
			getRidOf(item);
		else
			putAt(item, wx, wy, wz);
	}
	
	public void summon(Creature other) {
		world.add(other);
	}
	
	private int detectCreatures;
	public void modifyDetectCreatures(int amount) { detectCreatures += amount; }
	
	public void castSpell(Spell spell, int x2, int y2) {
		Creature other = creature(x2, y2, z);
		
		if (spell.manaCost() > mana){
			doAction("apunta y murmulla pero nada ocurre");
			return;
		} else if (other == null) {
			doAction("apunta y murmulla a la nada");
			return;
		}
		
		other.addEffect(spell.effect());
		modifyMana(-spell.manaCost());
	}
	
	public String nameOf(Item item){
		return ai.getName(item);
	}
	
	public void learnName(Item item){
		notify(capitalize(checkGender(item.gender(), true)) + item.appearance() + " es " + checkGender(item.gender(), false) + " " + item.name() + "!");
		ai.setName(item, item.name());
	}
}
