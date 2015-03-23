package me.HeyAwesomePeople.kitpvp.commands;

import java.util.concurrent.TimeUnit;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands implements CommandExecutor {
	private KitPVP plugin = KitPVP.instance;

	public boolean onCommand(final CommandSender s, Command cmd,
			String commandLabel, final String[] args) {
		if (!(s instanceof Player)) return false;
		if (plugin.manager.playerCmdsEnabled == false) return false;
		final Player p = (Player) s;
		if (commandLabel.equalsIgnoreCase("join")) {
			if (!p.hasPermission("kitpvp.player.join")) return false;
			if (plugin.manager.gameStarted == false) {
				p.sendMessage(plugin.kitpvp + ChatColor.BLUE + " Game not started yet!");
				return false;
			}
			if (plugin.manager.playersInGame.contains(p.getUniqueId())) {
				p.sendMessage(plugin.kitpvp + ChatColor.BLUE + " You are already in the game!");
				return false;
			}
			plugin.gamemethods.putPlayerInGame(p);
			return false;
		}
		if (commandLabel.equalsIgnoreCase("leave")) {
			if (!p.hasPermission("kitpvp.player.leave")) return false;
			if (plugin.manager.gameStarted == false) {
				p.sendMessage(plugin.kitpvp + ChatColor.BLUE + " Game not started yet!");
				return false;
			}
			if (plugin.combatApi.isInCombat(p)) {
				p.sendMessage(plugin.kitpvp + ChatColor.RED + " You must be free of combat for " + TimeUnit.MILLISECONDS.toSeconds(plugin.combatApi.getRemainingTagTime(p)) + " more seconds before leaving the game!");
				return false;
			}

			plugin.gamemethods.removePlayerFromGame(p);
			return false;
		}
		if (commandLabel.equalsIgnoreCase("logout")) {
			if (!p.hasPermission("kitpvp.player.logout")) return false;
			if (plugin.combatApi.isInCombat(p)) {
				p.sendMessage(plugin.kitpvp + ChatColor.RED + " You must be free of combat for " + TimeUnit.MILLISECONDS.toSeconds(plugin.combatApi.getRemainingTagTime(p)) + " more seconds before leaving the server!");
				return false;
			}
			p.kickPlayer(plugin.kitpvp + ChatColor.GOLD + " Safely logged off.");
			return false;
		}
		return false;
	}
}
