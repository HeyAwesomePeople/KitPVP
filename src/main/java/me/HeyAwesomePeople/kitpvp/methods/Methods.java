package me.HeyAwesomePeople.kitpvp.methods;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Methods {
	// private KitPVP plugin = KitPVP.instance;
	
	public ItemStack makeItemStack(Material m, Integer amount, String name, List<String> lore) {
		List<String> l = new ArrayList<String>();
		for (String sm : lore) {
			l.add(ChatColor.translateAlternateColorCodes('&', sm));
		}
		ItemStack i = new ItemStack(m, amount);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(l);
		i.setItemMeta(im);
		return i;
	}

	public void clearInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
	}
	
	public String trans(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public void hidePlayer(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 60 * 60, 1));
	}

	public void unHidePlayer(Player p) {
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void debugMessage(String s) {
		Bukkit.broadcastMessage("[Debug] " + s);
	}
}
