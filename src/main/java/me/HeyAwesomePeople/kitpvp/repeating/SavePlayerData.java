package me.HeyAwesomePeople.kitpvp.repeating;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class SavePlayerData implements Runnable {
	private KitPVP plugin = KitPVP.instance;
	
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (plugin.manager.playersInGame.contains(p.getUniqueId())) {
				plugin.playermethods.savePlayerData(p);
			}
		}
	}

}
