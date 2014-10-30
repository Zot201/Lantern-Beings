package zotmc.lanternbeings.api;

import net.minecraft.entity.passive.EntityAnimal;

public class LbUtils {
	
	public static boolean isLanternEntity(EntityAnimal entity) {
		return LbRegistry.lanternEntities.contains(entity.getClass());
	}

}
