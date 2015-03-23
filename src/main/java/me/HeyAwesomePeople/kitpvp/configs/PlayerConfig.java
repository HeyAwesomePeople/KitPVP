package me.HeyAwesomePeople.kitpvp.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerConfig {
	private KitPVP plugin = KitPVP.instance;

	private FileConfiguration playerConfig = null;
	private File playerConfigFile = null;

	private File file = new File(plugin.getDataFolder() + File.separator + "players.yml");

	public PlayerConfig() {
		if (!file.exists()) {
			getPlayerConfig().set("player.Player.kills", 420);
			getPlayerConfig().set("player.Player.deaths", 69);
			getPlayerConfig().set("player.Player.credits", 21);
			savePlayerConfig();
		}
	}
	
	public Boolean hasPlayed(UUID id) {
		if (this.getPlayerConfig().contains("player." + id)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public void reloadPlayerConfig() {
		if (playerConfigFile == null) {
			playerConfigFile = new File(plugin.getDataFolder(), "players.yml");
		}
		playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource("players.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			playerConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getPlayerConfig() {
		if (playerConfig == null) {
			this.reloadPlayerConfig();
		}
		return playerConfig;
	}

	public void savePlayerConfig() {
		if (playerConfig == null || playerConfigFile == null) {
			return;
		}
		try {
			getPlayerConfig().save(playerConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + playerConfigFile, ex);
		}
	}

	public void save2playerConfig() {
		if (playerConfigFile == null) {
			playerConfigFile = new File(plugin.getDataFolder(), "players.yml");
		}
		if (!playerConfigFile.exists()) {
			plugin.saveResource("player.yml", false);
		}
	}

}
