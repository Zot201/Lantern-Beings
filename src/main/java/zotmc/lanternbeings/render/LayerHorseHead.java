package zotmc.lanternbeings.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHorseHead extends LayerAnimalHead {

	public LayerHorseHead(ModelRenderer head) {
		super(head, 0);
	}

	@Override protected void renderItemBlock(boolean flag, EntityLivingBase entity, ItemStack itemstack) {
		GlStateManager.rotate(-6, 1, 0, 0);
		GlStateManager.rotate(-12, 0, 0, 1);
		GlStateManager.translate(0, -3/4F, -1/16F);

		GlStateManager.rotate(192.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.625F, -0.625F, -0.625F);

		if (flag)
		{
			GlStateManager.translate(0.0F, 0.1875F, 0.0F);
		}

		Minecraft.getMinecraft().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.HEAD);
	}

}
