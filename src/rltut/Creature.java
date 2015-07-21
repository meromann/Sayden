package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import asciiPanel.AsciiPanel;
import rltut.ai.CreatureAi;
import rltut.screens.Option;

public class Creature extends Thing{
	private World world;
	public World world() { return world; }
	public void setWorld(World world) { this.world = world; }
	
	public int x;
	public int y;
	public int z;
	
	public Color color() { return (Color) (getData(RPG.STATUS_COLOR) != null ? getData(RPG.STATUS_COLOR) : getData(RPG.COLOR)); }
	
	private CreatureAi ai;
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	public CreatureAi ai() { return ai; }
	
	private String job;
	public String job() { return job; }
	public void setJob(String job) { this.job = job; }
	
	private int hp;
	public int hp() { return hp; }
	
	private int hpRegenerationCooldown;
	
	private int hpRegeneration;
	public int hpRegeneration() { return hpRegeneration; }
	public void modifyHpRegeneration(int amount) { this.hpRegeneration += amount; }
	
	private int maxHp;
	public int maxHp() { return maxHp; }
	public void modifyMaxHp(int amount) { this.maxHp += amount; }
	
	private int woundResistance;
	public int woundResistance() { return hp < 1 ? woundResistance - 2 : woundResistance; }
	public void modifyWoundResistance(int amount) { this.woundResistance += amount; }
	
	private int visionRadius;
	public void modifyVisionRadius(int value) { visionRadius += value; }
	public int visionRadius() { return visionRadius <= 0 ? 1 : visionRadius; }

	private int accuracy;
	public int accuracy() { return accuracy; }
	public void modifyAccuracy(int amount) { this.accuracy += amount; }
	
	private int missChance;
	public int missChance() { return missChance; }
	public void modifyMissChance(int amount) { this.missChance += amount; }
	
	private String originalName;
	public String originalName() { return originalName; } 
	
	private String name;
	public String name() { return name; }
	public void setName(String newName) { this.name = newName; }
	
	public String nameElLa() { return ((char)getData(RPG.GENDER) == 'M' ? "el " : "la ") + name(); }
	public String nameUnUna() { return ((char)getData(RPG.GENDER) == 'M' ? "un " : "una ") + name(); }

	private Inventory inventory;
	public Inventory inventory() { return inventory; }
	
	private Item intrinsicWeapon;
	public Item intrinsicWeapon() { return intrinsicWeapon; }
	
	private Item intrinsicArmor;
	public Item intrinsicArmor() { return intrinsicArmor; }
	
	private Item weapon;
	public Item weapon() { return weapon; }
	public void setWeapon(Item weapon) { this.weapon = weapon; }
	
	private Item armor;
	public Item armor() { return armor; }
	public void setArmor(Item armor) { this.armor = armor; }
	
	private Item helment;
	public Item helment() { return helment; }
	public void setHelment(Item helment) { this.helment = helment; }
	
	private Item shield;
	public Item shield() { return shield; }
	public void setShield(Item shield) { this.shield = shield; }
	
	private List<Effect> effects;
	public List<Effect> effects(){ return effects; }
	
	private int movementSpeed;
	public int movementSpeed() { return movementSpeed
			+ (armor != null ? armor.getIntegerData(RPG.MOVEMENT_SPEED) : 0)
			+ (helment != null ? helment.getIntegerData(RPG.MOVEMENT_SPEED) : 0)
			+ (shield != null ? shield.getIntegerData(RPG.MOVEMENT_SPEED) : 0)
			+ (weapon != null ? weapon.getIntegerData(RPG.MOVEMENT_SPEED) : 0); }
	public void modifyMovementSpeed(int amount) { this.movementSpeed += amount; }
	
	public int attackSpeed;
	public int attackSpeed() { return attackSpeed 
			+ (armor != null ? armor.getIntegerData(RPG.ATTACK_SPEED) : 0)
			+ (helment != null ? helment.getIntegerData(RPG.ATTACK_SPEED) : 0)
			+ (shield != null ? shield.getIntegerData(RPG.ATTACK_SPEED) : 0)
			+ (weapon != null ? weapon.getIntegerData(RPG.ATTACK_SPEED) : 0); }
	public void modifyAttackSpeed(int amount) { this.attackSpeed += amount; }
	
