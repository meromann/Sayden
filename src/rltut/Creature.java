package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import rltut.Item.ItemType;
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
	
	private Color originalColor;
	public Color originalColor() { return originalColor; }
	
	private Color color;
	public Color color() { return color; }
	public void modifyColor(Color newColor) { this.color = newColor; }
	
	private Color statusColor;
	public Color statusColor() { return statusColor; }
	public void modifyStatusColor(Color newColor) { this.statusColor = newColor; }
	
	private CreatureAi ai;
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	public CreatureAi getCreatureAi() { return ai; }
	
	private String job;
	public String job() { return job; }
	public void setJob(String job) { this.job = job; }
	
	private int hp;
	public int hp() { return hp; }
	
	private int maxHp;
	public int maxHp() { return maxHp; }
	public void modifyMaxHp(int amount) { this.maxHp += amount; }
	
	private int woundResistance;
	public int woundResistance() { return woundResistance; }
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
	
	private String name;
	public String name() { return name; }

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
	public int movementSpeed() { return movementSpeed; }
	public void modifyMovementSpeed(int amount) { this.movementSpeed += amount; }
	
	public int attackSpeed;
	public int attackSpeed() { return attackSpeed; }
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
	
	private Screen shopScreen;
	public Screen shopScreen() { return shopScreen; }
	public void setShopScreen(Screen shop) { this.shopScreen = shop; }
	
	private boolean isPlayer;
	public boolean isPlayer(){ return isPlayer; }
	public void makePlayer() { this.isPlayer = true; }
	
	private boolean inmaculado;
	public boolean inmaculado() { return inmaculado; }
		
	private List<Wound> wounds;
	public List<Wound> wounds() { return wounds; }
	
	public void addWound(Wound wound, Creature applier){ inmaculado = false; wound.onApply(this, applier); wounds.add(wound); }
	public boolean hasWound(String woundName){
		for(Wound wound : wounds){
			if(wound.name() == woundName)
				return true;
		}
		return false;
	}	
	
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
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.originalColor = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.woundResistance = 4;
		this.visionRadius = Constants.STARTING_VISION_RADIUS;
		this.name = name;
		this.inventory = new Inventory(Constants.INVENTORY_SIZE);
		this.effects = new ArrayList<Effect>();
		this.limbs = new ArrayList<BodyPart>();
		this.wounds = new ArrayList<Wound>();
		this.job = name;
		this.gender = gender;
		this.intrinsicWeapon = weapon;
		this.intrinsicArmor = armor;
		this.attackSpeed = Constants.STARTING_ATTACK_SPEED;
		this.movementSpeed = Constants.STARTING_MOVE_SPEED;
		this.dismembered = false;
		this.inmaculado = true;
		this.accuracy = 100;
		this.missChance = 10;
	}
	
	public void moveBy(int mx, int my, int mz){
		if (mx==0 && my==0 && mz==0 || (isPlayer && actionPoints < 0)){
			if(isPlayer){
				if(actionPoints < 0){
					doAction(Constants.MESSAGE_STATUS_EFFECT_COLOR, "esta " + statusEffect()); 
					modifyActionPoints(100);
				}
				world.modifyActionPoints(100);
			}else{
				modifyActionPoints(-100);
			}
			return;
		}
		
		if(statusEffect != null && (actionPoints > 0 || isPlayer)){
			statusEffect = null;
			modifyStatusColor(null);
		}
		
		if(attackQue != null && actionPoints >= attackSpeed){
			Creature queCreature = world.creature(attackQue.x, attackQue.y, attackQue.z);
			if(queCreature == null){
				doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_DODGE);
			}else{
				meleeAttack(queCreature, weapon);
			}
			modifyActionPoints(-attackSpeed);
			attackQue = null;
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
				world.modifyActionPoints(movementSpeed);
			}else if(actionPoints < movementSpeed){
				return;
			}else{
				modifyActionPoints(-movementSpeed);
			}
			ai.onEnter(x+mx, y+my, z+mz, tile);
		}else{
			if(isPlayer){
				world.modifyActionPoints(attackSpeed);
			}else if(actionPoints <= (attackSpeed * 0.5f) && actionPoints > 0){
				doAction(Constants.MESSAGE_DODGE_COLOR, Constants.MESSAGE_DODGE_WARNING);
				attackQue = new Point(x+mx, y+my, z+mz);
				return;
			}else if(actionPoints < attackSpeed){
				return;
			}else{
				modifyActionPoints(-attackSpeed);
			}
			//other.ai.onTalkedTo(this);
			meleeAttack(other, weapon);
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
				
		//Comienza a recorrer todos los ataques posibles del objeto con el que se ataca
		for(int i = 0; i < damagingObject.damageTypes().size(); i++){
			int power = damagingObject.damageTypes().get(i).power();		//Guardamos el poder
			DamageType type = damagingObject.damageTypes().get(i);	//Guardamos el tipo
			
			if(damagingObject.broken())
				power = 1;
			
			damageType = type;
			damagePower = power;
			
			//Comienza a recorrer todas las defensas posibles del objetivo
			for(int n = 0; n < defendingObject.damageTypes().size(); n++){
				int defense_power = defendingObject.damageTypes().get(n).power();		//Guardamos el poder
				String defense_type = defendingObject.damageTypes().get(n).wondType();	//Guardamos el tipo
				
				//Si esta defendiendo con "la piel" no lo hace desde la cabeza
				if(defendingObject.itemType() == ItemType.INTRINSIC &&
						position == BodyPart.HEAD){
					defense_power = 0;
				}
				
				//Las debilidades a ciertos tipos se resuelven con numeros negativos, en otro caso se le resta al poder de ataque
				if(defense_power < 0)
					power += Math.abs(defense_power);
				else
					power -= defense_power;
				
				//Si es diferente el tipo no le prestemos atencion (BLUNT vs SLICE no hace nada)
				if(type.wondType() != defense_type)
					continue;

				//Si este es el mayor daño que encontramos, lo guardamos (el = es para que se guarde tambien la defensa)
				if(damagePower <= power){
					damageType = type;
					damagePower = power;
					defendingPower = defense_power;
				}
			}			
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
			other.doAction("resiste el ataque"+ (defendingObject.itemType() != ItemType.INTRINSIC ?
					" con %s %s" : ""), other.isPlayer() ? "tu" : "su", defendingObject.name());
			
			//Si el objeto que defiende es un escudo y tiene mas de la mitad defensa el arma se CAE
			if(defendingObject.itemType() != ItemType.INTRINSIC
					&& damagingObject.itemType() != ItemType.INTRINSIC
					&& damagePower <= defendingPower * 0.5f
					&& damagePower > defendingPower * 0.3f){
				notifyArround(damagingObject.gender() == 'M' ? "El " : "La " + damagingObject.name() + " cae al suelo"
						+" al impactar contra " + (defendingObject.gender() == 'M' ? "el " : "la " + defendingObject.name()));
				drop(damagingObject, "");
			}else
			//Si el objeto que defiende es un escudo y tiene mas de tres veces la defensa el arma se ROMPE contra el
			if(defendingObject.itemType() != ItemType.INTRINSIC
					&& damagingObject.itemType() != ItemType.INTRINSIC
					&& damagePower <= defendingPower * 0.3f){
				notifyArround(damagingObject.gender() == 'M' ? "El " : "La " + damagingObject.name() + " se rompe"
						+" al impactar contra " + (defendingObject.gender() == 'M' ? "el " : "la " + defendingObject.name()));
				damagingObject.makeBroken(true);
			}
			
			return;
		}
				
		//Efecto de COUNTER en el enemigo
		//Si el enemigo tiene un ataque guardado (fue muy lento para pegar) y uno le pega y al pegarle
		//es lo suficientemente rapido para que no efectue el ataque el mismo, uno contrarresta el ataque		
		if(other.attackQue() != null && 
				other.getActionPoints() <= other.attackSpeed()){
			doAction(Constants.MESSAGE_DODGE_COLOR, "logra contrarrestar el ataque "+ StringUtils.genderizeCreature(other.gender(), other.name(), true) +"!");
			other.modifyAttackQue(null);
			other.resetActionPoints();
		}
				
		if(damagePower < other.woundResistance()){
			doAction("golpea efectuando %s "+ (damagePower > 1 ? "puntos" : "punto")  +" de herida", damagePower);
		}else{
			doAction("golpea por %s generando una herida!", damagePower);
			
			Wound wound_to_apply = other.getCreatureAi().getWound(damageType, position, other);
			
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
		ai.onAttack(other);
		
		other.modifyHp(-damagePower, damageType.causeOfDeath());
	}

	public void modifyHp(int amount, String causeOfDeath) { 
		hp += amount;
		this.causeOfDeath = causeOfDeath;
		
		if (hp < 1) {
			doAction("perece");
			leaveCorpse();
			world.remove(this);
		}
	}
	
	public void kill(String causeOfDeath, String action){
		this.causeOfDeath = causeOfDeath;
		
		if(action != null)
			doAction(action);
		
		hp = -1;
		
		if(!isPlayer){
			leaveCorpse();
			world.remove(this);
		}
	}
	
	public void bleed(int intensity){
		int start = 0;
		do{
			start++;
			world.changeAtEmptySpace(AsciiPanel.red, x, y, z);
		}while(start < intensity);
	}
	
	private void leaveCorpse(){
		Item corpse = new Item(ItemType.STATIC, '%', 'M', color, "cadaver de " + name, null);
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
			ai.onUpdate();
		}while(actionPoints >= 100 && actionPoints < 100);
	}
	
	private void updateWounds(){
		List<Wound> done = new ArrayList<Wound>();
		
		for (int i = 0; i < wounds.size(); i++){
			Wound wound = wounds.get(i);
			wound.update(this);
			if (wound.isHealed()) {
				wound.onFinish(this);
				done.add(wound);
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
	
	public void talkAction(Color color, String message, Object ... params){
		boolean hasPunctuation = StringUtils.hasPunctuation(message);
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(color, StringUtils.makeSecondPerson(message, false) + (hasPunctuation ? "" : "."), params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " : "La " + name);
				other.notify(color, String.format("%s %s"+ (hasPunctuation ? "" : "."), nombre, message), params);
			}
		}
	}
	
	public void doAction(String message, Object ... params){
		boolean hasPunctuation = StringUtils.hasPunctuation(message);
		for (Creature other : getCreaturesWhoSeeMe()){
			if (other == this){
				other.notify(StringUtils.makeSecondPerson(message, true) + (hasPunctuation ? "" : "."), params);
			} else {
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " + name : "La " + name);
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
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " + name : "La " + name);
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
				String nombre = !name.equals(name.toLowerCase()) ? name : (gender == 'M' ? "El " : "La " + name);
				other.notify(String.format("%s %s"+ (hasPunctuation ? "" : "."), nombre, message), params);
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
			doAction("levanta "+ StringUtils.checkGender(item.gender(), false, this.isPlayer()) +" %s", nameOf(item));
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
			doAction("suelta "+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) +" %s", nameOf(item));
			inventory.remove(item);
			unequip(item);
		} else {
			notify("No hay lugar donde soltar"+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) +" %s.", nameOf(item));
		}
	}
	
	public void drop(Item item, String action){
		if(item == null)
			return;
		
		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("suelta "+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) +" %s", nameOf(item));
			inventory.remove(item);
			unequip(item, action);
		} else {
			notify("No hay lugar donde soltar"+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) +" %s.", nameOf(item));
		}
	}
	
	public void eat(Item item){
		doAction("consume " + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
		consume(item);
	}
	
	public void quaff(Item item){
		doAction("bebe " + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
		consume(item);
	}
	
	private void consume(Item item){
		if (item.itemType() != ItemType.EDIBLE)
			notify("Asqueroso!");
		
		addEffect(item.quaffEffect());
		
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
				doAction(action == null ? "remueve " : action + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			
			armor = null;
		} else if (item == weapon) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			
			weapon = null;
		} else if (item == shield) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			
			shield = null;
		} else if (item == helment) {
			if (hp > 0 && !action.isEmpty()) 
				doAction(action == null ? "guarda " : action + StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			
			helment = null;
		}
	}
	
	public void equip(Item item){
		if (!inventory.contains(item)) {
			if (inventory.isFull()) {
				notify("No puedes equipar "+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " %s ya que el inventario esta lleno!", nameOf(item));
				return;
			} else {
				world.remove(item);
				inventory.add(item);
			}
		}
		
		if (item.itemType() != ItemType.ARMOR && 
				item.itemType() != ItemType.HELMENT && 
				item.itemType() != ItemType.SHIELD && 
				item.itemType() != ItemType.WEAPON)
			return;
		
		if (item.itemType() == ItemType.WEAPON){
			unequip(weapon);
			doAction("sujeta " +  StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			weapon = item;
		} else if(item.itemType() == ItemType.ARMOR){
			unequip(armor);
			doAction("viste " +  StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			armor = item;
		} else if(item.itemType() == ItemType.HELMENT){
			unequip(helment);
			doAction("coloca " +  StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
			helment = item;
		} else if(item.itemType() == ItemType.SHIELD){
			unequip(shield);
			doAction("alza " +  StringUtils.checkGender(item.gender(), true, this.isPlayer()) + " " + nameOf(item));
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
			doAction("arroja "+ StringUtils.checkGender(item.gender(), true, this.isPlayer()) +" %s", nameOf(item));
		
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

		doAction("apunta y murmulla a la nada");
		
		other.addEffect(spell.effect());
	}
	
	public String nameOf(Item item){
		return ai.getName(item);
	}
	
	public void learnName(Item item){
		notify(StringUtils.capitalize(StringUtils.checkGender(item.gender(), true, this.isPlayer())) + item.appearance() + " es " 
					+ StringUtils.checkGender(item.gender(), false, this.isPlayer()) + " " + item.name() + "!");
		ai.setName(item, item.name());
	}
}
