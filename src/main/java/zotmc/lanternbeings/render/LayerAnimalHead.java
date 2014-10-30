package zotmc.lanternbeings.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @see net.minecraft.client.renderer.entity.layers.LayerCustomHead
 */
@SideOnly(Side.CLIENT)
public class LayerAnimalHead implements LayerRenderer {

	private final ModelRenderer head;
	protected final float z;

	public LayerAnimalHead(ModelRenderer head, float z) {
		this.head = head;
		this.z = z;
	}

	protected void renderItemBlock(boolean flag, EntityLivingBase entity, ItemStack itemstack) {
		GlStateManager.translate(0, 0, z);

		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.625F, -0.625F, -0.625F);

		if (flag)
		{
			GlStateManager.translate(0.0F, 0.1875F, 0.0F);
		}

		Minecraft.getMinecraft().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.HEAD);
	}

	@Override public void doRenderLayer(EntityLivingBase entity, float p, float q, float r, float s,
			float t, float u, float v) {
		
		ItemStack itemstack = entity.getCurrentArmor(3);

		if (itemstack != null && itemstack.getItem() != null)
		{
			Item item = itemstack.getItem();
			GlStateManager.pushMatrix();

			if (entity.isSneaking())
			{
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			boolean flag = entity instanceof EntityVillager || entity instanceof EntityZombie && ((EntityZombie)entity).isVillager();
			float f7;

			if (!flag && entity.isChild())
			{
				f7 = 2.0F;
				float f8 = 1.4F;
				GlStateManager.scale(f8 / f7, f8 / f7, f8 / f7);
				GlStateManager.translate(0.0F, 16.0F * v, 0.0F);
			}

			head.postRender(0.0625F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			if (item instanceof ItemBlock) renderItemBlock(flag, entity, itemstack);

			GlStateManager.popMatrix();
		}
		
	}

	@Override public boolean shouldCombineTextures() {
		return true;
	}

}
