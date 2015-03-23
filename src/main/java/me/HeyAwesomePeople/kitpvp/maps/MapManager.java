package me.HeyAwesomePeople.kitpvp.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class MapManager implements Listener {

	/*
	 * Note - Where you set the map is where the spawn will be
	 */

	private KitPVP plugin = KitPVP.instance;

	public MapManager() {

	}

	public void loadMapsFromConffig() {
		FileConfiguration c = plugin.mapsconfig.getMapsConfig();
		for (String mapId : c.getConfigurationSection("maps").getKeys(false)) {
			if (!plugin.methods.isInteger(mapId)) continue;
			String spawn = c.getString("maps." + mapId + ".spawn");
			String c1 = c.getString("maps." + mapId + ".c1");
			String c2 = c.getString("maps." + mapId + ".c2");
			Location a = null;
			Location b = null;
			Location ab = null;
			if (spawn != null) {
				String[] spaw = spawn.split(":");
				a = new Location(Bukkit.getWorld(spaw[3]), Double.parseDouble(spaw[0]), Double.parseDouble(spaw[1]), Double.parseDouble(spaw[2]), Float.parseFloat(spaw[4]), Float.parseFloat(spaw[5]));
			}
			if (c1 != null) {
				String[] spaw = c1.split(":");
				b = new Location(Bukkit.getWorld(spaw[3]), Double.parseDouble(spaw[0]), Double.parseDouble(spaw[1]), Double.parseDouble(spaw[2]), Float.parseFloat(spaw[4]), Float.parseFloat(spaw[5]));
			}
			if (c2 != null) {
				String[] spaw = c2.split(":");
				ab = new Location(Bukkit.getWorld(spaw[3]), Double.parseDouble(spaw[0]), Double.parseDouble(spaw[1]), Double.parseDouble(spaw[2]), Float.parseFloat(spaw[4]), Float.parseFloat(spaw[5]));
			}
			new MMapManager(c.getString("maps." + mapId + ".name"), Integer.parseInt(mapId), a, b, ab);
			Bukkit.getServer().getConsoleSender().sendMessage(plugin.kitpvp + ChatColor.RED + " Loaded map " + mapId + "(" + ChatColor.BLUE + c.getString("maps." + mapId + ".name") + ChatColor.RED + ")!");
		}
	}

	private Integer getNextOpenId() {
		List<Integer> mapIds = new ArrayList<Integer>();
		for (MMapManager m : plugin.manager.mapList.values()) {
			mapIds.add(m.getMapId());
		}
		if (mapIds.size() == 0) {
			return 1;
		}
		int mapId = 1;
		while (mapIds.contains(mapId)) {
			mapId++;
		}
		return mapId;
	}

	public MMapManager createNewMap(String name, Location one, Location two,
			Location three) {
		return new MMapManager(name, getNextOpenId(), one, two, three);
	}

	public void deleteMap(Integer i) {
		plugin.manager.mapList.get(i).remove();
	}

	public void setCorner1(Integer i, Location l1) {
		plugin.manager.mapList.get(i).setCorner1(l1);
	}

	public void setCorner2(Integer i, Location l2) {
		plugin.manager.mapList.get(i).setCorner2(l2);
	}
	
	public void addSpawn(Integer i, Location ls) {
		plugin.manager.mapList.get(i).addSpawn(ls);
	}

	public void setRandomMap() {
		Random rand = new Random();
		plugin.manager.cMap = plugin.manager.mapList.get(rand.nextInt(plugin.manager.mapList.size()) + 1);
		plugin.mapsconfig.getMapsConfig().set("maps.currentMap", plugin.manager.cMap.getMapId());
		plugin.mapsconfig.saveMapsConfig();
	}

	public void showAllMaps(CommandSender p) {
		p.sendMessage(ChatColor.BLUE + "[KitPVP] Maps currently in rotation: ");
		for (MMapManager map : plugin.manager.mapList.values()) {
			if (map.getMapId().equals(plugin.manager.cMap)) {
				p.sendMessage(ChatColor.RED + "" + map.getMapId() + ": " + map.getMapName());
			} else {
				p.sendMessage(ChatColor.BLUE + "" + map.getMapId() + ": " + map.getMapName());
			}
		}
	}

	public void setHub(Location l) {
		plugin.mapsconfig.getMapsConfig().set("hub", l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getWorld().getName() + ":" + l.getYaw() + ":" + l.getPitch());
		plugin.mapsconfig.saveMapsConfig();
	}

	public Location getHub(World w) {
		if (plugin.mapsconfig.getMapsConfig().getString("hub") == null && (w != null)) {
			return w.getSpawnLocation();
		}
		String[] s = plugin.mapsconfig.getMapsConfig().getString("hub").split(":");
		return new Location(Bukkit.getWorld(s[3]), Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
	}

}
