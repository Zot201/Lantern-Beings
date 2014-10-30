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
public class LayerChickenHead extends LayerAnimalHead {

	private final float y;

	public LayerChickenHead(ModelRenderer head, float y, float z) {
		super(head, z);
		this.y = y;
	}

	@Override protected void renderItemBlock(boolean flag, EntityLivingBase entity, ItemStack itemstack) {
		GlStateManager.rotate(-90, 1, 0, 0);
		GlStateManager.translate(0, y, z);

		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.625F, -0.625F, -0.625F);

		if (flag)
		{
			GlStateManager.translate(0.0F, 0.1875F, 0.0F);
		}

		Minecraft.getMinecraft().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.HEAD);
	}

}
