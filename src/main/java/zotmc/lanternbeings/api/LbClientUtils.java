package zotmc.lanternbeings.api;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zotmc.lanternbeings.LanternBeings;
import zotmc.lanternbeings.data.ReflClientData;
import zotmc.lanternbeings.render.LayerAnimalHead;
import zotmc.lanternbeings.render.LayerChickenHead;
import zotmc.lanternbeings.render.LayerHorseHead;
import zotmc.lanternbeings.util.Fields;
import zotmc.lanternbeings.util.Utils;

@SideOnly(Side.CLIENT)
public class LbClientUtils {
	
	public static void addQuadrupedHead(Class<? extends Entity> entity, float z) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelQuadruped)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerAnimalHead(((ModelQuadruped) mb).head, z)
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelQuadruped: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
	public static void addWolfHead(Class<? extends Entity> entity, float z) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelWolf)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerAnimalHead(((ModelWolf) mb).wolfHeadMain, z)
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelWolf: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
	public static void addChickenHead(Class<? extends Entity> entity, float y, float z) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelChicken)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerChickenHead(((ModelChicken) mb).body, y, z)
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelChicken: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
	public static void addOcelotHead(Class<? extends Entity> entity, float z) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelOcelot)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerAnimalHead((ModelRenderer) ReflClientData.OCELOT_HEAD.get(mb), z)
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelOcelot: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
	public static void addRabbitHead(Class<? extends Entity> entity, float z) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelRabbit)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerAnimalHead((ModelRenderer) ReflClientData.RABBIT_HEAD.get(mb), z)
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelRabbit: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
	public static void addHorseHead(Class<? extends Entity> entity) {
		Render r = Utils.RenderManagers.entityRenderMap().get(entity);
		
		if (r instanceof RendererLivingEntity) {
			RendererLivingEntity rle = (RendererLivingEntity) r;
			ModelBase mb = Fields.get(rle, ReflClientData.MAIN_MODEL);
			
			if (mb instanceof ModelHorse)
				try {
					ReflClientData.ADD_LAYER.invoke(
							rle,
							new LayerHorseHead((ModelRenderer) ReflClientData.HORSE_HEAD.get(mb))
					);
					
				} catch (Throwable e) {
					Utils.propagate(e);
				}
			else LanternBeings.INSTANCE.log.error("Main model not a ModelHorse: %s %s", entity, mb);
		}
		else LanternBeings.INSTANCE.log.error("Render not a RendererLivingEntity: %s %s", entity, r);
	}
	
}
