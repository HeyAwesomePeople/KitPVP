package me.HeyAwesomePeople.kitpvp.managers;

import java.util.ArrayList;
import java.util.List;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OUIManager {

	private KitPVP plugin = KitPVP.instance;

	private Boolean disabled;
	private String itemName;
	private List<String> itemLore;
	private ItemStack item;
	private Double cost;

	public OUIManager(Boolean disabled, String itemName, List<String> itemLore, ItemStack item, Double cost) {
		this.disabled = disabled;
		this.itemName = itemName;
		this.itemLore = itemLore;
		this.item = item;
		this.cost = cost;

		runVars();
		
		plugin.manager.ouilist.add(this);
	}

	private void runVars() {
		ItemMeta im = item.getItemMeta();
		List<String> lores = new ArrayList<String>();
		for (String l : this.itemLore) {
			lores.add(ChatColor.translateAlternateColorCodes('&', l));
		}
		im.setLore(lores);
		im.setDisplayName(this.itemName);
		this.item.setItemMeta(im);
	}

	public Boolean getDisabled() {
		return this.disabled;
	}

	public String getItemName() {
		return this.itemName;
	}

	public List<String> getItemLore() {
		return this.itemLore;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public Double getCost() {
		return this.cost;
	}

	public void givePlayerItem(Player p) {
		plugin.playermethods.getPlayerData(p).removeCredits(this.cost);
		plugin.screen.sendTitle(p, "", ChatColor.GOLD + this.itemName + " bought for " + this.cost + " credits", 10, 20, 10);
		p.getInventory().addItem(item);
	}

}
