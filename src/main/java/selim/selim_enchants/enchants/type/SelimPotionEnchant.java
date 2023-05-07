package selim.selim_enchants.enchants.type;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;

public class SelimPotionEnchant extends SelimEnchant {

	private final MobEffectCreator effectCreator;

	protected SelimPotionEnchant(MobEffectCreator effectCreator, Rarity rarityIn, EnchantmentCategory typeIn,
			EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.effectCreator = effectCreator;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.VENOMOUS))
			return false;
		return stack.getItem() instanceof AxeItem ? true : super.canEnchant(stack);
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 17 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return this.getMinCost(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof SelimPotionEnchant);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doPostAttack(LivingEntity user, Entity target, int level) {
		if (!EnchantConfig.isEnabled(this))
			return;
		if (target instanceof LivingEntity)
			((LivingEntity) target).addEffect(effectCreator.createMobEffect(user, target, level));
		else if (target instanceof Player)
			((Player) target).addEffect(effectCreator.createMobEffect(user, target, level));
//		else if (target instanceof PlayerMP)
//			((PlayerMP) target).addEffect(new MobEffectInstance(
//					ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("wither")), 60, level));
		super.doPostAttack(user, target, level);
	}

	protected static interface MobEffectCreator {

		public MobEffectInstance createMobEffect(LivingEntity user, Entity target, int level);

	}

}
