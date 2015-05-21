package rltut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rltut.ai.BatAi;
import rltut.ai.FungusAi;
import rltut.ai.GoblinAi;
import rltut.ai.PlayerAi;
import rltut.ai.ZombieAi;
import asciiPanel.AsciiPanel;

public class StuffFactory {
	private World world;
	private Map<String, Color> potionColors;
	private List<String> potionAppearances;
	
	public StuffFactory(World world){
		this.world = world;
		
		setUpPotionAppearances();
	}
	
	private void setUpPotionAppearances(){
		potionColors = new HashMap<String, Color>();
		potionColors.put("pocion roja", AsciiPanel.brightRed);
		potionColors.put("pocion amarilla", AsciiPanel.brightYellow);
		potionColors.put("pocion verde", AsciiPanel.brightGreen);
		potionColors.put("pocion celeste", AsciiPanel.brightCyan);
		potionColors.put("pocion azul", AsciiPanel.brightBlue);
		potionColors.put("pocion magenta", AsciiPanel.brightMagenta);
		potionColors.put("pocion oscura", AsciiPanel.brightBlack);
		potionColors.put("pocion gris", AsciiPanel.white);
		potionColors.put("pocion de luz", AsciiPanel.brightWhite);

		potionAppearances = new ArrayList<String>(potionColors.keySet());
		Collections.shuffle(potionAppearances);
	}
	
	public Creature newPlayer(List<Message> messages, FieldOfView fov){
		Creature player = new Creature(world, '@', 'M', AsciiPanel.brightWhite, "jugador", 100, 20, 5);
		world.addAtEmptyLocation(player, 0);
		new PlayerAi(player, messages, fov);
		player.makePlayer();
		player.inventory().add(newDagger(-1));
		player.inventory().add(newOPDagger(-1));
		return player;
	}
	
