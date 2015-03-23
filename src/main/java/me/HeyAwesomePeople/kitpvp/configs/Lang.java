package me.HeyAwesomePeople.kitpvp.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	private KitPVP plugin = KitPVP.instance;

	private FileConfiguration langConfig = null;
	private File langConfigFile = null;

	private File file = new File(plugin.getDataFolder() + File.separator + "lang.yml");

	public Lang() {
		if (!file.exists()) {
			getLangConfig().set("errors.notEnoughCredits.msg", "&4Not enough credits!");
			getLangConfig().set("errors.notEnoughCredits.showTime", 20);
			saveLangConfig();
		}
	}

	@SuppressWarnings("deprecation")
	public void reloadLangConfig() {
		if (langConfigFile == null) {
			langConfigFile = new File(plugin.getDataFolder(), "lang.yml");
		}
		langConfig = YamlConfiguration.loadConfiguration(langConfigFile);

		InputStream defConfigStream = plugin.getResource("lang.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			langConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getLangConfig() {
		if (langConfig == null) {
			this.reloadLangConfig();
		}
		return langConfig;
	}

	public void saveLangConfig() {
		if (langConfig == null || langConfigFile == null) {
			return;
		}
		try {
			getLangConfig().save(langConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + langConfigFile, ex);
		}
	}

	public void save2langConfig() {
		if (langConfigFile == null) {
			langConfigFile = new File(plugin.getDataFolder(), "lang.yml");
		}
		if (!langConfigFile.exists()) {
			plugin.saveResource("lang.yml", false);
		}
	}

}
