package me.HeyAwesomePeople.kitpvp.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.custommobs.AgroSpider;
import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Spiderman implements Listener {

	private KitPVP plugin = KitPVP.instance;

	final public String staticName = "Spiderman";

	public String className;
	public String classPrefix;
	public String classDesc;
	public Double classCost;
	public ItemStack abilityItem;
	public ItemStack classItem;
	private Boolean enabled;

	// Cooldowns
	public HashMap<UUID, Long> webbed = new HashMap<UUID, Long>();
	public HashMap<UUID, Long> hook = new HashMap<UUID, Long>();
	public HashMap<UUID, Long> spiderattack = new HashMap<UUID, Long>();

	// Spiderattack
	private List<Player> spiderAttackInProgress = new ArrayList<Player>();

	// Hook
	public HashMap<Player, Fish> hooked = new HashMap<Player, Fish>();
	public HashMap<Player, State> hookedState = new HashMap<Player, State>();
	private List<Player> cancelNextLeft = new ArrayList<Player>();

	// Webbed
	private HashMap<Item, BukkitTask> item = new HashMap<Item, BukkitTask>();
	private List<Block> webbedBlocks = new ArrayList<Block>();

	public List<String> abilities = new ArrayList<String>();
	public ClassManager clazz;

	public Spiderman() {
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
		this.abilities.add("webbed");
		this.abilities.add("hook");
		this.abilities.add("spiderattack");
	}

	public void clearPlayer(Player p) {
		spiderAttackInProgress.remove(p);
		hooked.remove(p);
		hookedState.remove(p);
	}

	public void shutdown() {
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.hasMetadata("spiderattack")) {
					e.removeMetadata("spiderattack", plugin);
					e.remove();
				}
			}
		}

		for (Block b : this.webbedBlocks) {
			b.setType(Material.AIR);
		}
	}

	/************************* Webbed *********************/

	@EventHandler
	public void playerUseWebbed(PlayerInteractEvent e) {
		e.getPlayer().getItemInHand().setDurability((short) 0);
		if (!e.getPlayer().getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if (cancelNextLeft.contains(e.getPlayer())) {
				cancelNextLeft.remove(e.getPlayer());
				e.getPlayer().getItemInHand().setDurability((short) 0);
				return;
			}
			if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 0)) {
				plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Must have class prestige of atleast 0 to perform 'Webbed'!");
				return;
			}
			if (plugin.classcooldowns.hasCooldown(e.getPlayer(), webbed, 3)) {
				plugin.classcooldowns.showPlayerCooldownTime(e.getPlayer(), className, webbed, 3, "Webbed");
				return;
			}
			plugin.classcooldowns.activateCooldown(e.getPlayer(), webbed);
			throwWeb(e.getPlayer());
			return;
		}
		return;
	}

	public void throwWeb(Player p) {
		Item item = p.getWorld().dropItem(p.getLocation().add(new Vector(0, 1.5, 0)), new ItemStack(Material.WEB, 1));
		item.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		item.setMetadata("cannotPickUp", new FixedMetadataValue(plugin, "webbed"));
		this.item.put(item, startRepeater(item, (Entity) p));
	}

	@EventHandler
	public void onPickupEvent(PlayerPickupItemEvent e) {
		if (e.getItem().hasMetadata("cannotPickUp")) {
			e.setCancelled(true);
		}
	}

	public void startWebTimer(final Block b) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				b.setType(Material.AIR);
			}
		}, 20 * 10);
	}

	public BukkitTask startRepeater(final Item i, final Entity shooter) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				if ((i.getVelocity().getX() == 0.0 || i.getVelocity().getY() == 0.0 || i.getVelocity().getZ() == 0.0) || i.isOnGround()) {
					if (!plugin.manager.cMap.isPlayerInMap(i.getLocation())) {
						i.remove();
						item.get(i).cancel();
						return;
					}
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						public void run() {
							Block b = i.getWorld().getBlockAt(i.getLocation());
							b.setType(Material.WEB);
							webbedBlocks.add(b);
							startWebTimer(b);
						}
					});
					i.remove();
					item.get(i).cancel();
					return;
				}
			}

		}, 1L, 1L);
	}

	/****************** Hook **********************/

	@EventHandler
	public void grappleUse(PlayerFishEvent e) {
		e.getPlayer().getItemInHand().setDurability((short) 0);
		cancelNextLeft.add(e.getPlayer());
		if (!e.getPlayer().getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			return;
		}
		if (!e.getState().equals(State.FISHING)) {
			this.hooked.remove(e.getPlayer());
			this.hookedState.put(e.getPlayer(), State.FAILED_ATTEMPT);
			return;
		}
		if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 1)) {
			plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Not ready to be used");
			e.setCancelled(true);
			return;
		}
		if (plugin.classcooldowns.hasCooldown(e.getPlayer(), hook, 5)) {
			plugin.classcooldowns.showPlayerCooldownTime(e.getPlayer(), className, hook, 5, "Hooked");
			e.setCancelled(true);
			e.getPlayer().getItemInHand().setDurability((short) 0);
			return;
		}
		plugin.classcooldowns.activateCooldown(e.getPlayer(), hook);
		this.hooked.put(e.getPlayer(), e.getHook());
		this.hookedState.put(e.getPlayer(), e.getState());
	}

	/*************** Spider Attack ********************/

	@EventHandler
	public void useSpiderAttack(PlayerToggleSneakEvent e) {
		if (this.hooked.containsKey(e.getPlayer())) return;
		Player p = e.getPlayer();
		if (!p.getItemInHand().equals(this.abilityItem) || !plugin.classmethods.doesPlayerHaveClass(e.getPlayer(), staticName) || !plugin.manager.playersInGame.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if (!(this.clazz.getCPClassPrestige(e.getPlayer()) >= 2)) {
			plugin.screen.sendActionBar(e.getPlayer(), ChatColor.RED + "Must have class prestige of atleast 2 to perform 'Spider Attack'!");
			return;
		}
		if (!e.isSneaking()) {
			return;
		}
		if (spiderAttackInProgress.contains(e.getPlayer())) {
			plugin.screen.sendActionBar(p, ChatColor.RED + "Your spiders are still attacking!");
			return;
		}
		if (plugin.classcooldowns.hasCooldown(p, spiderattack, 30)) {
			plugin.classcooldowns.showPlayerCooldownTime(p, className, spiderattack, 30, "SpiderAttack");
			return;
		}
		plugin.screen.sendActionBar(p, ChatColor.RED + "Enemies will be attacked...");
		startSSTimer(spawnSpiders(e.getPlayer()), p);
		return;
	}

	@EventHandler
	public void spiderTarget(EntityTargetLivingEntityEvent e) {
		if (e.getEntity().getName() == null) return;
		if (e.getTarget() == null) return;
		if (e.getEntity().getName().equals("Spider") && e.getTarget().getType().equals(EntityType.PLAYER)) {
			if (!e.getEntity().hasMetadata("spiderattack")) return;

			String p = e.getEntity().getMetadata("spiderattack").get(0).asString();
			String t = e.getTarget().getUniqueId().toString();
			if (t.equals(p)) {
				e.setCancelled(true);
			}
		}
	}

	public List<Entity> spawnSpiders(Player p) {
		List<Entity> ens = new ArrayList<Entity>();
		this.spiderAttackInProgress.add(p);
		WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle();

		AgroSpider spi1 = new AgroSpider(nmsWorld);
		AgroSpider spi2 = new AgroSpider(nmsWorld);
		AgroSpider spi3 = new AgroSpider(nmsWorld);
		AgroSpider spi4 = new AgroSpider(nmsWorld);

		spi1.setPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() + 1.5);
		spi2.setPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() - 1.5);
		spi3.setPosition(p.getLocation().getX() + 1.5, p.getLocation().getY(), p.getLocation().getZ());
		spi4.setPosition(p.getLocation().getX() - 1.5, p.getLocation().getY(), p.getLocation().getZ());

		nmsWorld.addEntity(spi1);
		nmsWorld.addEntity(spi2);
		nmsWorld.addEntity(spi3);
		nmsWorld.addEntity(spi4);

		spi1.getBukkitEntity().setMetadata("spiderattack", new FixedMetadataValue(plugin, p.getUniqueId()));
		spi2.getBukkitEntity().setMetadata("spiderattack", new FixedMetadataValue(plugin, p.getUniqueId()));
		spi3.getBukkitEntity().setMetadata("spiderattack", new FixedMetadataValue(plugin, p.getUniqueId()));
		spi4.getBukkitEntity().setMetadata("spiderattack", new FixedMetadataValue(plugin, p.getUniqueId()));

		ens.add(spi1.getBukkitEntity());
		ens.add(spi2.getBukkitEntity());
		ens.add(spi3.getBukkitEntity());
		ens.add(spi4.getBukkitEntity());

		return ens;
	}

	public void startSSTimer(final List<Entity> list, final Player p) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				spiderAttackInProgress.remove(p);
				plugin.classcooldowns.activateCooldown(p, spiderattack);
				plugin.screen.sendActionBar(p, ChatColor.RED + "SpiderAttack has worn off!");
				for (Entity e : list) {
					if (!e.isValid()) continue;
					e.removeMetadata("spiderattack", plugin);
					e.remove();
				}
			}
		}, 20 * 15);
	}
}
