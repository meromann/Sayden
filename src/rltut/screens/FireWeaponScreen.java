package rltut.screens;

import rltut.Creature;
import rltut.Line;
import rltut.Point;
import rltut.StringUtils;

public class FireWeaponScreen extends TargetBasedScreen {

	public FireWeaponScreen(Creature player, int sx, int sy) {
		super(player, "Donde disparas " + StringUtils.checkGender(player.weapon().gender(), true, player.isPlayer()) + " " + player.nameOf(player.weapon()) + "?", sx, sy);
	}

	public boolean isAcceptable(int x, int y) {
		if (!player.canSee(x, y, player.z))
			return false;
		
		for (Point p : new Line(player.x, player.y, x, y)){
			if (!player.realTile(p.x, p.y, player.z).isGround())
				return false;
		}
		
		return true;
	}
	
	public void selectWorldCoordinate(int x, int y, int screenX, int screenY){
		Creature other = player.creature(x, y, player.z);
		
		if (other == null)
			player.notify("No hay nadie a quien disparar.");
		else
			player.rangedWeaponAttack(other);
	}

	@Override
	public String getScreenName() {
		return "DISPARAR";
	}
}
