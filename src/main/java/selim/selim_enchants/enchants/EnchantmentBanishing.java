package selim.selim_enchants.enchants;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.SelimEnchants;

public class EnchantmentBanishing extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentBanishing() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "banishing");
		this.setRegistryName("banishing");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					TextFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".banishing.desc"));
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
		if (!this.isEnabled() || target.getEntityWorld().isRemote)
			return;
		if (target instanceof EntityWither) {
			target.attackEntityFrom(DamageSource.causeMobDamage(user), level * 2.5f);
			return;
		}
		for (EnumCreatureType type : EnumCreatureType.values())
			for (Biome.SpawnListEntry e : Biome.REGISTRY.getObject(new ResourceLocation("hell"))
					.getSpawnableList(type))
				if (e.entityClass.equals(target.getClass()))
					target.attackEntityFrom(DamageSource.causeMobDamage(user), level * 2.5f);
	}

}