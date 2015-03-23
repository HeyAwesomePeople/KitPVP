package me.HeyAwesomePeople.kitpvp.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ExplosionRegeneraterListener implements Listener {
	private KitPVP plugin = KitPVP.instance;

	public Double getRandomVel(Double min, Double max) {
		Random generator = new Random();
		return generator.nextDouble() * (max - min) + min;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void explode(EntityExplodeEvent e) {
		final List<BlockState> blocks = new ArrayList<BlockState>();

		// Remove blocks that cannot be destroyed
		Iterator<Block> iter = e.blockList().iterator();
		while (iter.hasNext()) {
			Block b = iter.next();
			if (!plugin.manager.cMap.isPlayerInMap(b.getLocation()) || b.hasMetadata("nodestroy")) {
				iter.remove();
			}
		}

		for (Block b : e.blockList()) {
			final BlockState state = b.getState();
			if (b.getType().equals(Material.AIR)) continue;
			
			b.setType(Material.AIR);
			if (!blocks.contains(state)) {
				blocks.add(state);
				FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
				fb.setVelocity(new Vector(getRandomVel(-0.8, 0.8), getRandomVel(0.1, 0.5), getRandomVel(-0.8, 0.8)));
			}
		}
		new BukkitRunnable() {
			public void run() {
				regen(blocks, true, plugin.config.regenSpeed);
				this.cancel();
			}
		}.runTaskTimer(plugin, plugin.config.regenDelay * 20L, plugin.config.regenDelay * 20L);
	}

	@EventHandler
	public void removeFallingBlock(final EntityChangeBlockEvent e) {
		e.getBlock().setMetadata("nodestroy", new FixedMetadataValue(plugin, "nonono"));
		
		for (Player p : e.getBlock().getWorld().getPlayers()) {
			if (p.getLocation().distance(e.getBlock().getLocation()) <= 0.5) {
				p.damage(2.0);
			}
		}
		
		new BukkitRunnable() {
			public void run() {
				e.getBlock().removeMetadata("nodestroy", plugin);
				e.getBlock().setType(Material.AIR);
				this.cancel();
			}
		}.runTaskTimer(plugin, plugin.config.gravityDelay * 20L, plugin.config.gravityDelay * 20L);
	}

	public void regen(final List<BlockState> blocks, final boolean effect,
			final int speed) {
		new BukkitRunnable() {
			int i = -1;

			@SuppressWarnings("deprecation")
			public void run() {
				if (i != blocks.size() - 1) {
					i++;
					BlockState bs = blocks.get(i);
					bs.getBlock().setType(bs.getType());
					bs.getBlock().setData(bs.getBlock().getData());
					bs.update();
					if (effect) bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
				} else {
					for (BlockState bs : blocks) {
						bs.getBlock().setType(bs.getType());
						bs.getBlock().setData(bs.getBlock().getData());
						bs.update();
					}
					blocks.clear();
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, speed, speed);
	}

}
