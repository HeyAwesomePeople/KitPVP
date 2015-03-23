package me.HeyAwesomePeople.kitpvp.listeners;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import me.HeyAwesomePeople.kitpvp.playerdata.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class InGameListener implements Listener {
	private KitPVP plugin = KitPVP.instance;

	@EventHandler
	public void onPlayerKillPlayer(final PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getEntity().getKiller() instanceof Player) {
				PlayerData killer = plugin.manager.pData.get(e.getEntity().getKiller());
				killer.addKill();
			}
			PlayerData victim = plugin.manager.pData.get(e.getEntity());
			victim.addDeath();
			victim.getPlayer().spigot().respawn();
			e.getDrops().remove(victim.getCClass().abilityItem);
			
			return;
		}
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (plugin.manager.playersInGame.contains(p.getUniqueId())) {
			if (!plugin.manager.cMap.isPlayerInMap(e.getTo())) {
				e.setTo(e.getFrom());
				return;
			}
		}
	}
	
	@EventHandler
	public void onMouseScroll(PlayerItemHeldEvent e) {
		if (plugin.manager.pData.get(e.getPlayer()).getCClass() == null) return;
		if (e.getPlayer().getItemInHand().equals(plugin.manager.pData.get(e.getPlayer()).getCClass().abilityItem)) {
			//plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Avaliable Abilities: " + plugin.manager.pData.get(e.getPlayer()).getCClass().getCPAbilities(e.getPlayer()));
			return;
		}
	}

	@EventHandler
	public void onRespawnEvent(final PlayerRespawnEvent e) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				plugin.gamemethods.removePlayerFromGame(e.getPlayer());
			}
		}, 1L);

	}
	
	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent e) {
		for (ClassManager cm : plugin.manager.classList) {
			if (cm.abilityItem.equals(e.getItemDrop().getItemStack())) {
				e.setCancelled(true);
			}
		}
	}

}
