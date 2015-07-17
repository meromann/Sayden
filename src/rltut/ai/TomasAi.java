package rltut.ai;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import rltut.Creature;
import rltut.DamageType;
import rltut.Item;
import rltut.Item.ItemType;
import rltut.screens.Option;

public class TomasAi extends CreatureAi {

	private ArrayList<String> messages;
	
	public TomasAi(Creature creature) {
		super(creature);
		messages = new ArrayList<String>();
	}
	public void onTalkTo(Creature talker){
		if(!getFlag("IsIntroduced") && messages.isEmpty()){
			messages.clear();
			messages.add("Mi nombre es Tomas, mi camino es el del herrero");
			messages.add("Existo en la medida que puedo fraguar los metales");
			messages.add("Existo solo gracias a mi arte, solo a causa de mi arte");
			messages.add("Mas no tengo utilidad para mis constructos");
			messages.add("Pocas personas necesitan armas hoy en dia, y las que lo necesitan no son de fiar");
			messages.add("Eres tu de fiar? O lo que es mas importante...");
			messages.add("Encuentras en tu camino utilidad para un arma?");
			setFlag("IsIntroduced", true);
		}
		if(getFlag("IsIntroduced") && messages.isEmpty() &&
				!getFlag("IsWarrior") && !getFlag("IsPacific")){
			talker.addOption(new Option("Si (pedir el arma)", creature){
				public void onSelect(Creature player){
					player.clearOptions();
					creature.ai().setFlag("IsWarrior", true);
					creature.ai().onTalkTo(player);
				}
			});
			talker.addOption(new Option("No (rechazar el arma)", creature){
				public void onSelect(Creature player){
					player.clearOptions();
					creature.ai().setFlag("IsPacific", true);
					creature.ai().onTalkTo(player);
				}
			});
		}
		if(getFlag("IsPacific") && messages.isEmpty() && !getFlag("IsFinished")){
			messages.clear();
			messages.add("Bien...Poco consiguen las armas y demasiado demandan de uno");
			messages.add("El arma entrega la ilusion de poder, es un temible objeto de perdicion");
			messages.add("Cuantos guerreros de epocas pasadas perdieron su cordura...");
			messages.add("...su resolucion...su camino...");
			messages.add("Yo soy Tomas y mi camino es del herrero, que tengas suerte en el tuyo, donde sea que te lleve");
			setFlag("IsFinished", true);
			
		}
		if(getFlag("IsWarrior") && messages.isEmpty() && !getFlag("IsFinished")){
			messages.clear();
			messages.add("...");
			messages.add("Un sabio guerrero sabe que del arma poco se puede esperar");
			messages.add("Un sabio guerrero conoce sus habilidades y convierte a su espada en una extension de su persona");
			messages.add("Un sabio guerrero sabe que el arma es su maldicion pero tambien su herramienta");
			messages.add("...te ruego, desconocido, que si has de blandir una de mis armas sea tuyo el camino del guerrero");
			messages.add("Yo soy Tomas y mi camino es del herrero...ten cuidado mientras deambules por el tuyo desconocido");
			setFlag("IsFinished", true);
		}		
		if(getFlag("IsFinished") && messages.isEmpty() ){
			if(getFlag("IsWarrior") && !getFlag("IsArmed")){
				talker.addOption(new Option("Espada (cortante)", creature){
					public void onSelect(Creature player){
						Item item = new Item(ItemType.WEAPON, ')', 'F', AsciiPanel.brightWhite, "espada", null);
						item.addDamageType(DamageType.SLICE, 2);
						item.modifyAttackSpeed(50);
						player.clearOptions();
						player.inventory().add(item);
						creature.ai().setFlag("IsArmed",true);
						creature.ai().onTalkTo(player);
					}
				});
				talker.addOption(new Option("Maza (contundente)", creature){
					public void onSelect(Creature player){
						Item item = new Item(ItemType.WEAPON, ')', 'F', AsciiPanel.brightWhite, "maza", null);
						item.addDamageType(DamageType.BLUNT, 2);
						item.modifyAttackSpeed(50);
						player.clearOptions();
						player.inventory().add(item);
						creature.ai().setFlag("IsArmed",true);
						creature.ai().onTalkTo(player);
					}
				});
				talker.addOption(new Option("Daga (penetrante)", creature){
					public void onSelect(Creature player){
						Item item = new Item(ItemType.WEAPON, ')', 'F', AsciiPanel.brightWhite, "daga", null);
						item.addDamageType(DamageType.PIERCING, 2);
						
						player.clearOptions();
						player.inventory().add(item);
						creature.ai().setFlag("IsArmed",true);
						creature.ai().onTalkTo(player);
					}
				});
				talker.addOption(new Option("Rechazar el camino", creature){
					public void onSelect(Creature player){
						player.clearOptions();
						player.notify(creature.color(), "Bien...en una de esas eres realmente un guerrero...");
						creature.ai().setFlag("IsArmed",true);
						creature.ai().onTalkTo(player);
					}
				});
			}
			if((getFlag("IsPacific") || getFlag("IsArmed")) && messages.isEmpty()){
				messages.clear();
				messages.add("Ten suerte en tu camino");
			}
		}
		if(!messages.isEmpty()){
			talker.notify(creature.color(), messages.get(0));
			messages.remove(0);
		}
	}
}
