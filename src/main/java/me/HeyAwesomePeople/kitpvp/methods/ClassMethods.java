package me.HeyAwesomePeople.kitpvp.methods;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;

import org.bukkit.entity.Player;

public class ClassMethods {
	private KitPVP plugin = KitPVP.instance;

	public Boolean doesPlayerHaveClass(Player p, String clzz) {
		return plugin.playerconfig.getPlayerConfig().contains("player." + p.getUniqueId() + ".classes." + clzz);
	}

	public void givePlayersDefaultClasses(Player p) {
		for (ClassManager m : plugin.manager.classList) {
			if (m.getDefClass()) {
				m.givePlayerClassConfig(p);
			}
		}
	}
	
}
