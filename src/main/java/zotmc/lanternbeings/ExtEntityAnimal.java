package zotmc.lanternbeings;

import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import zotmc.lanternbeings.api.LbUtils;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

public class ExtEntityAnimal {
	
	private static final String LB = "Lb";
	private static final AttributeModifier lanternEntitySpeedBoost = new AttributeModifier(
			UUID.fromString("2c2b0adc-1936-4940-93a3-748a16c3dd51"), "Lantern entity speed boost", 0.3, 1);
	private static final AttributeModifier lanternEntityAttackBonus = new AttributeModifier(
			UUID.fromString("7d81324a-4902-4edf-9908-c54a7d04755d"), "Lantern entity attack bonus", 2, 0);
	private static final AttributeModifier lanternEntityHealthDecline = new AttributeModifier(
			UUID.fromString("43a43a18-40ee-4780-bd3e-d1e7e86525dd"), "Lantern entity health decline", -0.2, 1);
	
	private final EntityAnimal animal;
	private final boolean isLanternEntity;
	private boolean isAggressive, lanternBoost;
	
	private EntityAIAttackOnCollide task;
	private SetMultimap<Integer, EntityAITarget> targetTasks;
	
	public ExtEntityAnimal(EntityAnimal animal) {
		this.animal = animal;
		isLanternEntity = LbUtils.isLanternEntity(animal);
	}
	
	protected boolean isBreedingItemExt(ItemStack item) {
		return animal instanceof EntityHorse ?
				item != null && item.getItem() == Items.golden_carrot : animal.isBreedingItem(item);
	}
	
	protected void setLanternBoost(boolean value) {
		if (value) {
			applyAttributeModifier(SharedMonsterAttributes.attackDamage, lanternEntityAttackBonus);
			applyAttributeModifier(SharedMonsterAttributes.movementSpeed, lanternEntitySpeedBoost);
			applyAttributeModifier(SharedMonsterAttributes.maxHealth, lanternEntityHealthDecline);
			
			lanternBoost = true;
		}
		else {
			removeAttributeModifier(SharedMonsterAttributes.attackDamage, lanternEntityAttackBonus);
			removeAttributeModifier(SharedMonsterAttributes.movementSpeed, lanternEntitySpeedBoost);
			removeAttributeModifier(SharedMonsterAttributes.maxHealth, lanternEntityHealthDecline);
			
			lanternBoost = false;
		}
	}
	
	protected void setIsAggressive(boolean value) {
		if (value != isAggressive) {
			if (value) {
				task = new EntityAIAttackOnCollide(animal, 1, true);
				animal.tasks.addTask(30, task);
				
				targetTasks = ImmutableSetMultimap.of(
						30, new EntityAIHurtByTarget(animal, false),
						31, new EntityAINearestAttackableTarget(animal, EntityPlayer.class, true)
				);
				for (Entry<Integer, EntityAITarget> entry : targetTasks.entries())
					animal.targetTasks.addTask(entry.getKey(), entry.getValue());
				
				isAggressive = true;
			}
			else {
				animal.tasks.removeTask(task);
				task = null;
				
				for (EntityAIBase ai : targetTasks.values())
					animal.targetTasks.removeTask(ai);
				targetTasks = null;
				
				isAggressive = false;
			}
		}
	}
	
	
	public void writeEntityToNBTExt(NBTTagCompound tags) {
		if (isLanternEntity && (isAggressive || lanternBoost)) {
			NBTTagCompound lb = tags.getCompoundTag(LB);
			if (isAggressive) lb.setBoolean("IsAggressive", true);
			if (lanternBoost) lb.setBoolean("LanternBoost", true);
			tags.setTag(LB, lb);
		}
	}
	
	public void readEntityFromNBTExt(NBTTagCompound tags) {
		if (isLanternEntity && tags.hasKey(LB)) {
			NBTTagCompound lb = tags.getCompoundTag(LB);
			if (lb.getBoolean("IsAggressive")) setIsAggressive(true);
			if (lb.getBoolean("LanternBoost")) setLanternBoost(true);
		}
	}
	
