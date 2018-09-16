package selim.selim_enchants.enchants;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.SelimEnchants;

public class EnchantmentVenomous extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentVenomous() {
		super(Rarity.RARE, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "venom");
		this.setRegistryName("venom");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					ChatFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":venom_desc"));
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 19 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		if (!EnchantConfig.isEnabled(this))
			return -1;
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
