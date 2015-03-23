package me.HeyAwesomePeople.kitpvp.methods;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameMethods {
	private KitPVP plugin = KitPVP.instance;

	/************************************** Admin methods **************************************/
	public String startGame() {
		if (plugin.manager.gameStarted == true) {
			return plugin.kitpvp + ChatColor.GREEN + " Already started!";
		} else {
			if (plugin.manager.cMap == null) {
				plugin.mapmanager.setRandomMap();
			}
			plugin.log.logToMiniLog("KitPVP game was started!", "INFO");
			plugin.manager.gameStarted = true;
			plugin.manager.playerCmdsEnabled = true;

			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Let the playing resume!");
			}

			return plugin.kitpvp + ChatColor.GREEN + " Game Started.";
		}
	}

	public void restartMap() {
		if (plugin.manager.gameStarted == false) {
			this.startGame();
		} else {
			plugin.log.logToMiniLog("KitPVP game was restarted!", "INFO");
			plugin.manager.cMap = null;
			plugin.manager.gameStarted = false;
			plugin.manager.playerCmdsEnabled = false;

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (plugin.manager.playersInGame.contains(p.getUniqueId()) || plugin.manager.playersInSpectate.contains(p.getUniqueId())) {
					removePlayerFromGame(p);
					p.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Map is changing!");
				}
			}

			this.startGame();
		}
	}

	public String stopGame() {
		if (plugin.manager.gameStarted == false) {
			return plugin.kitpvp + ChatColor.GREEN + " Already stopped!";
		} else {
			plugin.log.logToMiniLog("KitPVP game was stopped!", "INFO");
			plugin.manager.cMap = null;
			plugin.manager.gameStarted = false;
			plugin.manager.playerCmdsEnabled = false;

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (plugin.manager.playersInGame.contains(p.getUniqueId()) || plugin.manager.playersInSpectate.contains(p.getUniqueId())) {
					removePlayerFromGame(p);
					p.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Game has been stopped.");
				}
			}

			return ChatColor.GREEN + "[KitPVP] Game Stopped.";
		}
	}

	public void removeClass(Player p) {
		plugin.fireninja.clearPlayer(p);
		plugin.spiderman.clearPlayer(p);
		
		for (ClassManager cm : plugin.manager.classList) {
			p.getInventory().remove(cm.abilityItem);
		}
		
		plugin.manager.pData.get(p).setClassManager(null);
	}

	public void putPlayerInGame(Player p) {
		if (!plugin.manager.playersInGame.contains(p.getUniqueId())) {
			plugin.manager.playersInGame.add(p.getUniqueId());
		}
		p.teleport(plugin.manager.cMap.getRandomSpawn());
		p.setHealth(40.0);
	}

	public void removePlayerFromGame(Player p) {
		this.teleportToHub(p);
		removeClass(p);
		plugin.manager.playersInGame.remove(p.getUniqueId());
	}

	public void removePlayerInfoFromGame(Player p) {
		removeClass(p);
		plugin.fireninja.clearPlayer(p);
		plugin.spiderman.clearPlayer(p);
		plugin.manager.playersInGame.remove(p.getUniqueId());
	}

	/************ Teleports *************/

	public void teleportToHub(Player p) {
		p.teleport(plugin.mapmanager.getHub(p.getWorld()));
	}

	public String toCurrentMap(Player p) {
		if (plugin.manager.cMap == null) return plugin.kitpvp + ChatColor.RED + " Failed to retrieve map ID.";
		if (plugin.manager.cMap == null) return plugin.kitpvp + ChatColor.RED + " Failed to retrieve map spawn location.";
		p.teleport(plugin.manager.cMap.getSpawnLoc());
		return plugin.kitpvp + ChatColor.GREEN + " Teleported into the game!";
	}

}
