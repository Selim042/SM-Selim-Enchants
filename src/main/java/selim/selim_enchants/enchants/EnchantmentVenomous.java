package selim.selim_enchants.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;

public class EnchantmentVenomous extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentVenomous() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName("venom");
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 19 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentVenomous);
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		if (!this.isEnabled())
			return;
		if (target instanceof EntityLiving)
			((EntityLiving) target).addPotionEffect(new PotionEffect(
					ForgeRegistries.POTIONS.getValue(new ResourceLocation("poison")), 60, level));
		else if (target instanceof EntityPlayer)
			((EntityPlayer) target).addPotionEffect(new PotionEffect(
					ForgeRegistries.POTIONS.getValue(new ResourceLocation("poison")), 60, level));
		else if (target instanceof EntityPlayerMP)
			((EntityPlayerMP) target).addPotionEffect(new PotionEffect(
					ForgeRegistries.POTIONS.getValue(new ResourceLocation("poison")), 60, level));
		super.onEntityDamaged(user, target, level);
	}

}
