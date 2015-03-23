package me.HeyAwesomePeople.kitpvp.listeners;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
	private KitPVP plugin = KitPVP.instance;

	@EventHandler
	public void signChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[Class]") || e.getLine(0).equalsIgnoreCase("[Item]") || e.getLine(0).equalsIgnoreCase("[Join]")) {
			e.setLine(0, plugin.config.signColor + e.getLine(0));
		}
	}
	
	@EventHandler
	public void signClickListener(PlayerInteractEvent e) {
		if (e.getAction() == null) return;
		if (e.getClickedBlock() == null) return;
		Block b = e.getClickedBlock();
		Player p = e.getPlayer();

		if (b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
			BlockState bs = b.getState();
			Sign sign = (Sign) bs;
			if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(ChatColor.stripColor(plugin.config.signMsgToJoin))) {
				plugin.gamemethods.putPlayerInGame(p);
				return;
			} else if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(ChatColor.stripColor("[Class]"))) {
				if (ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("Clear")) {
					plugin.gamemethods.removeClass(p);
					return;
				}
				for (ClassManager s : plugin.manager.classList) {
					if (ChatColor.stripColor(s.staticName).equalsIgnoreCase(ChatColor.stripColor(sign.getLine(1)))) {
						s.giveClass(p);
						p.updateInventory();
						return;
					}
				}
				return;
			} else if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(ChatColor.stripColor("[Item]"))) {
				for (OUIManager s : plugin.manager.ouilist) {
					if (ChatColor.stripColor(s.getItemName()).equals(ChatColor.stripColor(sign.getLine(1)))) {
						if (!plugin.manager.pData.get(p).hasCredits(s.getCost())) return;
						s.givePlayerItem(p);
						p.updateInventory();
						return;
					}
				}
			} else if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Join]")) {
				p.performCommand("join");
				return;
			}
		}
	}

}
