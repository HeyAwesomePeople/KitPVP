package me.HeyAwesomePeople.kitpvp.resources;

import me.HeyAwesomePeople.kitpvp.KitPVP;

import org.bukkit.Color;
import org.bukkit.Location;

import de.slikey.effectlib.effect.ExplodeEffect;

public class TrailCreator {

	private static KitPVP plugin = KitPVP.instance;

	public static void showHelix(Location loc, ParticleEffect effect, double radius) {

		for (double y = 5; y >= 0; y -= 0.008) {
			radius = y / 3;
			double x = (radius * Math.cos(1.6 * y));
			double z = (radius * Math.sin(1.6 * y));

			double y2 = 5 - y;

			Location loc2 = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y2, loc.getZ() + z);
			effect.display(0, 0, 0, 0, 1, loc2, 50D);
		}

		for (double y = 5; y >= 0; y -= 0.008) {
			radius = y / 3;
			double x = (-(radius * Math.cos(1.6 * y)));
			double z = (-(radius * Math.sin(1.6 * y)));

			double y2 = 5 - y;

			Location loc2 = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y2, loc.getZ() + z);
			effect.display(0, 0, 0, 0, 1, loc2, 50D);
		}

	}
	
	
	public static void showExplosion(Location loc, Color color) {
		ExplodeEffect ex = new ExplodeEffect(plugin.effectManager);
		ex.setLocation(loc);
		ex.visibleRange = 50F;
		ex.color = color;
		ex.start();
		
	}
	
	public static void showFireEffect(Location l) {
		ParticleEffect.FLAME.display(0, 0, 0, 0.05F, 2, l, 50D);
	}
	
	public static void showGrenadeEffect(Location l) {
		ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0.02F, 1, l, 30D);
	}

}
