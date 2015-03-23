package me.HeyAwesomePeople.kitpvp;

import java.io.File;
import java.lang.reflect.Field;

import me.HeyAwesomePeople.kitpvp.classes.FireNinja;
import me.HeyAwesomePeople.kitpvp.classes.Spiderman;
import me.HeyAwesomePeople.kitpvp.commands.AdminCommands;
import me.HeyAwesomePeople.kitpvp.commands.PlayerCommands;
import me.HeyAwesomePeople.kitpvp.configs.Config;
import me.HeyAwesomePeople.kitpvp.configs.Lang;
import me.HeyAwesomePeople.kitpvp.configs.MapsConfig;
import me.HeyAwesomePeople.kitpvp.configs.PlayerConfig;
import me.HeyAwesomePeople.kitpvp.custommobs.Register;
import me.HeyAwesomePeople.kitpvp.listeners.ExplosionRegeneraterListener;
import me.HeyAwesomePeople.kitpvp.listeners.InGameListener;
import me.HeyAwesomePeople.kitpvp.listeners.KitPVPListener;
import me.HeyAwesomePeople.kitpvp.listeners.SignListener;
import me.HeyAwesomePeople.kitpvp.listeners.WhitelistListener;
import me.HeyAwesomePeople.kitpvp.maps.MMapManager;
import me.HeyAwesomePeople.kitpvp.maps.MapManager;
import me.HeyAwesomePeople.kitpvp.methods.ClassMethods;
import me.HeyAwesomePeople.kitpvp.methods.GameMethods;
import me.HeyAwesomePeople.kitpvp.methods.Methods;
import me.HeyAwesomePeople.kitpvp.methods.ScoreboardManager;
import me.HeyAwesomePeople.kitpvp.methods.Screen;
import me.HeyAwesomePeople.kitpvp.oneuseitems.Flashbang;
import me.HeyAwesomePeople.kitpvp.oneuseitems.Grenade;
import me.HeyAwesomePeople.kitpvp.oneuseitems.RPG;
import me.HeyAwesomePeople.kitpvp.oneuseitems.Snowball;
import me.HeyAwesomePeople.kitpvp.playerdata.PlayersDataMethods;
import me.HeyAwesomePeople.kitpvp.repeating.GrappleHook;
import me.HeyAwesomePeople.kitpvp.repeating.SavePlayerData;
import me.HeyAwesomePeople.kitpvp.repeating.Scoreboard;
import me.HeyAwesomePeople.kitpvp.resources.BlankEnchant;
import me.HeyAwesomePeople.kitpvp.resources.ClassCooldowns;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class KitPVP extends JavaPlugin {

	public EffectManager effectManager;
	public static KitPVP instance;
	public BlankEnchant blankEnchant = new BlankEnchant(128);

	public final String kitpvp = ChatColor.GOLD + "[Kit" + ChatColor.RED + "PVP" + ChatColor.GOLD + "]";

	public CombatTagApi combatApi;

	public Config config;
	public Lang lang;
	public Logs log;
	public ClassCooldowns classcooldowns;
	public PlayerConfig playerconfig;
	public MapsConfig mapsconfig;
	public VariableManager manager;
	public Methods methods;
	public MapManager mapmanager;
	public GameMethods gamemethods;
	public PlayersDataMethods playermethods;
	public ScoreboardManager scoreboard;
	public Screen screen;
	public ClassMethods classmethods;
	public FireNinja fireninja;
	public Spiderman spiderman;

	public Economy econ = null;

	@Override
	public void onEnable() {
		instance = this;

		EffectLib lib = EffectLib.instance();
		effectManager = new EffectManager(lib);

		if (!new File(this.getDataFolder() + File.separator + "config.yml").exists()) {
			saveDefaultConfig();
		}

		registerEnchants();
		loadApis();
		loadClasses();
		loadCommands();
		loadListeners();

		startRepeaters();
		Register.registerEntities();
		
		mapmanager.loadMapsFromConffig();

		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.BLUE + " KitPVP has been successfully loaded!");

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kitpvp start");
	}

	@Override
	public void onDisable() {
		for (MMapManager mm : manager.mapList.values()) {
			mm.saveData();
		}

		playermethods.saveAllPlayerData();
		cleanup();

		for (Player p : Bukkit.getOnlinePlayers()) {
			gamemethods.removeClass(p);
			p.kickPlayer(ChatColor.RED + "Server restarting...");
		}
	}

	public void registerEnchants() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
		}
		try {
			Enchantment.registerEnchantment(this.blankEnchant);
		} catch (Exception e) {
		}

		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.RED + " Registered custom enchants.");
	}

	public void loadClasses() {
		lang = new Lang();
		config = new Config();
		log = new Logs();
		playerconfig = new PlayerConfig();
		mapsconfig = new MapsConfig();
		manager = new VariableManager();
		methods = new Methods();
		mapmanager = new MapManager();
		gamemethods = new GameMethods();
		playermethods = new PlayersDataMethods();
		classcooldowns = new ClassCooldowns();
		classmethods = new ClassMethods();
		scoreboard = new ScoreboardManager();
		screen = new Screen();
		fireninja = new FireNinja();
		spiderman = new Spiderman();
	}

	public void loadCommands() {
		getCommand("join").setExecutor(new PlayerCommands());
		getCommand("leave").setExecutor(new PlayerCommands());
		getCommand("logout").setExecutor(new PlayerCommands());

		getCommand("kitpvp").setExecutor(new AdminCommands());

		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.RED + " Loaded commands.");
	}

	public void loadListeners() {
		getServer().getPluginManager().registerEvents(new InGameListener(), this);
		getServer().getPluginManager().registerEvents(new KitPVPListener(), this);
		getServer().getPluginManager().registerEvents(new WhitelistListener(), this);
		getServer().getPluginManager().registerEvents(new ExplosionRegeneraterListener(), this);
		getServer().getPluginManager().registerEvents(new SignListener(), this);

		getServer().getPluginManager().registerEvents(fireninja, this);
		getServer().getPluginManager().registerEvents(spiderman, this);

		getServer().getPluginManager().registerEvents(new Grenade(), this);
		getServer().getPluginManager().registerEvents(new RPG(), this);
		getServer().getPluginManager().registerEvents(new Snowball(), this);
		getServer().getPluginManager().registerEvents(new Flashbang(), this);

		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.RED + " Loaded listeners.");
	}

	public void loadApis() {
		if (getServer().getPluginManager().getPlugin("CombatTag") != null) {
			combatApi = new CombatTagApi((CombatTag) getServer().getPluginManager().getPlugin("CombatTag"));
		} else {
			log.logToMiniLog("Failed to load CombatTag! CombatLogging will not work!", "SEVERE");
		}
		setupEconomy();
		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.RED + " Hooked into APIs.");
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}

		return (econ != null);
	}

	public void cleanup() {
		this.spiderman.shutdown();
		this.fireninja.shutdown();
		this.manager.cleanup();
	}

	public void startRepeaters() {
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new SavePlayerData(), 60L, 20 * (long) config.savePlayerDataInterval);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Scoreboard(), 60L, config.scoreboardUpdate);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new GrappleHook(), 1L, 1L);

		Bukkit.getServer().getConsoleSender().sendMessage(kitpvp + ChatColor.RED + " Started repeaters.");
	}
}
