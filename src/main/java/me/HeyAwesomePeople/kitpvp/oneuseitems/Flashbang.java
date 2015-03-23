package me.HeyAwesomePeople.kitpvp.oneuseitems;

import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;
import me.HeyAwesomePeople.kitpvp.resources.TrailCreator;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Flashbang implements Listener {
	private KitPVP plugin = KitPVP.instance;
	private OUIManager manager;
	private ItemStack flashbang;

	public Flashbang() {
		loadFlashbang();

		manager = new OUIManager(plugin.config.flashbang, plugin.config.fbName, plugin.config.fbLore, this.flashbang, plugin.config.fbCost);
	}

	public void loadFlashbang() {
		this.flashbang = new ItemStack(Material.SHEARS, 1);
	}

	public OUIManager getManager() {
		return this.manager;
	}

	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().equals(this.flashbang)) {
			if (!plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
				return;
			}

			ItemMeta dm = e.getItemDrop().getItemStack().getItemMeta();
			dm.setDisplayName(UUID.randomUUID().toString());
			e.getItemDrop().getItemStack().setItemMeta(dm);

			e.getItemDrop().setMetadata("unpickupable", new FixedMetadataValue(plugin, "flashbang"));
			beginTimer(e.getItemDrop());
		}
	}

	@EventHandler
	public void onPickupEvent(PlayerPickupItemEvent e) {
		if (e.getItem().hasMetadata("unpickupable")) {
			e.setCancelled(true);
		}
	}
	
	public void getNearbyPlayers(Item l) {
		for (Entity e : l.getNearbyEntities(plugin.config.fbEffectRange, plugin.config.fbEffectRange, plugin.config.fbEffectRange)) {
			if (e instanceof Player) {
				Player p = (Player) e;
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, plugin.config.fbEffectLast, 1));
				continue;
			}
		}
	}

	public void beginTimer(final Item i) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				i.remove();
				getNearbyPlayers(i);
				TrailCreator.showExplosion(i.getLocation(), Color.RED);
				return;
			}
		}, plugin.config.fbFakeExplosionDelay);
	}

}
