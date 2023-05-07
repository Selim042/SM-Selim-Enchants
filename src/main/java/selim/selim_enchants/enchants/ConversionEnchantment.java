package selim.selim_enchants.enchants;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;

public class ConversionEnchantment extends SelimEnchant {

	public ConversionEnchantment() {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 10 + (enchantmentLevel * 10);
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof ReflectionEnchantment);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.CONVERSION))
			return false;
		return stack.getItem() instanceof ShieldItem;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.CONVERSION))
			return false;
		return stack != null && stack.getItem() instanceof ShieldItem;
	}

	@Override
	public void doPostShielded(LivingEntity defender, DamageSource source, float blocked, int level) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.CONVERSION))
			return;
		ItemStack activeStack = defender.getUseItem();
		boolean shielding = activeStack != null && activeStack.getItem() instanceof ShieldItem;
		if (!shielding || level <= 0)
			return;
		float mult = Math.min(level / 6.f, 1) / 8;
		defender.heal(blocked * mult);
		defender.level.playLocalSound(defender.xo, defender.yo, defender.zo, SoundEvents.ENCHANTMENT_TABLE_USE,
				SoundSource.PLAYERS, 0.75f, 1.0f, true);
	}

}
