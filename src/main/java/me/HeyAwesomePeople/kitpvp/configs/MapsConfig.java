package me.HeyAwesomePeople.kitpvp.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapsConfig {
	private KitPVP plugin = KitPVP.instance;

	private FileConfiguration mapsConfig = null;
	private File mapsConfigFile = null;

	private File file2 = new File(plugin.getDataFolder() + File.separator + "maps.yml");

	public MapsConfig() {
		if (!file2.exists()) {
			Location loc = Bukkit.getWorld("world").getSpawnLocation();
			getMapsConfig().set("backupSpawn", loc.toString());
			getMapsConfig().set("maps.currentMap", 0);
			saveMapsConfig();
		}
	}

	@SuppressWarnings("deprecation")
	public void reloadMapsConfig() {
		if (mapsConfigFile == null) {
			mapsConfigFile = new File(plugin.getDataFolder(), "maps.yml");
		}
		mapsConfig = YamlConfiguration.loadConfiguration(mapsConfigFile);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource("maps.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			mapsConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMapsConfig() {
		if (mapsConfig == null) {
			this.reloadMapsConfig();
		}
		return mapsConfig;
	}

	public void saveMapsConfig() {
		if (mapsConfig == null || mapsConfigFile == null) {
			return;
		}
		try {
			getMapsConfig().save(mapsConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + mapsConfigFile, ex);
		}
	}

	public void save2mapsConfig() {
		if (mapsConfigFile == null) {
			mapsConfigFile = new File(plugin.getDataFolder(), "maps.yml");
		}
		if (!mapsConfigFile.exists()) {
			plugin.saveResource("maps.yml", false);
		}
	}

}
