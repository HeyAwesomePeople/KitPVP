package me.HeyAwesomePeople.kitpvp.listeners;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class WhitelistListener implements Listener {
	private KitPVP plugin = KitPVP.instance;

	@EventHandler
	public void playerPlaceBlock(BlockPlaceEvent e) {
		if (!plugin.config.blockPlaceWhitelist.contains(e.getBlock().getType().toString()) && (!e.getPlayer().hasPermission("kitpvp.placeblock.bypass")) && (!e.getPlayer().hasPermission("kitpvp.placeblock." + e.getBlock().getType().toString()))) {
			e.setBuild(false);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void playerBreakBlock(BlockBreakEvent e) {
		if (!plugin.config.blockBreakWhitelist.contains(e.getBlock().getType().toString()) && (!e.getPlayer().hasPermission("kitpvp.breakblock.bypass")) && (!e.getPlayer().hasPermission("kitpvp.breakblock." + e.getBlock().getType().toString()))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e) {
		if (!plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			return;
		}
		for (OUIManager m : plugin.manager.ouilist) {
			if (m.getItem().equals(e.getItemDrop().getItemStack())) {
				return;
			}
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		if (plugin.config.dropItemsOnDeath) {
			e.getDrops().clear();
		}
	}
	
}
