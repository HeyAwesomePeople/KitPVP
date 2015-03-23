package me.HeyAwesomePeople.kitpvp.custommobs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R2.EntityInsentient;
import net.minecraft.server.v1_8_R2.EntityTypes;

public class NMSUtils {
	public static void registerEntity(String name, int id,
			Class<? extends EntityInsentient> nmsClass,
			Class<? extends EntityInsentient> customClass) {
		try {

			/*
			 * First, we make a list of all HashMap's in the EntityTypes class
			 * by looping through all fields. I am using reflection here so we
			 * have no problems later when minecraft changes the field's name.
			 * By creating a list of these maps we can easily modify them later
			 * on.
			 */
			List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
			for (Field f : EntityTypes.class.getDeclaredFields()) {
				if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
					f.setAccessible(true);
					dataMaps.add((Map<?, ?>) f.get(null));
				}
			}

			/*
			 * since minecraft checks if an id has already been registered, we
			 * have to remove the old entity class before we can register our
			 * custom one
			 * 
			 * map 0 is the map with names and map 2 is the map with ids
			 */
			if (dataMaps.get(2).containsKey(id)) {
				dataMaps.get(0).remove(name);
				dataMaps.get(2).remove(id);
			}

			/*
			 * now we call the method which adds the entity to the lists in the
			 * EntityTypes class, now we are actually 'registering' our entity
			 */
			Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			method.setAccessible(true);
			method.invoke(null, customClass, name, id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void unregisterEntity(String name, int id,
			Class<? extends EntityInsentient> nmsClass,
			Class<? extends EntityInsentient> customClass) {
	}

}