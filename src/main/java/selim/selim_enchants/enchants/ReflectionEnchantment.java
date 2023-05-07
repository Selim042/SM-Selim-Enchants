package selim.selim_enchants.enchants;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;

public class ReflectionEnchantment extends SelimEnchant {

	public ReflectionEnchantment() {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 12 + (enchantmentLevel * 10);
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof ConversionEnchantment);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.REFLECTION))
			return false;
		return stack.getItem() instanceof ShieldItem;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.REFLECTION))
			return false;
		return stack.getItem() instanceof ShieldItem;
	}

	@Override
	public void doPostShielded(LivingEntity defender, DamageSource source, float blocked, int level) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.REFLECTION))
			return;
		Entity damager = source.getEntity();
		if (damager instanceof LivingEntity) {
			LivingEntity damagerLiving = (LivingEntity) damager;
			ItemStack activeStack = defender.getMainHandItem();
			if (level <= 0)
				return;
			boolean shielding = activeStack != null && activeStack.getItem() instanceof ShieldItem;
			if (!shielding)
				return;
			damagerLiving.hurt(DamageSource.thorns(defender), blocked * ((float) level / 5));
			activeStack.hurtAndBreak((int) (blocked / (5 / (float) level)), defender,
					(p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			defender.level.playLocalSound(defender.xo, defender.yo, defender.zo, SoundEvents.THORNS_HIT,
					SoundSource.PLAYERS, 0.75f, 1.0f, true);
		}
	}

}
