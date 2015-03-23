package me.HeyAwesomePeople.kitpvp.commands;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.configs.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
	private KitPVP plugin = KitPVP.instance;

	public boolean onCommand(final CommandSender s, Command cmd,
			String commandLabel, final String[] args) {
		if (commandLabel.equalsIgnoreCase("kitpvp")) {
			if (args.length == 0) {
				s.sendMessage(ChatColor.RED + "KitPVP Commands");
				if (s.hasPermission("kitpvp.admin")) {
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp start");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp stop");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp addmap <name>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp removemap <mapid>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp setcorner1 <mapid>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp setcorner2 <mapid>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp addspawn <mapid>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp teleport <mapid>");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp maprotation");
					s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp reload");
				} else if (s.hasPermission("kitpvp.player")) {
					s.sendMessage(ChatColor.DARK_AQUA + "/join");
					s.sendMessage(ChatColor.DARK_AQUA + "/leave");
					s.sendMessage(ChatColor.DARK_AQUA + "/logout");
				}

			} else {
				if (args.length == 1) {
					if (!s.hasPermission("kitpvp.admin")) {
						s.sendMessage(plugin.kitpvp + ChatColor.RED + " You do not have permission to preform that commands!");
						return false;
					}
					/**************** Debug Commands *********************/
					
					if (args[0].equalsIgnoreCase("rankup")) {
						if (s instanceof ConsoleCommandSender) {
							s.sendMessage(plugin.kitpvp + ChatColor.RED + " Cannot preform command in console!");
							return false;
						}
						Player p = (Player) s;
						plugin.manager.pData.get(p).setCCRank(15, 35, 2);
						plugin.manager.pData.get(p).addCredits(1500.0);
						return false;
					}

					if (args[0].equalsIgnoreCase("stats")) {
						if (s instanceof ConsoleCommandSender) {
							s.sendMessage(plugin.kitpvp + ChatColor.RED + " Cannot preform command in console!");
							return false;
						}
						Player p = (Player) s;
						Bukkit.broadcastMessage("In Game: " + plugin.manager.playersInGame);
						Bukkit.broadcastMessage("In Spectate: " + plugin.manager.playersInSpectate);
						Bukkit.broadcastMessage("MapID: " + plugin.manager.cMap.getMapId());
						Bukkit.broadcastMessage("Map Name: " + plugin.manager.cMap.getMapName());
						Bukkit.broadcastMessage("Hub: " + plugin.mapmanager.getHub(p.getWorld()));
						Bukkit.broadcastMessage("Started: " + plugin.manager.gameStarted);
						Bukkit.broadcastMessage("ClassList: " + plugin.manager.classList);
						return false;
					}

					/************************* Admin Commands ****************************/
					if (args[0].equalsIgnoreCase("start")) {
						s.sendMessage(plugin.gamemethods.startGame());
					}
					if (args[0].equalsIgnoreCase("stop")) {
						s.sendMessage(plugin.gamemethods.stopGame());
					}
					if (args[0].equalsIgnoreCase("sethub")) {
						if (s instanceof ConsoleCommandSender) {
							s.sendMessage(plugin.kitpvp + ChatColor.RED + " Cannot preform command in console!");
							return false;
						}
						plugin.mapmanager.setHub(((Player) s).getLocation());
						s.sendMessage(plugin.kitpvp + ChatColor.BLUE + " Hub has been set to your location!");
						return false;
					}
					if (args[0].equalsIgnoreCase("maprotation")) {
						plugin.mapmanager.showAllMaps(s);
						return false;
					}
					if (args[0].equalsIgnoreCase("reload")) {
						plugin.reloadConfig();
						plugin.playerconfig.reloadPlayerConfig();
						plugin.mapsconfig.reloadMapsConfig();
						plugin.lang.reloadLangConfig();
						plugin.config = new Config();
						return false;
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("addmap")) {
						if (!(s instanceof Player)) return false;
						Player p = (Player) s;
						if (args[1] == null) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp addmap <name>");
							return false;
						}
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Map successfully added, spawn set to your location! MapID: " + plugin.mapmanager.createNewMap(args[1], p.getLocation(), null, null).getMapId());
					}
					if (args[0].equalsIgnoreCase("setcorner1")) {
						if (!(s instanceof Player)) return false;
						Player p = (Player) s;
						if (args[1] == null || plugin.methods.isInteger(args[1]) == false) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp setcorner1 <mapid>");
							return false;
						}
						if (!plugin.manager.mapList.containsKey(Integer.parseInt(args[1]))) {
							s.sendMessage(ChatColor.DARK_AQUA + "Map ID does not exist!");
							return false;
						}
						plugin.mapmanager.setCorner1(Integer.parseInt(args[1]), p.getLocation());
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Added corner one to map!");
					}
					if (args[0].equalsIgnoreCase("setcorner2")) {
						if (!(s instanceof Player)) return false;
						Player p = (Player) s;
						if (args[1] == null || plugin.methods.isInteger(args[1]) == false) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp setcorner2 <mapid>");
							return false;
						}
						if (!plugin.manager.mapList.containsKey(Integer.parseInt(args[1]))) {
							s.sendMessage(ChatColor.DARK_AQUA + "Map ID does not exist!");
							return false;
						}
						plugin.mapmanager.setCorner2(Integer.parseInt(args[1]), p.getLocation());
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Added corner two to map!");
					}
					if (args[0].equalsIgnoreCase("addspawn")) {
						if (!(s instanceof Player)) return false;
						Player p = (Player) s;
						if (args[1] == null || plugin.methods.isInteger(args[1]) == false) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp addspawn <mapid>");
							return false;
						}
						if (!plugin.manager.mapList.containsKey(Integer.parseInt(args[1]))) {
							s.sendMessage(ChatColor.DARK_AQUA + "Map ID does not exist!");
							return false;
						}
						plugin.mapmanager.addSpawn(Integer.parseInt(args[1]), p.getLocation());
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Added spawn to map!");
					}
					if (args[0].equalsIgnoreCase("removemap")) {
						if (args[1] == null || plugin.methods.isInteger(args[1]) == false) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp removemap <mapid>");
							return false;
						}
						if (!plugin.manager.mapList.containsKey(Integer.parseInt(args[1]))) {
							s.sendMessage(ChatColor.DARK_AQUA + "Map ID does not exist!");
							return false;
						}
						plugin.mapmanager.deleteMap(Integer.parseInt(args[1]));
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Map successfully removed!");
					}
					if (args[0].equalsIgnoreCase("teleport")) {
						if (s instanceof ConsoleCommandSender) return false;
						if (args[1] == null || plugin.methods.isInteger(args[1]) == false) {
							s.sendMessage(ChatColor.DARK_AQUA + "/kitpvp teleport <mapid>");
							return false;
						}
						if (!plugin.manager.mapList.containsKey(Integer.parseInt(args[1]))) {
							s.sendMessage(ChatColor.DARK_AQUA + "Map ID does not exist!");
							return false;
						}
						((Player) s).teleport(plugin.manager.mapList.get(args[1]).getSpawnLoc());
						s.sendMessage(plugin.kitpvp + ChatColor.GREEN + " Teleported to " + plugin.manager.mapList.get(args[1]).getMapName());
					}
				}
			}
		}
		return false;
	}
}
