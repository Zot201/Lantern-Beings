package zotmc.lanternbeings.data;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zotmc.lanternbeings.util.Utils;

import com.google.common.reflect.Invokable;

@SideOnly(Side.CLIENT)
public class ReflClientData {
	
	public static final Invokable<RendererLivingEntity, Boolean>
	ADD_LAYER = Utils.findMethod(RendererLivingEntity.class, "addLayer", "func_177094_a")
		.asInvokable(LayerRenderer.class)
		.returning(boolean.class);
	
	public static final Field
	MAIN_MODEL = Utils.findField(RendererLivingEntity.class, "mainModel", "field_77045_g"),
	OCELOT_HEAD = Utils.findField(ModelOcelot.class, "ocelotHead", "field_78156_g"),
	RABBIT_HEAD = Utils.findField(ModelRabbit.class, "rabbitHead", "field_178704_h"),
	HORSE_HEAD = Utils.findField(ModelHorse.class, "head", "field_110709_a");
	
}
