package me.HeyAwesomePeople.kitpvp.playerdata;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayersDataMethods {

	private KitPVP plugin = KitPVP.instance;

	public void loadPlayerData(Player p) {
		if (!plugin.manager.pData.containsKey(p) && (plugin.playerconfig.hasPlayed(p.getUniqueId()) == true)) {
			FileConfiguration c = plugin.playerconfig.getPlayerConfig();
			Integer kills = c.getInt("player." + p.getUniqueId() + ".kills");
			Integer deaths = c.getInt("player." + p.getUniqueId() + ".deaths");
			Integer xp = c.getInt("player." + p.getUniqueId() + ".xp");
			Integer ranks = c.getInt("player." + p.getUniqueId() + ".rank");
			Integer prestige = c.getInt("player." + p.getUniqueId() + ".prestige");

			if (kills == null) {
				kills = 0;
			}
			if (deaths == null) {
				deaths = 0;
			}
			if (xp == null) {
				xp = 0;
			}
			if (ranks == null) {
				ranks = 0;
			}
			if (prestige == null) {
				prestige = 0;
			}

			plugin.manager.pData.put(p, new PlayerData(p, kills, deaths, null));
			savePlayerData(p);
		}
	}

	public void newPlayerData(Player p) {
		if (!plugin.manager.pData.containsKey(p) && (plugin.playerconfig.hasPlayed(p.getUniqueId()) == false)) {
			plugin.manager.pData.put(p, new PlayerData(p, 0, 0, null));
			savePlayerData(p);
			plugin.classmethods.givePlayersDefaultClasses(p);
		}
	}

	public PlayerData getPlayerData(Player p) {
		if (plugin.manager.pData.containsKey(p)) {
			return plugin.manager.pData.get(p);
		} else {
			return null;
		}
	}

	public void savePlayerData(Player p) {
		if (plugin.manager.pData.containsKey(p)) {
			plugin.manager.pData.get(p).saveData();
		} else {
		}
	}

	public void saveAllPlayerData() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (plugin.manager.pData.containsKey(p)) {
				plugin.manager.pData.get(p).saveData();
			}
	}

	public void removePlayerData(Player p) {
		if (plugin.manager.pData.containsKey(p)) {
			plugin.manager.pData.get(p).closeData();
		} else {
		}
	}

	public Integer getPrestige(Player p) {
		if (plugin.manager.pData.containsKey(p)) {
			return plugin.manager.pData.get(p).getCCPrestige();
		} else {
			return null;
		}
	}

}
