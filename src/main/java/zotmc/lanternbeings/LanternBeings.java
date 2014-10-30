package zotmc.lanternbeings;

import static zotmc.lanternbeings.data.ModData.LanternBeings.MC_STRING;
import static zotmc.lanternbeings.data.ModData.LanternBeings.MODID;

import java.util.Set;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zotmc.lanternbeings.api.LbClientUtils;
import zotmc.lanternbeings.api.LbRegistry;
import zotmc.lanternbeings.data.ModData;
import zotmc.lanternbeings.loading.LbLoading;
import zotmc.lanternbeings.util.Utils;

@Mod(modid = MODID)
public enum LanternBeings {
	INSTANCE;
	
	public final Logger log = LogManager.getFormatterLogger(MODID);
	
	@Deprecated @InstanceFactory public static LanternBeings instance() { return INSTANCE; }
	
	@EventHandler public void onConstruct(FMLConstructionEvent event) {
		Set<ArtifactVersion> missing = Utils.checkRequirements(ModData.class, MC_STRING);
		if (!missing.isEmpty()) throw new MissingModsException(missing);
		
		Utils.construct(LbLoading.Post.class);
	}
	
	@EventHandler public void init(FMLInitializationEvent event) {
		LbRegistry.registerLanternEntity(EntityPig.class);
		LbRegistry.registerLanternEntity(EntitySheep.class);
		LbRegistry.registerLanternEntity(EntityCow.class);
		LbRegistry.registerLanternEntity(EntityMooshroom.class);
		LbRegistry.registerLanternEntity(EntityWolf.class);
		LbRegistry.registerLanternEntity(EntityChicken.class);
		LbRegistry.registerLanternEntity(EntityOcelot.class);
		LbRegistry.registerLanternEntity(EntityRabbit.class);
		LbRegistry.registerLanternEntity(EntityHorse.class);
		
		if (event.getSide().isClient()) clientInit();
	}
	
	@SideOnly(Side.CLIENT)
	private void clientInit() {
		LbClientUtils.addQuadrupedHead(EntityPig.class, -7/32F);
		LbClientUtils.addQuadrupedHead(EntitySheep.class, -1/8F);
		LbClientUtils.addQuadrupedHead(EntityCow.class, -1/4F);
		LbClientUtils.addQuadrupedHead(EntityMooshroom.class, -1/4F);
		LbClientUtils.addWolfHead(EntityWolf.class, -1/8F);
		LbClientUtils.addChickenHead(EntityChicken.class, -1/4F, -1/4F);
		LbClientUtils.addOcelotHead(EntityOcelot.class, 0);
		LbClientUtils.addRabbitHead(EntityRabbit.class, -1/8F);
		LbClientUtils.addHorseHead(EntityHorse.class);
	}
	
}
