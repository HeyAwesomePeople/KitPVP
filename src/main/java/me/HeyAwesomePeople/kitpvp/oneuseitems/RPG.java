package me.HeyAwesomePeople.kitpvp.oneuseitems;

import java.util.HashMap;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;
import me.HeyAwesomePeople.kitpvp.resources.TrailCreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class RPG implements Listener {
	private KitPVP plugin = KitPVP.instance;
	private OUIManager manager;
	private ItemStack rpg;

	public HashMap<UUID, Long> launch = new HashMap<UUID, Long>();

	private HashMap<Item, Vector> vels = new HashMap<Item, Vector>();
	private HashMap<Item, BukkitTask> item = new HashMap<Item, BukkitTask>();

	public RPG() {
		loadItem();

		manager = new OUIManager(plugin.config.rpg, plugin.config.rpgName, plugin.config.rpgLore, this.rpg, plugin.config.rpgCost);
	}

	public void loadItem() {
		this.rpg = new ItemStack(Material.DIAMOND_BARDING, 1);
	}

	public OUIManager getManager() {
		return this.manager;
	}

	@EventHandler
	public void playerDropItemEvent(PlayerInteractEvent e) {
		if (!plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) return;
		if (e.getPlayer().getItemInHand().equals(this.rpg)) {
			throwRPG(e.getPlayer());
		}
	}

	private void removeOneOfItemInHand(Player p) {
		if (p.getItemInHand().getAmount() == 1) {
			p.setItemInHand(new ItemStack(Material.AIR));
			return;
		}
		p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
	}

	public void throwRPG(Player p) {
		if (plugin.classcooldowns.hasCooldown(p, launch, 3)) {
			plugin.classcooldowns.showPlayerCooldownTime(p, "RPG", launch, plugin.config.rpgCooldown, "RPG");
			return;
		}
		plugin.classcooldowns.activateCooldown(p, launch);
		Item item = p.getWorld().dropItem(p.getLocation().add(new Vector(0, 1.5, 0)), new ItemStack(Material.PAPER, 1));
		item.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		item.setMetadata("rpg", new FixedMetadataValue(plugin, "rpg"));
		this.item.put(item, startRepeater(item, (Entity) p));
		removeOneOfItemInHand(p);
	}

	public BukkitTask startRepeater(final Item i, final Entity shooter) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				TrailCreator.showFireEffect(i.getLocation());
				if (i.getVelocity() == null) {
					return;
				}
				vels.put(i, i.getVelocity());
				if (i.getVelocity().getX() == 0.0 || i.getVelocity().getY() == 0.0 || i.getVelocity().getZ() == 0.0) {
					i.remove();
					item.get(i).cancel();
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							i.getWorld().createExplosion(i.getLocation(), plugin.config.rpgExplosionPower);
							return;
						}
					});
					return;
				}
				if (i.isOnGround()) {
					i.remove();
					item.get(i).cancel();
					return;
				}
			}

		}, 1L, 1L);
	}

	@EventHandler
	public void onPickupEvent(PlayerPickupItemEvent e) {
		if (e.getItem().hasMetadata("rpg")) {
			e.setCancelled(true);
		}
	}

}
