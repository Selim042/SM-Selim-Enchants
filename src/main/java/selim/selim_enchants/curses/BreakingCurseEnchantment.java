package selim.selim_enchants.curses;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class BreakingCurseEnchantment extends SelimEnchant {

	public BreakingCurseEnchantment() {
		super(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.BREAKING_CURSE))
			return;
		damageItemHand(event.getPlayer(), event.getPlayer().getMainHandItem(), InteractionHand.MAIN_HAND);
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.BREAKING_CURSE))
			return;
		Entity attacker = event.getSource().getEntity();
		if (attacker instanceof LivingEntity) {
			LivingEntity livingAttacker = (LivingEntity) attacker;
			damageItemHand(livingAttacker, livingAttacker.getMainHandItem(), InteractionHand.MAIN_HAND);
		}
		LivingEntity attacked = event.getEntity();
		if (attacked != null)
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = attacked.getItemBySlot(slot);
				damageItemArmor(attacked, stack, slot);
			}
	}

	private static void damageItemArmor(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		if (EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.BREAKING_CURSE.get(), stack) > 0)
			stack.hurtAndBreak(1, entity, (p) -> p.broadcastBreakEvent(slot));
	}

	private static void damageItemHand(LivingEntity entity, ItemStack stack, InteractionHand hand) {
		if (EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.BREAKING_CURSE.get(), stack) > 0)
			stack.hurtAndBreak(1, entity, (p) -> p.broadcastBreakEvent(hand));
	}

}
