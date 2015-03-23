package me.HeyAwesomePeople.kitpvp.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import me.HeyAwesomePeople.kitpvp.resources.ParticleEffect;
import me.HeyAwesomePeople.kitpvp.resources.TrailCreator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class FireNinja implements Listener {

	private KitPVP plugin = KitPVP.instance;

	final public String staticName = "FireNinja";

	public String className;
	public String classPrefix;
	public String classDesc;
	public Double classCost;
	public ItemStack abilityItem;
	public ItemStack classItem;

	private Boolean enabled;

	// Invisibility
	public List<Player> hasShifted = new ArrayList<Player>();

	// Cooldowns
	public HashMap<UUID, Long> ninjaport = new HashMap<UUID, Long>();
	public HashMap<UUID, Long> flaminstar = new HashMap<UUID, Long>();
	public HashMap<UUID, Long> invisibility = new HashMap<UUID, Long>();

	// FlaminStar
	private HashMap<Item, BukkitTask> item = new HashMap<Item, BukkitTask>();
	public List<String> abilities = new ArrayList<String>();

	public ClassManager clazz;

	public FireNinja() {
		loadOtherVars();
		loadAbilities();

		clazz = new ClassManager(enabled, className, staticName, classDesc, classItem, true, classCost, abilities, abilityItem, "kitpvp.class." + staticName);
	}

	private void loadOtherVars() {
		this.enabled = plugin.getConfig().getBoolean("classes." + staticName + ".enabled");
		this.className = plugin.methods.trans(plugin.getConfig().getString("classes." + staticName + ".className"));
		this.classPrefix = plugin.methods.trans(plugin.getConfig().getString("classes." + staticName + ".prefix"));
		this.classDesc = plugin.methods.trans(plugin.getConfig().getString("classes." + staticName + ".classDesc"));
		this.classCost = plugin.getConfig().getDouble("classes." + staticName + ".classCost");
		this.abilityItem = plugin.methods.makeItemStack(Material.getMaterial(plugin.getConfig().getString("classes." + staticName + ".abilityItem.item")), 1, plugin.getConfig().getString("classes." + staticName + ".abilityItem.name"), plugin.getConfig().getStringList("classes." + staticName + ".abilityItem.lore"));
		return;
	}

	private void loadAbilities() {
		this.abilities.add("ninjaport");
		this.abilities.add("flaminstar");
		this.abilities.add("invisibility");
	}

	public void clearPlayer(Player p) {
		hasShifted.remove(p);
	}

	public void shutdown() {
		for (Item i : item.keySet()) {
			i.remove();
		}
	}
	
	/************************* NinjaPort *********************/

	@EventHandler
	public void playerUseNinjaport(PlayerInteractEvent e) {
		if (!e.getPlayer().getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 0)) {
				plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Must have class prestige of atleast 0 to perform 'NinjaPort'!");
				return;
			}
			if (getTarget(e.getPlayer()) != null) {
				if (plugin.classcooldowns.hasCooldown(e.getPlayer(), ninjaport, 3)) {
					plugin.classcooldowns.showPlayerCooldownTime(e.getPlayer(), className, ninjaport, 3, "NinjaPort");
					return;
				}
				if (!plugin.manager.cMap.isPlayerInMap(getTarget(e.getPlayer()).getLocation())) {
					plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Entity is outside of map!");
					return;
				}
				plugin.classcooldowns.activateCooldown(e.getPlayer(), ninjaport);
				e.getPlayer().teleport(getTarget(e.getPlayer()));

				TrailCreator.showHelix(e.getPlayer().getLocation(), ParticleEffect.FLAME, 3);
			} else {
				plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Entity too far away to teleport!");
			}
			return;
		}
	}

	public Entity getTarget(Player p) {
		List<Entity> nearbyE = p.getNearbyEntities(30, 30, 30);
		ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

		for (Entity e : nearbyE) {
			if (e instanceof LivingEntity) {
				livingE.add((LivingEntity) e);
			}
		}

		BlockIterator bItr = new BlockIterator(p, 30);
		Block block;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;

		while (bItr.hasNext()) {
			block = bItr.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			for (LivingEntity e : livingE) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
					return e;
				}
			}
		}
		return null;
	}

	/****************** FLAMING STAR **********************/

	@EventHandler
	public void playerUseFlamingStar(PlayerInteractEvent e) {
		if (!e.getPlayer().getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 1)) {
				plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Must have class prestige of atleast 1 to perform 'FlaminStar'!");
				return;
			}
			if (plugin.classcooldowns.hasCooldown(e.getPlayer(), flaminstar, 3)) {
				plugin.classcooldowns.showPlayerCooldownTime(e.getPlayer(), className, flaminstar, 3, "FlaminStar");
				return;
			}
			plugin.classcooldowns.activateCooldown(e.getPlayer(), flaminstar);
			throwNinjaStar(e.getPlayer());
			return;
		}
	}

	@EventHandler
	public void onPickupEvent(PlayerPickupItemEvent e) {
		if (e.getItem().hasMetadata("unfireable")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Item && e.getCause().equals(DamageCause.FIRE)) {
			if (e.getEntity().hasMetadata("unfireable")) {
				e.setCancelled(true);
			}
		}
	}

	public void throwNinjaStar(Player p) {
		Item item = p.getWorld().dropItem(p.getLocation().add(new Vector(0, 1.5, 0)), new ItemStack(Material.NETHER_STAR, 1));
		item.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		item.setFireTicks(20000);
		item.setMetadata("unfireable", new FixedMetadataValue(plugin, ""));
		this.item.put(item, startRepeater(item, (Entity) p));
	}

	public BukkitTask startRepeater(final Item i, final Entity shooter) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				TrailCreator.showFireEffect(i.getLocation());
				if (i.isOnGround()) {
					i.remove();
					item.get(i).cancel();
					return;
				}
				for (Entity en : i.getNearbyEntities(1.5, 1.5, 1.5)) {
					en.setFireTicks(40);
					i.remove();
					item.get(i).cancel();
					break;
				}
			}

		}, 1L, 1L);
	}

	/*************** INVISIBILITY ********************/

	@EventHandler
	public void toggleInvisibilityEvent(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if (!p.getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 2)) {
			plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Must have class prestige of atleast 2 to perform 'Invisibility'!");
			return;
		}
		if (!e.isSneaking()) {
			return;
		}
		if (this.hasShifted.contains(p)) {
			this.hasShifted.remove(p);
			plugin.methods.unHidePlayer(p);
			plugin.screen.sendActionBar(p, ChatColor.RED + "*poof* Unvanished!");
			TrailCreator.showExplosion(p.getLocation(), Color.RED);
		} else {
			if (plugin.classcooldowns.hasCooldown(p, invisibility, 10)) {
				plugin.classcooldowns.showPlayerCooldownTime(p, className, invisibility, 10, "Invisibility");
				return;
			}
			plugin.classcooldowns.activateCooldown(p, invisibility);
			this.hasShifted.add(p);
			plugin.methods.hidePlayer(p);
			plugin.screen.sendActionBar(p, ChatColor.RED + "*poof* Vanished!");
			TrailCreator.showExplosion(p.getLocation(), Color.RED);
			startInvisTimer(p);
		}
		return;
	}

	private void startInvisTimer(final Player p) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (hasShifted.contains(p)) {
					hasShifted.remove(p);
					plugin.methods.unHidePlayer(p);
					TrailCreator.showExplosion(p.getLocation(), Color.RED);
					plugin.screen.sendActionBar(p, ChatColor.RED + "*poof* Unvanished!");
				}
			}
		}, 20 * 10L);
	}
}