	private Point attackQue = null;
	public Point attackQue() { return attackQue; }
	public void modifyAttackQue(Point p) { this.attackQue = p; }
	
	private String statusEffect;
	public String statusEffect() { return statusEffect; }
	
	private int actionPoints;
	public int getActionPoints() { return actionPoints; }
	public void modifyActionPoints(int actionPoints) { this.actionPoints += actionPoints; }
	public void resetActionPoints() { actionPoints = 0; }
	
	/** @param actionPoints Puntos de accion que añadir o sustraer
	 *  @param status El nombre del estado de la criatura
	 *  
	 *  De ser negativo se sustraen los puntos y se muerstra "esta "+ status (Ej: "esta bajo gran dolor", "esta aturdido")
	 *  cada turno hasta que el efecto se disipe
	 * */
	public void modifyActionPoints(int actionPoints, String status) { this.statusEffect = status; this.actionPoints += actionPoints; }

	private String causeOfDeath;
	public String causeOfDeath() { return causeOfDeath; }
	
	private boolean isPlayer;
	public boolean isPlayer(){ return isPlayer; }
	public void makePlayer() { this.isPlayer = true; }
		
	private List<Wound> wounds;
	public List<Wound> wounds() { return wounds; }
	
	public void addWound(Wound wound, Creature applier){ 
		wound.onApply(this, applier); 
		wounds.add(wound); 
		
		if (hp < 1) {
			doAction("perece");
			leaveCorpse();
			world.remove(this);
		}
	}
	public boolean hasWound(String woundName){
		for(Wound wound : wounds){
			if(wound.name() == woundName)
				return true;
		}
		return false;
	}	
	public boolean hasWoundAt(String woundName, BodyPart part){
		for(Wound wound : wounds){
			if(wound.name() == woundName && wound.bodyPart().position().indexOf(part.position()) != -1)
				return true;
		}
		return false;
	}	
	
	private ArrayList<Option> options;
	public ArrayList<Option> options() { return options; }
	public void addOptions(ArrayList<Option> newOptions) { options.clear(); options.addAll(newOptions); }
	public void addOption(Option newOption){ options.add(newOption); }
	public void clearOptions() { options.clear(); }
	
	private List<BodyPart> limbs;
	public List<BodyPart> limbs() { return limbs; }
	
	public void addBodyPart(BodyPart bodyPart){ this.limbs.add(bodyPart); }
	public void removeBodyPart(BodyPart bodyPart) { limbs.remove(bodyPart); }
	public void removeBodyPart(String bodyPart){
		for(int i = 0; i < limbs.size(); i++){
			if(limbs.get(i).position().indexOf(bodyPart) != -1){
				limbs.get(i).onRemove(this);
				limbs.remove(limbs.get(i));
				dismembered = true;
				return;
			}
		}
	}
	public BodyPart getBodyPart(String bodyPart){
		for(int i = 0; i < limbs.size(); i++){
			if(limbs.get(i).position().indexOf(bodyPart) != -1){
				return limbs.get(i);
			}
		}
		return null;
	}
	/**
	 * 
	 * @param bodyPart La posicion de la parte del cuerpo
	 * @return true o false
	 */
	public boolean hasBodyPart(String bodyPart){
		for(int i = 0; i < limbs.size(); i++){
			if(limbs.get(i).position().indexOf(bodyPart) != -1){
				return true;
			}
		}
		return false;
	}
	private boolean dismembered;
	public boolean dismembered() { return dismembered; }
	
	public Creature(World world, char glyph, char gender, Color color, String name, int maxHp, Item weapon, Item armor){
		this.data = new HashMap<String, Object>();
		setData(RPG.GENDER, gender);
		setData(RPG.NAME, name);
		setData(RPG.ORIGINAL_NAME, name);
		setData(RPG.GLYPH, glyph);
		setData(RPG.COLOR, color);
		setData(RPG.ORIGINAL_COLOR, color);
		
		this.world = world;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.visionRadius = Constants.STARTING_VISION_RADIUS;
		this.name = name;
		this.originalName = name;
		this.inventory = new Inventory(Constants.INVENTORY_SIZE);
		this.effects = new ArrayList<Effect>();
		this.limbs = new ArrayList<BodyPart>();
		this.wounds = new ArrayList<Wound>();
		this.options = new ArrayList<Option>();
		this.intrinsicWeapon = weapon;
		this.intrinsicArmor = armor;
		this.attackSpeed = Constants.STARTING_ATTACK_SPEED;
		this.movementSpeed = Constants.STARTING_MOVE_SPEED;
		this.dismembered = false;
		this.accuracy = 100;
		this.woundResistance = 0;
	}
	
