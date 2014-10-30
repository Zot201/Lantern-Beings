package zotmc.lanternbeings.api;

import java.util.Set;

import net.minecraft.entity.passive.EntityAnimal;
import zotmc.lanternbeings.util.Utils;

public class LbRegistry {
	
	static final Set<Class<? extends EntityAnimal>> lanternEntities = Utils.newIdentityHashSet();
	
	public static void registerLanternEntity(Class<? extends EntityAnimal> clz) {
		lanternEntities.add(clz);
	}
	
}
