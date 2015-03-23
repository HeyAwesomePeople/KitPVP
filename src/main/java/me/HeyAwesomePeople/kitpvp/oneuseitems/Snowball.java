package me.HeyAwesomePeople.kitpvp.oneuseitems;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Snowball implements Listener {
	private KitPVP plugin = KitPVP.instance;
	private OUIManager manager;
	private ItemStack snowball;

	public Snowball() {
		loadSnowball();

		manager = new OUIManager(plugin.config.snowball, plugin.config.sName, plugin.config.sLore, this.snowball, plugin.config.sCost);
	}

	public void loadSnowball() {
		this.snowball = new ItemStack(Material.SNOW_BALL, 1);
	}

	public OUIManager getManager() {
		return this.manager;
	}

	@EventHandler
	public void snowballThrown(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof org.bukkit.entity.Snowball) {
			e.getEntity().setMetadata("snowball", new FixedMetadataValue(plugin, "snowball"));
			return;
		}
	}

	@EventHandler
	public void onSnowballHit(EntityDamageByEntityEvent event) {
		Entity damaged = event.getEntity();
		Entity damageEntity = event.getDamager();
		if (damaged instanceof Player) {
			if (!damageEntity.hasMetadata("snowball")) return;
			float y = damaged.getLocation().getYaw();

			if (y >= 0 && y <= 89.9) {
				damaged.teleport(new Location(damaged.getWorld(), damaged.getLocation().getX(), damaged.getLocation().getY(), damaged.getLocation().getZ(), damaged.getLocation().getYaw() + 90, damaged.getLocation().getPitch()));
				// add 90
			}
			if (y >= 90 && y <= 179.9) {
				damaged.teleport(new Location(damaged.getWorld(), damaged.getLocation().getX(), damaged.getLocation().getY(), damaged.getLocation().getZ(), damaged.getLocation().getYaw() - 90, damaged.getLocation().getPitch()));
				// remove 90
			}
			if (y <= -0.1 && y >= -89.9) {
				damaged.teleport(new Location(damaged.getWorld(), damaged.getLocation().getX(), damaged.getLocation().getY(), damaged.getLocation().getZ(), damaged.getLocation().getYaw() - 90, damaged.getLocation().getPitch()));
				// remove 90
			}
			if (y <= -90 && y >= -180) {
				damaged.teleport(new Location(damaged.getWorld(), damaged.getLocation().getX(), damaged.getLocation().getY(), damaged.getLocation().getZ(), damaged.getLocation().getYaw() + 90, damaged.getLocation().getPitch()));
				// add 90
			}
		}
	}
}
