package me.HeyAwesomePeople.kitpvp.configs;

import java.util.List;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	private KitPVP plugin = KitPVP.instance;
	private FileConfiguration config = plugin.getConfig();
	private FileConfiguration lang = plugin.lang.getLangConfig();
	
	// Whitelist/Blacklist
	public List<String> blockPlaceWhitelist;
	public List<String> blockBreakWhitelist;
	
	// Maps Config
	public Integer mapRotateTime;
	public String mapChangeMessage;
	
	// General Settings
	public Boolean dropItemsOnDeath;
	public Boolean enableFood;
	public Integer scoreboardUpdate;
	public String resourcePack;
	public Integer savePlayerDataInterval;
	public Double combatTagPunish;
	public Integer xpPerKill;
	public Double creditsPerKill;
	public Boolean moveInvItems;
	public String motdStarted;
	public String motdStopped;
	public String signColor;
	
	// Chat formatting
	public String outGame;
	public String inGame;
	
	
	// Explosion regen
	public Integer regenDelay;
	public Integer regenSpeed;
	public Integer gravityDelay;
	
	// Shops
	public Boolean grenade;
	public Double gCost;
	public String gName;
	public Integer gExplosionDelay;
	public Integer gFizzDelay;
	public Integer gExplosionPower;
	public List<String> gLore;
	
	public Boolean rpg;
	public Double rpgCost;
	public String rpgName;
	public Integer rpgExplosionPower;
	public List<String> rpgLore;
	public Integer rpgCooldown;
	
	public Boolean flashbang;
	public Double fbCost;
	public String fbName;
	public List<String> fbLore;
	public Integer fbFakeExplosionDelay;
	public Double fbEffectRange;
	public Integer fbEffectLast;
	
	public Boolean snowball;
	public Double sCost;
	public String sName;
	public List<String> sLore;
	
	// Sign Settings
	public String signMsgToJoin;
	
	// Class Selection
	
	// New Players/Returning Players
	public String newPlayerMsg;
	public String joinMsg;
	public Integer showTime;
	public Integer fadeTime;
	
	
	/*********** LANG ************/
	public String notEnoughCredits;
	public Integer necShowtime;
	
	
	public Config() {
		blockPlaceWhitelist = config.getStringList("blockPlaceWhitelist");
		blockBreakWhitelist = config.getStringList("blockBreakWhitelist");
		
		mapRotateTime = config.getInt("mapRotateTime");
		mapChangeMessage = ChatColor.translateAlternateColorCodes('&', config.getString("mapChangeMessage"));
		
		dropItemsOnDeath = config.getBoolean("dropItemsOnDeath");
		enableFood = config.getBoolean("enableFood");
		resourcePack = config.getString("resourcePack");
		scoreboardUpdate = config.getInt("scoreboardUpdateInterval");
		savePlayerDataInterval = config.getInt("savePlayerDataInterval");
		combatTagPunish = config.getDouble("combatTagPunish");
		xpPerKill = config.getInt("xpPerKill");
		creditsPerKill = config.getDouble("creditsPerKill");
		moveInvItems = config.getBoolean("moveInventoryItems");
		motdStarted = ChatColor.translateAlternateColorCodes('&', config.getString("motd.gameStarted"));
		motdStopped = ChatColor.translateAlternateColorCodes('&', config.getString("motd.gameStopped"));
		signColor = ChatColor.translateAlternateColorCodes('&', config.getString("signColor"));
		
		inGame = ChatColor.translateAlternateColorCodes('&', config.getString("chat.inGame"));
		outGame = ChatColor.translateAlternateColorCodes('&', config.getString("chat.outOfGame"));
		
		regenDelay = config.getInt("explosion.regenDelay");
		regenSpeed = config.getInt("explosion.speed");
		gravityDelay = config.getInt("explosion.gravityBlockDisappearDelay");
		
		grenade = config.getBoolean("shopitems.grenade.enabled");
		gCost = config.getDouble("shopitems.grenade.cost");
		gName = ChatColor.translateAlternateColorCodes('&', config.getString("shopitems.grenade.name"));
		gLore = config.getStringList("shopitems.grenade.lore");
		gExplosionDelay = config.getInt("shopitems.grenade.explosionDelay");
		gFizzDelay = config.getInt("shopitems.grenade.fizzDelay");
		gExplosionPower = config.getInt("shopitems.grenade.power");
		
		rpg = config.getBoolean("shopitems.rpg.enabled");
		rpgCost = config.getDouble("shopitems.rpg.cost");
		rpgName = ChatColor.translateAlternateColorCodes('&', config.getString("shopitems.rpg.name"));
		rpgExplosionPower = config.getInt("shopitems.rpg.power");
		rpgLore = config.getStringList("shopitems.rpg.lore");
		rpgCooldown = config.getInt("shopitems.rpg.cooldown");
		
		flashbang = config.getBoolean("shopitems.flashbang.enabled");
		fbCost = config.getDouble("shopitems.flashbang.cost");
		fbName = ChatColor.translateAlternateColorCodes('&', config.getString("shopitems.flashbang.name"));
		fbLore = config.getStringList("shopitems.flashbang.lore");
		fbFakeExplosionDelay = config.getInt("shopitems.flashbang.delay");
		fbEffectRange = config.getDouble("shopitems.flashbang.effect.range");
		fbEffectLast = config.getInt("shopitems.flashbang.effect.last");
		
		snowball = config.getBoolean("shopitems.snowball.enabled");
		sCost = config.getDouble("shopitems.snowball.cost");
		sName = ChatColor.translateAlternateColorCodes('&', config.getString("shopitems.snowball.name"));
		sLore = config.getStringList("shopitems.snowball.lore");
		
		signMsgToJoin = ChatColor.translateAlternateColorCodes('&', config.getString("signMsgToJoinGame"));
		
		newPlayerMsg = ChatColor.translateAlternateColorCodes('&', config.getString("newPlayerMsg"));
		joinMsg = ChatColor.translateAlternateColorCodes('&', config.getString("joinMsg"));
		showTime = config.getInt("showTime");
		fadeTime = config.getInt("fadeTime");
		
		
		/*********** LANG ************/
		
		notEnoughCredits = ChatColor.translateAlternateColorCodes('&', lang.getString("errors.notEnoughCredits.msg"));
		necShowtime = lang.getInt("errors.notEnoughCredits.showTime");

	}
}