	public boolean interactExt(EntityPlayer player, boolean isSubType) {
		if (isLanternEntity && !animal.isChild()
				&& (isSubType || !(animal instanceof EntityHorse || animal instanceof EntityWolf))) {
			
			ItemStack item = player.inventory.getCurrentItem();
			
			if (item != null) {
				if (item.getItem() == LbImpls.lit_pumpkin && !hasLanternHead()) {
					setHeadAndDropPrevious(new ItemStack(LbImpls.lit_pumpkin));
					setLanternBoost(true);
					setIsAggressive(true);
					consumePlayerItem(player, item);
					
					return true;
				}
				
				if (isBreedingItemExt(item)) {
					if (isAggressive) {
						setIsAggressive(false);
						
						return false;
					}
					
					if (hasLanternHead()) {
						setLanternBoost(false);
						setHeadAndDropPrevious(null);
						consumePlayerItem(player, item);
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean attackEntityAsMobExt(Entity target) {
		return isLanternEntity && target.attackEntityFrom(DamageSource.causeMobDamage(animal),
				(float) getAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
	}
	
	public void onRandomSpawnExt() {
		if (isLanternEntity && !animal.isChild()) {
			Calendar calendar = animal.worldObj.getCurrentDate();
			
			if ((calendar.get(2) + 1 == 10 && calendar.get(5) >= 24
					|| calendar.get(3) + 1 == 11 && calendar.get(5) < 14)
					&& animal.worldObj.rand.nextFloat() < 7/8F) {
				
				setHeadAndDropPrevious(new ItemStack(LbImpls.lit_pumpkin));
				setLanternBoost(true);
				setIsAggressive(true);
			}
		}
	}
	
	public void onDeathExt() {
		if (isLanternEntity && lanternBoost && !animal.worldObj.isRemote) {
			Random rand = animal.worldObj.rand;
			int n = rand.nextInt(3) + 7;
			
			for (int i = 0; i < n; i++) {
				EntityBat bat = new EntityBat(animal.worldObj);
				
				bat.setPosition(
						animal.posX - rand.nextGaussian(),
						animal.posY - rand.nextGaussian(),
						animal.posZ - rand.nextGaussian()
				);
				
				if (!animal.worldObj.checkBlockCollision(bat.getEntityBoundingBox()))
					animal.worldObj.spawnEntityInWorld(bat);
			}
		}
	}
	
	
	private void consumePlayerItem(EntityPlayer player, ItemStack item) {
		if (!player.capabilities.isCreativeMode && --item.stackSize <= 0)
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
	}
	
	private boolean hasLanternHead() {
		ItemStack item = animal.getCurrentArmor(3);
		return item != null && item.getItem() == LbImpls.lit_pumpkin;
	}
	
	private void setHeadAndDropPrevious(ItemStack item) {
		ItemStack previous = animal.getCurrentArmor(3);
		animal.setCurrentItemOrArmor(4, item);
		
		if (previous != null && !animal.worldObj.isRemote)
			animal.worldObj.spawnEntityInWorld(new EntityItem(animal.worldObj,
					animal.posX, animal.posY + animal.getEyeHeight(), animal.posZ, previous));
	}
	
	private void applyAttributeModifier(IAttribute attribute, AttributeModifier modifier) {
		IAttributeInstance instance = getAttribute(attribute);
		if (!instance.func_180374_a(modifier)) instance.applyModifier(modifier);
	}
	
	private void removeAttributeModifier(IAttribute attribute, AttributeModifier modifier) {
		IAttributeInstance instance = getAttribute(attribute);
		if (instance.func_180374_a(modifier)) instance.removeModifier(modifier);
	}
	
	private IAttributeInstance getAttribute(IAttribute attribute) {
		IAttributeInstance ret = animal.getEntityAttribute(attribute);
		if (ret == null) {
			animal.getAttributeMap().registerAttribute(attribute);
			ret = animal.getEntityAttribute(attribute);
		}
		return ret;
	}
	
}
