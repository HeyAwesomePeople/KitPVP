package me.HeyAwesomePeople.kitpvp.repeating;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Scoreboard implements Runnable {
	private KitPVP plugin = KitPVP.instance;

	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			plugin.scoreboard.setScoreboard(p);
		}
	}

}
