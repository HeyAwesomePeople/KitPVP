package me.HeyAwesomePeople.kitpvp.oneuseitems;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;
import me.HeyAwesomePeople.kitpvp.resources.TrailCreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

public class Grenade implements Listener {
	private KitPVP plugin = KitPVP.instance;
	private OUIManager manager;
	private ItemStack grenade;

	public Grenade() {
		loadGrenade();

		manager = new OUIManager(plugin.config.grenade, plugin.config.gName, plugin.config.gLore, this.grenade, plugin.config.gCost);
	}

	public void loadGrenade() {
		this.grenade = new ItemStack(Material.SLIME_BALL, 1);
	}
	
	public OUIManager getManager() {
		return this.manager;
	}
	
	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().equals(this.grenade)) {
			if (!plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
				return;
			}
			
			ItemMeta dm = e.getItemDrop().getItemStack().getItemMeta();
			dm.setDisplayName(UUID.randomUUID().toString());
			e.getItemDrop().getItemStack().setItemMeta(dm);
			
			e.getItemDrop().setMetadata("unpickupable", new FixedMetadataValue(plugin, "grenade"));
			beginTimer(beginSizzle(e.getItemDrop()), e.getItemDrop());
		}
	}
	
	@EventHandler
	public void onPickupEvent(PlayerPickupItemEvent e) {
		if (e.getItem().hasMetadata("unpickupable")) {
			e.setCancelled(true);
		}
	}
	
	public List<BukkitTask> beginSizzle(final Item i) {
		List<BukkitTask> tasks = new ArrayList<BukkitTask>();
		BukkitTask a = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				TrailCreator.showGrenadeEffect(i.getLocation());
			}

		}, 1L, 1L);
		tasks.add(a);
		return tasks;
	}
	
	public void beginTimer(final List<BukkitTask> bt, final Item i) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				i.getWorld().playSound(i.getLocation(), Sound.FUSE, 5, 2);
			}
		}, plugin.config.gFizzDelay);

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				i.remove();
				for (BukkitTask t : bt) {
					t.cancel();
				}
				i.getWorld().createExplosion(i.getLocation(), plugin.config.gExplosionPower);
			}
		}, plugin.config.gExplosionDelay);
	}

}
