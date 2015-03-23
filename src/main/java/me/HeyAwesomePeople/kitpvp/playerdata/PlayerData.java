package me.HeyAwesomePeople.kitpvp.playerdata;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

	private KitPVP plugin = KitPVP.instance;

	private Player player;
	private int kills;
	private int deaths;

	private ClassManager currentClass;

	public PlayerData(Player player, int kills, int deaths, ClassManager currentClass) {
		this.setPlayer(player);
		this.setKills(kills);
		this.setDeaths(deaths);
		this.setClassManager(currentClass);

		plugin.manager.playerDataClasses.add(this);
	}

	/******************** Player API ************************/
	// Setters

	public void setPlayer(Player p) {
		this.player = p;
	}

	public void setKills(Integer k) {
		this.kills = k;
	}

	public void setDeaths(Integer d) {
		this.deaths = d;
	}

	//TODO test...

	public void addClassCredits(Double c) {
		Double added = 0.0;
		if (this.player.hasPermission("kitpvp.donor.creditmultiplier.25")) {
			added = (double) Math.round((c * 0.25) * 10) / 10;
		}
		if (this.player.hasPermission("kitpvp.donor.creditmultiplier.50")) {
			added = (double) Math.round((c * 0.50) * 10) / 10;
		}
		if (this.player.hasPermission("kitpvp.donor.creditmultiplier.75")) {
			added = (double) Math.round((c * 0.75) * 10) / 10;
		}
		if (this.player.hasPermission("kitpvp.donor.creditmultiplier.100")) {
			added = (double) Math.round((c * 0.100) * 10) / 10;
		}
		plugin.econ.depositPlayer(this.player, c + added);
	}

	public void addCredits(Double c) {
		plugin.econ.depositPlayer(this.player, c);
	}

	public Boolean hasCredits(Double c) {
		Boolean b = (plugin.econ.getBalance(this.player) >= c);
		if (!b) {
			plugin.screen.sendTitle(this.player, "", ChatColor.RED + "Need at least $" + c + ". You have " + this.getCredits() + "!", 10, 20, 10);
		}
		return b;
	}

	public void removeCredits(Double c) {
		plugin.econ.withdrawPlayer(this.player, c);
	}

	// Getters

	public Integer getKills() {
		return this.kills;
	}

	public Integer getDeaths() {
		return this.deaths;
	}

	public double getCredits() {
		return plugin.econ.getBalance(this.player);
	}

	public Player getPlayer() {
		return this.player;
	}

	// Others

	public void addDeath() {
		this.deaths = this.deaths + 1;
	}

	public void punishForCombatLog() {
		this.removeCredits(plugin.config.combatTagPunish);
	}

	/************** Class Ranking API ******************/
	// Setters
	public void setCCRank(Integer xp, Integer rank, Integer prestige) {
		this.getCClass().setCPRank(player, xp + ":" + rank + ":" + prestige);
	}

	public void prestigeCurrentClass() {
		if (this.getCanCCPrestige()) {
			Integer prestige = this.getCCPrestige();
			if (prestige < 3) {
				this.setCCRank(0, 0, this.getCCPrestige() + 1);
				player.sendMessage(plugin.kitpvp + ChatColor.BLUE + "You have prestiged to level " + prestige + ".");
				saveData();
			}
		} else {
			player.sendMessage(plugin.kitpvp + ChatColor.RED + " Unabled to prestige, Rank not level 60!");
		}
	}

	public void setClassManager(ClassManager fireninjaman) {
		this.currentClass = fireninjaman;
	}
	
	//TODO test
	
	private Integer getXpPerKill() {
		Integer c = plugin.config.xpPerKill;
		Integer xp = 0;
		if (this.player.hasPermission("kitpvp.donor.expmultiplier.25")) {
			xp = (int) Math.round(c * 0.25);
		}
		if (this.player.hasPermission("kitpvp.donor.expmultiplier.50")) {
			xp = (int) Math.round(c * 0.50);
		}
		if (this.player.hasPermission("kitpvp.donor.expmultiplier.75")) {
			xp = (int) Math.round(c * 0.75);
		}
		if (this.player.hasPermission("kitpvp.donor.expmultiplier.100")) {
			xp = (int) Math.round(c * 0.100);
		}
		return (c + xp);
	}

	public void addKill() {
		this.setKills(this.getKills() + 1);
		this.addClassCredits(plugin.config.creditsPerKill);
		if (this.getCCRank() >= 60) return;
		this.setCCRank(this.getCCXp() + this.getXpPerKill(), this.getCCRank(), this.getCCPrestige());
		if (this.getCCXp() > (50 * 0.10) * this.getCCRank()) {
			upRank();
		}
	}

	public void upRank() {
		this.setCCRank(0, this.getCCRank() + 1, this.getCCPrestige());
		if (this.getCanCCPrestige()) {
			this.prestigeCurrentClass();
		}
	}

	// Getters

	public Boolean getCanCCPrestige() {
		if (this.getCCRank() >= 60) return true;
		else
			return false;
	}

	public Integer getCCXp() {
		String xp = this.getCClass().getCPRank(player)[0];
		if (plugin.methods.isInteger(xp)) {
			return Integer.parseInt(xp);
		} else {
			return 0;
		}
	}

	public Integer getCCRank() {
		String xp = this.getCClass().getCPRank(player)[1];
		if (plugin.methods.isInteger(xp)) {
			return Integer.parseInt(xp);
		} else {
			return 0;
		}
	}

	public Integer getCCPrestige() {
		String xp = this.getCClass().getCPRank(player)[2];
		if (plugin.methods.isInteger(xp)) {
			return Integer.parseInt(xp);
		} else {
			return 0;
		}
	}

	public String getCurrentClassName() {
		if (this.getCClass() == null) {
			return null;
		}
		if (this.getCClass().getClassName() == null) {
			return null;
		}
		return this.getCClass().getClassName();
	}

	public ClassManager getCClass() {
		return this.currentClass;
	}

	/****************************************************/

	public void saveData() {
		FileConfiguration c = plugin.playerconfig.getPlayerConfig();
		c.set("player." + player.getUniqueId() + ".kills", this.kills);
		c.set("player." + player.getUniqueId() + ".deaths", this.deaths);
		c.set("player." + player.getUniqueId() + ".lastName", this.player.getDisplayName());
		plugin.playerconfig.savePlayerConfig();
	}

	public void closeData() {
		plugin.manager.playerDataClasses.remove(this);
		plugin.manager.pData.remove(this.getPlayer());
	}

}
