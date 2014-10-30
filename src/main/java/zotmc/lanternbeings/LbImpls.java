package zotmc.lanternbeings;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zotmc.lanternbeings.api.LbUtils;

public class LbImpls {
	
	static final Item lit_pumpkin = Item.getItemFromBlock(Blocks.lit_pumpkin); 
	
	@SideOnly(Side.CLIENT)
	public static boolean isEntityGlowing(EntityAnimal entity) {
		if (LbUtils.isLanternEntity(entity)) {
			ItemStack item = entity.getCurrentArmor(3);
			return item != null && item.getItem() == lit_pumpkin;
		}
		return false;
	}
	
}
