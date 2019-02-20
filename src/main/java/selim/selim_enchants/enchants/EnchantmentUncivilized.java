package selim.selim_enchants.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityDrowned;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;

public class EnchantmentUncivilized extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentUncivilized() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName("uncivilized");
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 8;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 20;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentUncivilized) && !(ench instanceof EnchantmentDamage)
				&& !(ench instanceof EnchantmentBanishing && !(ench instanceof EnchantmentWarping));
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		if (!this.isEnabled())
			return;
		boolean isHumanish = false;
		if (target instanceof EntityVillager)
			isHumanish = true;
		else if (target instanceof EntityIronGolem)
			isHumanish = true;
		else if (target instanceof EntitySnowman)
			isHumanish = true;
		else if (target instanceof EntityWitch)
			isHumanish = true;
		else if (target instanceof EntityZombieVillager)
			isHumanish = true;
		else if (target instanceof EntityZombie)
			isHumanish = true;
		else if (target instanceof EntityPlayer)
			isHumanish = true;
		else if (target instanceof EntityPlayerMP)
			isHumanish = true;
		else if (target instanceof EntityEvoker)
			isHumanish = true;
		else if (target instanceof EntityVindicator)
			isHumanish = true;
		else if (target instanceof EntityVex)
			isHumanish = true;
		else if (target instanceof EntityIllusionIllager)
			isHumanish = true;
		else if (target instanceof EntityDrowned)
			isHumanish = true;
		if (!target.getEntityWorld().isRemote && isHumanish)
			target.attackEntityFrom(DamageSource.causeMobDamage(user), level * 2.5F);
	}

}
