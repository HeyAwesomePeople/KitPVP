package me.HeyAwesomePeople.kitpvp.resources;

import java.util.HashMap;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClassCooldowns {
	private KitPVP plugin = KitPVP.instance;
	
	public boolean hasCooldown(Player player, HashMap<UUID, Long> cooldowns,
			Integer seconds) {
		if (!cooldowns.containsKey(player.getUniqueId())) {
			return false;
		}
		if (cooldowns.get(player.getUniqueId()) < (System.currentTimeMillis() - seconds * 1000)) return false;
		else
			return true;
	}

	public void showPlayerCooldownTime(Player p, String msg,
			HashMap<UUID, Long> cooldown, Integer seconds, String ability) {
		plugin.screen.sendActionBar(p, msg + ChatColor.DARK_RED + ": Cannot use " + ability + " for another " + (((cooldown.get(p.getUniqueId()) - (System.currentTimeMillis() - (seconds * 1000))) / 1000) % 60) + " seconds!");
	}

	public void activateCooldown(Player player, HashMap<UUID, Long> cooldowns) {
		cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
	}

}