	public Creature newFungus(int depth, int spreadcount){
		Creature fungus = new Creature(world, 'f', 'M', AsciiPanel.green, "hongo", 10, 0, 0);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this, spreadcount);
		return fungus;
	}
	
	public Creature newBat(int depth){
		Creature bat = new Creature(world, 'b', 'M', AsciiPanel.brightYellow, "murcielago", 15, 5, 0);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	public Creature newZombie(int depth, Creature player){
		Creature zombie = new Creature(world, 'z', 'M', AsciiPanel.white, "zombie", 50, 10, 10);
		world.addAtEmptyLocation(zombie, depth);
		new ZombieAi(zombie, player);
		return zombie;
	}

	public Creature newGoblin(int depth, Creature player){
		Creature goblin = new Creature(world, 'g', 'M', AsciiPanel.brightGreen, "goblin", 66, 15, 5);
		new GoblinAi(goblin, player);
		goblin.equip(randomWeapon(depth));
		goblin.equip(randomArmor(depth));
		world.addAtEmptyLocation(goblin, depth);
		return goblin;
	}
	
	public Item newRock(int depth){
		Item rock = new Item(',', 'F', AsciiPanel.yellow, "roca", null);
		rock.modifyThrownAttackValue(5);
		world.addAtEmptyTileLocation(rock, depth, Tile.FLOOR);
		return rock;
	}
	
	public Item newVictoryItem(int depth){
		Item item = new Item('*', 'M', AsciiPanel.brightWhite, "oso de felpa", null);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBread(int depth){
		Item item = new Item('%', 'M', AsciiPanel.yellow, "pan", null);
		item.modifyFoodValue(400);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newFruit(int depth){
		Item item = new Item('%', 'F', AsciiPanel.brightRed, "manzana", null);
		item.modifyFoodValue(100);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newDagger(int depth){
		Item item = new Item(')', 'F', AsciiPanel.white, "daga", null);
		item.addDamageType(DamageType.PIERCING, 1);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newOPDagger(int depth){
		Item item = new Item(')', 'F', AsciiPanel.white, "op daga", null);
		item.addDamageType(DamageType.PIERCING, 10);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newSword(int depth){
		Item item = new Item(')', 'F', AsciiPanel.brightWhite, "espada", null);
		item.modifyAttackValue(10);
		item.modifyThrownAttackValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newStaff(int depth){
		Item item = new Item(')', 'M', AsciiPanel.yellow, "baston", null);
		item.modifyAttackValue(5);
		item.modifyDefenseValue(3);
		item.modifyThrownAttackValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newBow(int depth){
		Item item = new Item(')', 'M', AsciiPanel.yellow, "arco", null);
		item.modifyAttackValue(1);
		item.modifyRangedAttackValue(5);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newEdibleWeapon(int depth){
		Item item = new Item(')', 'F', AsciiPanel.yellow, "baguette", null);
		item.modifyAttackValue(3);
		item.modifyFoodValue(100);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newLightArmor(int depth){
		Item item = new Item('[', 'F',AsciiPanel.green, "tunica", null);
		item.modifyDefenseValue(2);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newMediumArmor(int depth){
		Item item = new Item('[', 'F', AsciiPanel.white, "cota de malla", null);
		item.modifyDefenseValue(4);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newHeavyArmor(int depth){
		Item item = new Item('[', 'F', AsciiPanel.brightWhite, "cota de plata", null);
		item.modifyDefenseValue(6);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item randomWeapon(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newDagger(depth);
		case 1: return newSword(depth);
		case 2: return newBow(depth);
		default: return newStaff(depth);
		}
	}

	public Item randomArmor(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newLightArmor(depth);
		case 1: return newMediumArmor(depth);
		default: return newHeavyArmor(depth);
		}
	}
	
	public Item newPotionOfHealth(int depth){
		String appearance = potionAppearances.get(0);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion de vida", appearance);
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(15, "Killed by a health potion?");
				creature.doAction(item, "ve mucho mas saludable");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfMana(int depth){
		String appearance = potionAppearances.get(1);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion de mana", appearance);
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature){
				if (creature.mana() == creature.maxMana())
					return;
				
				creature.modifyMana(10);
				creature.doAction(item, "ve restaurado");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfSlowHealth(int depth){
		String appearance = potionAppearances.get(2);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion rejuvenecedora", appearance);
		item.setQuaffEffect(new Effect(100){
			public void start(Creature creature){
				creature.doAction(item, "ve un poco mejor");
			}
			
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(1, "Killed by a slow health potion?");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfPoison(int depth){
		String appearance = potionAppearances.get(3);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion de veneno", appearance);
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.doAction(item, "ve muy enfermo");
			}
			
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(-1, "Died of poison.");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfWarrior(int depth){
		String appearance = potionAppearances.get(4);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion de veneno", appearance);
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.modifyAttackValue(5);
				creature.modifyDefenseValue(5);
				creature.doAction(item, "ve mucho mas peligroso");
			}
			public void end(Creature creature){
				creature.modifyAttackValue(-5);
				creature.modifyDefenseValue(-5);
				creature.doAction("ve menos peligroso");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfArcher(int depth){
		String appearance = potionAppearances.get(5);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion del arquero", appearance);
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.modifyVisionRadius(3);
				creature.doAction(item, "ve mas alerta");
			}
			public void end(Creature creature){
				creature.modifyVisionRadius(-3);
				creature.doAction("ve menos alerta");
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfExperience(int depth){
		String appearance = potionAppearances.get(6);
		final Item item = new Item('!', 'F', potionColors.get(appearance), "pocion de experiencia", appearance);
		item.setQuaffEffect(new Effect(20){
			public void start(Creature creature){
				creature.doAction(item, "ve mucho mas sabio");
				creature.modifyXp(creature.level() * 5);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item randomPotion(int depth){
		switch ((int)(Math.random() * 9)){
		case 0: return newPotionOfHealth(depth);
		case 1: return newPotionOfHealth(depth);
		case 2: return newPotionOfMana(depth);
		case 3: return newPotionOfMana(depth);
		case 4: return newPotionOfSlowHealth(depth);
		case 5: return newPotionOfPoison(depth);
		case 6: return newPotionOfWarrior(depth);
		case 7: return newPotionOfArcher(depth);
		default: return newPotionOfExperience(depth);
		}
	}
	
	public Item newWhiteMagesSpellbook(int depth) {
		Item item = new Item('+', 'M',AsciiPanel.brightWhite, "libro blanco", null);
		item.addWrittenSpell("ligera curacion", 4, new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(20, "Killed by a minor heal spell?");
				creature.doAction("ve mas sano");
			}
		});
		
		item.addWrittenSpell("curacion mayor", 8, new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(50, "Killed by a major heal spell?");
				creature.doAction("ve mucho mas sano");
			}
		});
		
		item.addWrittenSpell("curacion lenta", 12, new Effect(50){
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(2, "Killed by a slow heal spell?");
			}
		});

		item.addWrittenSpell("fuerza", 16, new Effect(50){
			public void start(Creature creature){
				creature.modifyAttackValue(2);
				creature.modifyDefenseValue(2);
				creature.modifyVisionRadius(1);
				creature.modifyRegenHpPer1000(10);
				creature.modifyRegenManaPer1000(-10);
				creature.doAction("parece brillar con omnipotencia!");
			}
			public void update(Creature creature){
				super.update(creature);
				if (Math.random() < 0.25)
					creature.modifyHp(1, "Killed by inner strength spell?");
			}
			public void end(Creature creature){
				creature.modifyAttackValue(-2);
				creature.modifyDefenseValue(-2);
				creature.modifyVisionRadius(-1);
				creature.modifyRegenHpPer1000(-10);
				creature.modifyRegenManaPer1000(10);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBlueMagesSpellbook(int depth) {
		Item item = new Item('+', 'M', AsciiPanel.brightBlue, "libro azul", null);

		item.addWrittenSpell("sangre por mana", 1, new Effect(1){
			public void start(Creature creature){
				int amount = Math.min(creature.hp() - 1, creature.maxMana() - creature.mana());
				creature.modifyHp(-amount, "Killed by a blood to mana spell.");
				creature.modifyMana(amount);
			}
		});
		
		item.addWrittenSpell("blink", 6, new Effect(1){
			public void start(Creature creature){
				creature.doAction("desaparece");
				
				int mx = 0;
				int my = 0;
				
				do
				{
					mx = (int)(Math.random() * 11) - 5;
					my = (int)(Math.random() * 11) - 5;
				}
				while (!creature.canEnter(creature.x+mx, creature.y+my, creature.z)
						&& creature.canSee(creature.x+mx, creature.y+my, creature.z));
				
				creature.moveBy(mx, my, 0);
				
				creature.doAction("reaparece");
			}
		});
		
		item.addWrittenSpell("invocar murcielagos", 11, new Effect(1){
			public void start(Creature creature){
				for (int ox = -1; ox < 2; ox++){
					for (int oy = -1; oy < 2; oy++){
						int nx = creature.x + ox;
						int ny = creature.y + oy;
						if (ox == 0 && oy == 0 
								|| creature.creature(nx, ny, creature.z) != null)
							continue;
						
						Creature bat = newBat(0);
						
						if (!bat.canEnter(nx, ny, creature.z)){
							world.remove(bat);
							continue;
						}
						
						bat.x = nx;
						bat.y = ny;
						bat.z = creature.z;
						
						creature.summon(bat);
					}
				}
			}
		});
		
		item.addWrittenSpell("detectar criaturas", 16, new Effect(75){
			public void start(Creature creature){
				creature.doAction("mira lejos en el horizonte");
				creature.modifyDetectCreatures(1);
			}
			public void end(Creature creature){
				creature.modifyDetectCreatures(-1);
			}
		});
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	

	public Item randomSpellBook(int depth){
		switch ((int)(Math.random() * 2)){
		case 0: return newWhiteMagesSpellbook(depth);
		default: return newBlueMagesSpellbook(depth);
		}
	}
	
}