	public void moveBy(int mx, int my, int mz){
		if (mx==0 && my==0 && mz==0 || (isPlayer && getActionPoints() < 0)){
			if(isPlayer){
				if(getActionPoints() < 0){
					doAction(Constants.MESSAGE_STATUS_EFFECT_COLOR, "esta " + statusEffect()); 
					modifyActionPoints(100);
				}
				world.modifyActionPoints(100);
			}else{
				modifyActionPoints(-100);
			}
			return;
		}
		
		if(statusEffect != null && (getActionPoints() > 0 || isPlayer)){
			statusEffect = null;
			unsetData(RPG.STATUS_COLOR);
		}
		
		if(attackQue != null && getActionPoints() >= attackSpeed()){
			Creature queCreature = world.creature(attackQue.x, attackQue.y, attackQue.z);
			if(queCreature == null){
				doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_DODGE);
			}else{
				meleeAttack(queCreature, weapon);
			}
			modifyActionPoints(-attackSpeed());
			attackQue = null;
			unsetData(RPG.BACKGROUND_COLOR);
			return;
		}
		
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
				!tile.getPortalTo().isEmpty()
					&& isPlayer){
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
		
		Creature other = world.creature(x+mx, y+my, z+mz);
		
		if (other == null){
			for (int i = 0; i < wounds.size(); i++){
				Wound wound = wounds.get(i);
				wound.onBeforeMove(this);
			}
			
			if(isPlayer){
				world.modifyActionPoints(movementSpeed());
			}else if(getActionPoints() < movementSpeed()){
				return;
			}else{
				modifyActionPoints(-movementSpeed());
			}
			ai.onEnter(x+mx, y+my, z+mz, tile);
		}else{
			if(isPlayer){
				world.modifyActionPoints(attackSpeed());
			}else if(getActionPoints() <= (attackSpeed() * 0.5f) && getActionPoints() > 0){
				doAction(Constants.MESSAGE_DODGE_COLOR, "se " + Constants.MESSAGE_DODGE_WARNING);
				attackQue = new Point(x+mx, y+my, z+mz);
				setData(RPG.BACKGROUND_COLOR, Constants.MESSAGE_DODGE_COLOR);
				return;
			}else if(getActionPoints() < attackSpeed()){
				return;
			}else{
				modifyActionPoints(-attackSpeed());
			}
			
			if(ai.isHostileTo(other))
				meleeAttack(other, weapon);
			else
				other.ai().onTalkTo(this);
		}
	}

	public void meleeAttack(Creature other, Item object){		
		commonAttack(other, object);
	}

	private void throwAttack(Item item, Creature other) {
		//commonAttack(other, attackValue / 2 + item.thrownAttackValue(), "arroja "+ checkGender(item.gender(), true) +" %s al %s golpeando por %d", nameOf(item), other.name);
		//other.addEffect(item.quaffEffect());
	}
	
	public void rangedWeaponAttack(Creature other){
		//commonAttack(other, attackValue / 2 + weapon.rangedAttackValue(), "dispara "+ checkGender(weapon.gender(), true) +" %s al %s golpeando por %d", nameOf(weapon), other.name);
	}
	
	/**
	 * 
	 * @param other				La criatura a la que se le efectua el golpe
	 * @param damagingObject	El objeto con el que se golpea
	 * 
	 * Esta funcion maneja todos los tipos de ataque
	 */
	private void commonAttack(Creature other, Item damagingObject) {
		BodyPart position = null;					//Posicion del ataque
		DamageType damageType = null;				//Tipo de daño inflinjido (BLUNT, SLICE, etc) Declarados en DamageType
		int damagePower = 0;						//Poder del daño inflinjido (si el arma tiene varios tipos de ataque tomaria el mayor
		int defendingPower = 0;						//Poder de defensa contra el mayor daño, usado para chequear bloqueos
		Item defendingObject = null;				//Objeto con el que la criatura esta intentando defender el ataque
		int chanceToHit = StringUtils.randInt(accuracy() - missChance(), accuracy());	//Chances de fallar el golpe	//TODO: Mejorar formula
		
		if(other.hp() < 0)
			return;
		
		if(chanceToHit <= other.accuracy() * .5f){	//Si las chances de pegar son menores que la mitad del accurancy de tu enemigo
			doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_DODGE);
			return;
		}
		
		for (int i = 0; i < wounds.size(); i++){
			Wound wound = wounds.get(i);
			wound.onBeforeAttack(this);
		}
		
		ai.onBeforeAttack(other);
		
		//Elige el lugar donde se golpea, se toma en cuenta si el enemigo "dispara" un arco (no esta directamente al lado
		//del objetivo del ataque. Tambien se guarda el objeto con el cual el objetivo trata de defender el ataque
		//
		// 		 HEAD
		//		  <-	<
		//BACK	|  @	|	ARMS/CHEST
		//		>  ->
		//	   LEGS/CHEST
		//
		if(x < other.x && y >= other.y){
			position = BodyPart.BACK;
			defendingObject = other.armor();
		}else if(y < other.y && x <= other.x){
			position = BodyPart.HEAD;
			defendingObject = other.helment();
		}else if(x > other.x && y <= other.y){
			if(other.hasBodyPart(BodyPart.ARMS.position())){//TODO: Arreglar esto
				position = (other.dismembered() ? (Math.random() < Constants.DISMEMBER_CHANCE_LIMB_OVER_KILL ? BodyPart.ARMS : BodyPart.CHEST) : BodyPart.ARMS);
				defendingObject = (position == BodyPart.CHEST ? other.armor() : (other.shield() != null ? other.shield() : other.armor()));
			}else{
				position = BodyPart.CHEST;
				defendingObject = other.armor();
			}
		}else if(y > other.y && x >= other.x){
			if(other.hasBodyPart(BodyPart.LEGS.position())){
				position = (other.dismembered() ? (Math.random() < Constants.DISMEMBER_CHANCE_LIMB_OVER_KILL ? BodyPart.LEGS : BodyPart.CHEST) : BodyPart.LEGS);
				defendingObject = (position == BodyPart.CHEST ? other.armor() : (other.shield() != null ? other.shield() : other.armor()));
			}else{
				position = BodyPart.CHEST;
				defendingObject = other.armor();
			}
		}
	
		if(damagingObject == null){
			damagingObject = intrinsicWeapon();
		}
		
		if(defendingObject == null){
			defendingObject = other.intrinsicArmor();
		}
		
		List<DamageType> availableDamageTypes = DamageType.getAvailableDamageTypes();
		
		//Comienza a recorrer todos los ataques posibles del objeto con el que se ataca
		for(int i = 0; i < availableDamageTypes.size(); i++){
			int power = damagingObject.getIntegerData(availableDamageTypes.get(i).wondType());		//Guardamos el poder
			DamageType type = availableDamageTypes.get(i);											//Guardamos el tipo
			
			if(damagingObject.getBooleanData(Flags.IS_BROKEN))
				power = 1;
			
			damageType = type;
			damagePower = power;
			
			//Comienza a recorrer todas las defensas posibles del objetivo

			int defense_power = defendingObject.getIntegerData(damageType.wondType());				//Guardamos el poder
			
			//Si esta defendiendo con "la piel" no lo hace desde la cabeza
			if(defendingObject.getBooleanData(Flags.IS_INTRINSIC) &&
					position == BodyPart.HEAD){
				defense_power = 0;
			}
			
			//Las debilidades a ciertos tipos se resuelven con numeros negativos, en otro caso se le resta al poder de ataque
			if(defense_power < 0)
				power += Math.abs(defense_power);
			else
				power -= defense_power;

			//Si este es el mayor daño que encontramos, lo guardamos (el = es para que se guarde tambien la defensa)
			if(damagePower <= power){
				damageType = type;
				damagePower = power;
				defendingPower = defense_power;
			}		
		}
		
		//Efecto de COUNTER en el enemigo
		//Si el enemigo tiene un ataque guardado (fue muy lento para pegar) y uno le pega y al pegarle
		//es lo suficientemente rapido para que no efectue el ataque el mismo, uno contrarresta el ataque		
		if(other.attackQue() != null && 
				other.getActionPoints() <= other.attackSpeed()){
			doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_COUNTER + " a " + other.nameElLa() +"!");
			other.modifyAttackQue(null);
			other.resetActionPoints();
			other.unsetData(RPG.BACKGROUND_COLOR);
		}
		
		//Si tus chances de pegar son mayores que la presicion del enemigo efectuas un golpe critico
		if(chanceToHit > other.accuracy()){
			damagePower++;
			doAction(Constants.MESSAGE_CRIT_COLOR, Constants.MESSAGE_CRIT);
		//Si tus chances de pegar son menores que tu 80% de presicion entonces efectuas un golpe parcial
		}else if(chanceToHit <= accuracy() * .8f){
			damagePower--;
			doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_PARTIAL_HIT);
		}
		
		//Si el poder es menor que la defensa (teniendo en cuenta que es el mismo tipo de daño) no hace nada
		if(damagePower <= defendingPower){
			other.doAction("resiste el ataque"+ (defendingObject.getBooleanData(Flags.IS_INTRINSIC) ?
					" con %s %s" : ""), other.isPlayer() ? "tu" : "su", defendingObject.name());
			
			//Si el objeto que defiende es un escudo y tiene mas de la mitad defensa el arma se CAE
			if(!defendingObject.getBooleanData(Flags.IS_INTRINSIC)
					&& !damagingObject.getBooleanData(Flags.IS_INTRINSIC)
					&& damagePower <= defendingPower * 0.5f
					&& damagePower > defendingPower * 0.3f){
				notifyArround(StringUtils.capitalize(damagingObject.nameElLa()) + " cae al suelo"
						+" al impactar contra " + defendingObject.nameElLa());
				drop(damagingObject, "");
			}else
			//Si el objeto que defiende es un escudo y tiene mas de tres veces la defensa el arma se ROMPE contra el
			if(!defendingObject.getBooleanData(Flags.IS_INTRINSIC)
					&& !damagingObject.getBooleanData(Flags.IS_INTRINSIC)
					&& damagePower <= defendingPower * 0.3f){
				notifyArround(StringUtils.capitalize(damagingObject.nameElLa()) + " se rompe"
						+" al impactar contra " + (defendingObject.nameElLa()));
				damagingObject.setData(Flags.IS_BROKEN, true);
			}
			
			return;
		}
								
		if(damagePower < other.woundResistance()){
			doAction("golpea efectuando %s "+ (damagePower > 1 ? "puntos" : "punto")  +" de herida", damagePower);
			
			Wound force_wound = ai.getWoundAttack(damageType, position, other);
			
			if(force_wound != null)
				other.addWound(force_wound, this);
		}else{
			doAction("golpea por %s generando una herida!", damagePower);
				
			Wound wound_to_apply = other.ai().getWound(damageType, position, other);
			
			if(wound_to_apply == null)
				wound_to_apply = ai.getWoundAttack(damageType, position, other);
			
			if(wound_to_apply == null)
				wound_to_apply = damagingObject.getWound(damageType, position, other);
			
			if(wound_to_apply == null)
				wound_to_apply = Wound.getWound(damageType, position, other);
			
			if(wound_to_apply != null)
				other.addWound(wound_to_apply, this);
		}
		
		for (int i = 0; i < wounds.size(); i++){
			Wound wound = wounds.get(i);
			wound.onAttack(this);
		}
		
		for (int i = 0; i < other.wounds().size(); i++){
			Wound wound = other.wounds().get(i);
			wound.onGetAttack(other, this, position);
		}
				
		ai.onAttack(other);
		other.ai().onGetAttacked(this);
		other.modifyHp(-damagePower, damageType.causeOfDeath());
	}

	public void modifyHp(int amount, String causeOfDeath) { 
		hp += amount;
		this.causeOfDeath = causeOfDeath;
		
		if(hp < 1)
			hp = 0;
	}
	
	public void bleed(int intensity){
		int start = 0;
		do{
			start++;
			world.changeAtEmptySpace(AsciiPanel.red, x, y, z);
		}while(start < intensity);
	}
	
	private void leaveCorpse(){
		Item corpse = new Item('%', 'M', (Color)getData(RPG.ORIGINAL_COLOR), "cadaver de " + name, null);
		corpse.setData(Flags.IS_EDIBLE, true);
		corpse.setData(Flags.IS_CORPSE, true);
		
		world.addAtEmptySpace(corpse, x, y, z);
		for (Item item : inventory.getItems()){
			if (item != null){
				inventory.remove(item);
				unequip(item, "deja caer ");
			}
		}
	}
	
	public void open(int wx, int wy, int wz) {
		//world.useDoor(wx, wy, wz);
		doAction("abre la puerta");
	}
	
	public void dig(int wx, int wy, int wz) {
		//world.dig(wx, wy, wz);
		doAction("cava");
	}
	
	public void update(){
		do{
			updateEffects();
			updateWounds();
			regenerateHp();
			ai.onUpdate();
		}while(actionPoints >= 100 && actionPoints < 100);
	}
	
	private void regenerateHp(){
		if(wounds.isEmpty()){
			hpRegenerationCooldown -= hpRegeneration;
	        if (hpRegenerationCooldown < 0){
	            if (hp < maxHp) {
	                modifyHp(1, "Mueres por regenerar vida?");
	            }
	            hpRegenerationCooldown += 50;
	        }
		}
	}
	
	private void updateWounds(){
		List<Wound> done = new ArrayList<Wound>();
		
		for (int i = 0; i < wounds.size(); i++){
			Wound wound = wounds.get(i);
			
			if (wound.isHealed()) {
				wound.onFinish(this);
				done.add(wound);
			}else{
				wound.update(this);
			}
		}
		
		wounds.removeAll(done);
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
	
	public void notifyArround(String message, Object ... params){
		for (Creature other : getCreaturesWhoSeeMe()){
			other.notify(message, params);
		}
	}
	
	public void notifyArround(Color color, String message, Object ... params){
		for (Creature other : getCreaturesWhoSeeMe()){
			other.notify(color, message, params);
		}
	}
	
	public void doAction(String message, Object ... params){
		boolean hasPunctuation = StringUtils.hasPunctuation(message);
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(StringUtils.makeSecondPerson(message, true) + (hasPunctuation ? "" : "."), params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : StringUtils.capitalize(nameElLa());
				other.notify(String.format("%s %s"+ (hasPunctuation ? "" : "."), nombre, message), params);
			}
		}
	}
	
	public void doAction(Color actionColor, String message, Object ... params){
		boolean hasPunctuation = StringUtils.hasPunctuation(message);
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(actionColor, StringUtils.makeSecondPerson(message, true) + (hasPunctuation ? "" : "."), params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : StringUtils.capitalize(nameElLa());
				other.notify(actionColor, String.format("%s %s"+ (hasPunctuation ? "" : "."), nombre, message), params);
			}
		}
	}
		
	public void doAction(Item item, String message, Object ... params){
		if (hp < 1)
			return;
		
		boolean hasPunctuation = StringUtils.hasPunctuation(message);
		
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(StringUtils.makeSecondPerson(message, false) + (hasPunctuation ? "" : "."), params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : StringUtils.capitalize(nameElLa());
				other.notify(String.format("%s %s"+ (hasPunctuation ? "" : "."), nombre, message), params);
			}
			other.learnName(item);
		}
	}
	
	public HashMap<Item, Point> getItemsArroundMe(){
		HashMap<Item, Point> items = new HashMap<Item, Point>();
		int r = 9;
		for (int ox = -r; ox < r+1; ox++){
			for (int oy = -r; oy < r+1; oy++){
				if (ox*ox + oy*oy > r*r)
					continue;
				
				Item item = world.item(x+ox, y+oy, z);
				
				if (item == null)
					continue;
				
				items.put(item, new Point(x+ox, y+oy, z));
			}
		}
		return items;
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
			doAction("levanta "+ item.nameUnUna() +"");
			world.remove(x, y, z);
			inventory.add(item);
		}
	}
	
	/** 
	 * Force drop remueve el item del inventario y no dice nada
	 * */
	public void force_drop(Item item){
		if(item == null)
			return;
		
		if (world.addAtEmptySpace(item, x, y, z)){
			inventory.remove(item);
			unequip(item);
		}
	}
	
	public void drop(Item item){
		if(item == null)
			return;
		
		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("suelta "+ item.nameUnUna() + "");
			inventory.remove(item);
			unequip(item);
		} else {
			notify("No hay lugar donde soltar "+ item.nameElLa() + "");
		}
	}
	
	public void drop(Item item, String action){
		if(item == null)
			return;
		
		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("suelta "+ item.nameUnUna() + "");
			inventory.remove(item);
			unequip(item, action);
		} else {
			notify("No hay lugar donde soltar "+ item.nameElLa() + "");
		}
	}
	
	public void eat(Item item){
		doAction("consume " + item.nameElLa());
		consume(item);
	}
	
	public void quaff(Item item){
		doAction("bebe " + item.nameElLa());
		consume(item);
	}
	
	private void consume(Item item){
		if (!item.getBooleanData(Flags.IS_EDIBLE))
			notify("Asqueroso!");
		
		addEffect((Effect)item.getData("ConsumeEffect"));
		
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
		unequip(item, null);
	}
	
	public void unequip(Item item, String action){
		if (item == null)
			return;
		
		if (item == armor){
			if (hp > 0 && !action.isEmpty())
				doAction(action == null ? "remueve " : action + " " + item.nameElLa());
			
			armor = null;
		} else if (item == weapon) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + " " + item.nameElLa());
			
			weapon = null;
		} else if (item == shield) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + " " + item.nameElLa());
			
			shield = null;
		} else if (item == helment) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + " " + item.nameElLa());
			
			helment = null;
		}
	}
	
	public void equip(Item item){
		if (!inventory.contains(item)) {
			if (inventory.isFull()) {
				notify("No puedes equipar "+ item.nameElLa() + "ya que el inventario esta lleno!");
				return;
			} else {
				world.remove(item);
				inventory.add(item);
			}
		}
		
		if (!item.getBooleanData(Flags.IS_ARMOR) && 
				!item.getBooleanData(Flags.IS_HELMENT) && 
				!item.getBooleanData(Flags.IS_SHIELD) && 
				!item.getBooleanData(Flags.IS_WEAPON))
			return;
		
		if (item.getBooleanData(Flags.IS_WEAPON)){
			unequip(weapon);
			doAction("sujeta " +  item.nameElLa());
			weapon = item;
		} else if(item.getBooleanData(Flags.IS_ARMOR)){
			unequip(armor);
			doAction("viste "  +  item.nameElLa());
			armor = item;
		} else if(item.getBooleanData(Flags.IS_HELMENT)){
			unequip(helment);
			doAction("coloca " +  item.nameElLa());
			helment = item;
		} else if(item.getBooleanData(Flags.IS_SHIELD)){
			unequip(shield);
			doAction("alza " +  item.nameElLa());
			shield = item;
		}
	}
	
	public Item item(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.item(wx, wy, wz);
		else
			return null;
	}
	
	public String details() {
		return "sarasa";
		//return String.format("  level:%d  attack:%d  defense:%d  hp:%d", level, attackValue(), defenseValue(), hp);
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
			doAction("arroja " + item.nameElLa());
		
		if (item.getData("ConsumeEffect") != null && c != null)
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

		doAction("apunta y murmulla a la nada");
		
		other.addEffect(spell.effect());
	}
	
	public String nameOf(Item item){
		return ai.getName(item);
	}
	
	public void learnName(Item item){
		/*notify(StringUtils.capitalize(StringUtils.checkGender(item.gender(), true, this.isPlayer())) + item.appearance() + " es " 
					+ StringUtils.checkGender(item.gender(), false, this.isPlayer()) + " " + item.name() + "!");*/
		ai.setName(item, item.name());
	}
}
