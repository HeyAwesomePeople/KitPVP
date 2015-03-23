package me.HeyAwesomePeople.kitpvp.repeating;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.util.Vector;

public class GrappleHook implements Runnable {
	private KitPVP plugin = KitPVP.instance;

	public void run() {
		for (Player player : plugin.spiderman.hookedState.keySet()) {
			if ((player.isSneaking()) && plugin.spiderman.hooked.containsKey(player) && plugin.spiderman.hookedState.containsKey(player)) {
				if (plugin.spiderman.hooked.get(player).isValid() && plugin.spiderman.hookedState.get(player).equals(State.FISHING)) {
					Vector pull = plugin.spiderman.hooked.get(player).getLocation().subtract(player.getLocation()).toVector().normalize().multiply(0.05D).add(new Vector(0.0D, 0.05D, 0.0D));
					player.setVelocity(player.getVelocity().add(pull));
					if (pull.getY() > 0.0D) {
						player.setFallDistance(0.0F);
					}
					return;
				}
			}
		}
	}

}
