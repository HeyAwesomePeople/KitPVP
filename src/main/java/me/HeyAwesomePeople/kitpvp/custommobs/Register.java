package me.HeyAwesomePeople.kitpvp.custommobs;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_8_R2.EntityInsentient;
import net.minecraft.server.v1_8_R2.EntitySpider;
import net.minecraft.server.v1_8_R2.EntityTypes;

import org.bukkit.entity.EntityType;

public enum Register {
	SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, AgroSpider.class);

	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;

	private Register(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
	}

	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.id;
	}

	public EntityType getEntityType() {
		return this.entityType;
	}

	public Class<? extends EntityInsentient> getNMSClass() {
		return this.nmsClass;
	}

	public Class<? extends EntityInsentient> getCustomClass() {
		return this.customClass;
	}

	public static void registerEntities() {
		for (Register entity : values()) {
			NMSUtils.registerEntity(entity.getName(), entity.getID(), entity.getNMSClass(), entity.getCustomClass());
		}
	}

	public static void unregisterEntities() {
		for (Register entity : values()) {
			NMSUtils.unregisterEntity(entity.getName(), entity.getID(), entity.getNMSClass(), entity.getCustomClass());
		}
	}

	private static Object getPrivateStatic(
			@SuppressWarnings("rawtypes") Class clazz, String f)
			throws Exception {
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private static void a(Class paramClass, String paramString, int paramInt) {
		try {
			((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
			((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
			((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
}
