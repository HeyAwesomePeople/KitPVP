package me.HeyAwesomePeople.kitpvp.managers;

import java.util.List;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClassManager {
	private KitPVP plugin = KitPVP.instance;
	private Boolean disabled;
	public String staticName;
	private String className;
	private String displayName;
	private String description;
	private ItemStack classItem;
	private Boolean defaultC;
	private Double classCost;
	private List<String> abilities;
	public ItemStack abilityItem;
	private String permission;

	public ClassManager(Boolean disabled, String dName, String staticName, String desc, ItemStack classItem, Boolean defaultC, Double classCost, List<String> abilities, ItemStack abilityI, String perm) {
		this.disabled = disabled;
		this.staticName = staticName;
		this.className = dName;
		this.displayName = dName;
		this.description = desc;
		this.classItem = classItem;
		this.classCost = classCost;
		this.defaultC = defaultC;
		this.abilities = abilities;
		this.abilityItem = abilityI;
		this.permission = perm;

		this.plugin.manager.classList.add(this);
	}

	public Double getClassCost() {
		return this.classCost;
	}

	public String getPermission() {
		return this.permission;
	}
	
	public List<String> abilities() {
		return this.abilities;
	}

	/***************************************** CLASS API ************************************************/

	public void setCPRank(Player p, String rank) {
		plugin.playerconfig.getPlayerConfig().set("player." + p.getUniqueId().toString() + ".classes." + this.staticName + ".rank", rank);
		plugin.playerconfig.savePlayerConfig();
	}

	public String[] getCPRank(Player p) {
		return plugin.playerconfig.getPlayerConfig().getString("player." + p.getUniqueId().toString() + ".classes." + this.staticName + ".rank").split(":");
	}

	public Boolean doesCPHaveClass(Player p) {
		return plugin.playerconfig.getPlayerConfig().contains("player." + p.getUniqueId().toString() + ".classes." + this.staticName);
	}
	
	public Integer getCPClassPrestige(Player p) {
		if (plugin.methods.isInteger(this.getCPRank(p)[2])) {
			return Integer.parseInt(this.getCPRank(p)[2]);
		}
		return 0;
	}
	
	public void giveClass(Player p) {
		p.getInventory().addItem(this.abilityItem);
		p.updateInventory();
		plugin.manager.pData.get(p).setClassManager(this);
		plugin.scoreboard.setScoreboard(p);
		plugin.screen.sendTitle(p, "", ChatColor.RED + this.getClassName() + ChatColor.GOLD + " selected.", 20, 20, 20);
	}
	
	public void buyClass(Player p) {
		if (plugin.manager.pData.get(p).hasCredits(this.classCost)) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddp " + p.getName() + this.permission);
			plugin.screen.sendTitle(p, "", ChatColor.RED + "You have bought the " + this.className + " class!", 10, 20, 10);
		}
	}

	public void givePlayerClassConfig(Player p) {
		setCPRank(p, "0:0:0");
		plugin.playerconfig.savePlayerConfig();
	}

	/****************************************************************************************************/
	public Boolean getDisabled() {
		return this.disabled;
	}

	public Boolean getDefClass() {
		return this.defaultC;
	}

	public String getClassName() {
		return this.className;
	}

	public String getDescription() {
		return this.description;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public ItemStack getClassItem() {
		return this.classItem;
	}
}
