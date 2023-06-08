package selim.selim_enchants.enchants.type;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;

public class SelimDamageEnchant extends SelimEnchant {

	private final String tag;
	private final float damageMult;

	protected SelimDamageEnchant(String tag, float damageMult, Rarity rarityIn, EnchantmentCategory typeIn,
			EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.tag = tag;
		this.damageMult = damageMult;
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof SelimDamageEnchant) && !(ench instanceof DamageEnchantment);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 8;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return this.getMinCost(enchantmentLevel) + 20;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.BANISHING))
			return false;
		return stack.getItem() instanceof AxeItem ? true : super.canEnchant(stack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doPostAttack(LivingEntity user, Entity target, int level) {
		if (!EnchantConfig.isEnabled(this) || target.level.isClientSide)
			return;
		if (target.getType().getTags().anyMatch((t) -> t.location().toString().equals(this.tag))) {
			target.invulnerableTime = 0;
			target.hurt(DamageSource.mobAttack(user), level * this.damageMult);
			if (user instanceof Player)
				((Player) user).magicCrit(target);
		}
	}

}
