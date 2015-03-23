package me.HeyAwesomePeople.kitpvp.listeners;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class KitPVPListener implements Listener {
	private KitPVP plugin = KitPVP.instance;

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			ClassManager cM = plugin.manager.pData.get(e.getPlayer()).getCClass();
			String msg = plugin.config.inGame.replace("$class", cM.getClassName()).replace("$rank", cM.getCPRank(e.getPlayer())[1]).replace("$prestige", cM.getCPRank(e.getPlayer())[2]);
			e.setFormat(msg);
		} else {
			String msg = plugin.config.outGame;
			e.setFormat(msg);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (plugin.playerconfig.hasPlayed(p.getUniqueId()) == false) {
			plugin.playermethods.newPlayerData(p);
			plugin.screen.sendTitle(p, plugin.config.newPlayerMsg.replace("%player%", p.getDisplayName()), "", 20 * plugin.config.fadeTime, 20 * plugin.config.showTime, 20 * plugin.config.fadeTime);
			plugin.log.logToMiniLog(p.getUniqueId() + "(" + p.getDisplayName() + ") has joined the server for the first time!", "INFO");
		} else {
			plugin.playermethods.loadPlayerData(p);
			plugin.screen.sendTitle(p, plugin.config.joinMsg.replace("%player%", p.getDisplayName()), "", 20 * plugin.config.fadeTime, 20 * plugin.config.showTime, 20 * plugin.config.fadeTime);
		}

		plugin.scoreboard.setScoreboard(p);
		plugin.gamemethods.teleportToHub(p);
		if (!plugin.config.resourcePack.equals("false")) {
			p.setResourcePack(plugin.config.resourcePack);
		}
		
		p.setMaxHealth(40.0);
		p.setHealth(40.0);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		checkForCombat(e.getPlayer());
		plugin.gamemethods.removePlayerFromGame(e.getPlayer());
		plugin.manager.pData.get(e.getPlayer()).closeData();
	}

	@EventHandler
	public void achievmentGet(PlayerAchievementAwardedEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void foodChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void serverPingEvent(ServerListPingEvent e) {
		e.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
		if (plugin.manager.cMap == null) {
			e.setMotd(plugin.config.motdStopped);
		} else {
			e.setMotd(plugin.config.motdStarted.replace("%map%", plugin.manager.cMap.getMapName()));
		}
	}

	private void checkForCombat(Player p) {
		if (plugin.combatApi.isInCombat(p)) {
			plugin.manager.pData.get(p).punishForCombatLog();
		}
	}

	@EventHandler
	public void playerInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		for (ClassManager cm : plugin.manager.classList) {
			if (e.getInventory().equals(e.getWhoClicked().getInventory()) && e.getCurrentItem().equals(cm.abilityItem)) {
				e.setResult(Result.DENY);
				e.setCancelled(true);
				return;
			}
		}
	}

}
