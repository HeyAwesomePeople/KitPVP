package me.HeyAwesomePeople.kitpvp.resources;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class BlankEnchant extends Enchantment {

	public BlankEnchant(int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public String getName() {
		return "BlankEnchant";
	}

	@Override
	public int getId() {
		return 128;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

}