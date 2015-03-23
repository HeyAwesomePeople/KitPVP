package me.HeyAwesomePeople.kitpvp.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class MMapManager {

	private KitPVP plugin = KitPVP.instance;

	private int xMin, xMax, zMin, zMax;
	private World world;

	private String name;
	private Integer id;
	private Location spawn;
	private Location c1;
	private Location c2;
	private HashMap<Integer, Location> spawns = new HashMap<Integer, Location>();

	public MMapManager(String mapName, Integer mapNum, Location spawnLoc, Location corner1, Location corner2) {
		this.name = mapName;
		this.id = mapNum;
		this.spawn = spawnLoc;
		this.c1 = corner1;
		this.c2 = corner2;

		loadSpawns();
		saveData();
		updateCuboid();
		plugin.manager.mapList.put(this.id, this);
	}

	/***************** Others *****************/

	public HashMap<Integer, Location> getSpawns() {
		return this.spawns;
	}
	
	public void updateCuboid() {
		if (c1 != null && c2 != null) {
			this.xMin = Math.min(c1.getBlockX(), c2.getBlockX());
			this.xMax = Math.max(c1.getBlockX(), c2.getBlockX());
			this.zMin = Math.min(c1.getBlockZ(), c2.getBlockZ());
			this.zMax = Math.max(c1.getBlockZ(), c2.getBlockZ());
			this.world = c1.getWorld();
		}
	}

	public Boolean isPlayerInMap(Location loc) {
		if (loc.getWorld() != this.world) return false;
		if (loc.getBlockX() < xMin) return false;
		if (loc.getBlockX() > xMax) return false;
		if (loc.getBlockZ() < zMin) return false;
		if (loc.getBlockZ() > zMax) return false;
		return true;
	}

	public void loadSpawns() {
		FileConfiguration c = plugin.mapsconfig.getMapsConfig();
		if (!c.contains("maps." + this.id + ".spawns")) return;
		for (String mapId : c.getConfigurationSection("maps." + this.id + ".spawns").getKeys(false)) {
			if (!plugin.methods.isInteger(mapId)) continue;
			String[] spaw = c.getString("maps." + this.id + ".spawns." + mapId).split(":");
			this.spawns.put(Integer.parseInt(mapId), new Location(Bukkit.getWorld(spaw[3]), Double.parseDouble(spaw[0]), Double.parseDouble(spaw[1]), Double.parseDouble(spaw[2]), Float.parseFloat(spaw[4]), Float.parseFloat(spaw[5])));
		}
	}

	public void saveData() {
		FileConfiguration c = plugin.mapsconfig.getMapsConfig();
		c.set("maps." + this.id, null);
		if (this.id == null) {
			return;
		}
		if (this.spawn != null) {
			c.set("maps." + this.id + ".spawn", this.spawn.getX() + ":" + this.spawn.getY() + ":" + this.spawn.getZ() + ":" + this.spawn.getWorld().getName() + ":" + this.spawn.getYaw() + ":" + this.spawn.getPitch());
		}
		if (this.c1 != null) {
			c.set("maps." + this.id + ".c1", this.c1.getX() + ":" + this.c1.getY() + ":" + this.c1.getZ() + ":" + this.c1.getWorld().getName() + ":" + this.c1.getYaw() + ":" + this.c1.getPitch());
		}
		if (this.c2 != null) {
			c.set("maps." + this.id + ".c2", this.c2.getX() + ":" + this.c2.getY() + ":" + this.c2.getZ() + ":" + this.c2.getWorld().getName() + ":" + this.c2.getYaw() + ":" + this.c2.getPitch());
		}
		if (!this.spawns.isEmpty()) {
			for (Location c2 : this.spawns.values()) {
				c.set("maps." + this.id + ".spawns." + getNewSpawnId(), c2.getX() + ":" + c2.getY() + ":" + c2.getZ() + ":" + c2.getWorld().getName() + ":" + c2.getYaw() + ":" + c2.getPitch());
			}
		}
		c.set("maps." + this.id + ".name", this.name);
		plugin.mapsconfig.saveMapsConfig();
	}

	public Integer getNewSpawnId() {
		FileConfiguration c = plugin.mapsconfig.getMapsConfig();
		List<Integer> mapIds = new ArrayList<Integer>();
		if (!c.contains("maps." + this.id + ".spawns")) {
			return 1;
		}
		for (String mapId : c.getConfigurationSection("maps." + this.id + ".spawns").getKeys(false)) {
			if (!plugin.methods.isInteger(mapId)) continue;
			mapIds.add(Integer.parseInt(mapId));
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

	public void remove() {
		plugin.mapsconfig.getMapsConfig().set("maps." + this.id, null);
		plugin.manager.mapList.remove(this.id);
	}

	/***************** Getters ******************/

	public String getMapName() {
		return this.name;
	}

	public Integer getMapId() {
		return this.id;
	}

	public Location getSpawnLoc() {
		return this.spawn;
	}

	public Location getCorner1() {
		return this.c1;
	}

	public Location getCorner2() {
		return this.c2;
	}

	public Location getRandomSpawn() {
		if (this.spawns.size() == 0) {
			return this.getSpawnLoc();
		}
		Random rand = new Random();
		int n = rand.nextInt(getSpawns().values().size()) + 1;
		return this.spawns.get(n);
	}

	public void removeSpawn(Integer i) {
		this.spawns.remove(i);
		this.saveData();
		return;
	}

	/*************** Setters ****************/

	public void setMapName(String name) {
		this.name = name;
	}

	public void setMapId(Integer id) {
		this.id = id;
		saveData();
	}

	public void setSpawnLoc(Location l) {
		this.spawn = l;
		saveData();
	}

	public void setCorner1(Location l1) {
		this.c1 = l1;
		saveData();
	}

	public void setCorner2(Location l2) {
		this.c2 = l2;
		saveData();
	}

	public void addSpawn(Location s) {
		this.spawns.put(getNewSpawnId(), s);
		saveData();
	}

}
