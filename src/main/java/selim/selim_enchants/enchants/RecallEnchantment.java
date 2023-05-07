package selim.selim_enchants.enchants;

import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class RecallEnchantment extends SelimEnchant {

	public RecallEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BOW,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.RECALL))
			return false;
		return stack.getItem() instanceof BowItem ? true : super.canEnchant(stack);
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof EnderShiftEnchantment);
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 22 + (enchantmentLevel) * 10;
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
	public boolean isTreasureOnly() {
		return true;
	}

//	@Override
//	public void onEntityDamaged(LivingEntity user, Entity target, int level) {
//		// if (!user.isServerWorld() || !(target instanceof LivingEntity))
//		// return;
//		// LivingEntity living = (LivingEntity) target;
//		// System.out.println(living.getHealth() <= 0.0f);
//	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityDeathFirst(LivingDropsEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.RECALL))
			return;
		Entity killer = event.getSource().getEntity();
		if (!(killer instanceof LivingEntity) || killer.level.isClientSide || event.isCanceled())
			return;
		LivingEntity livingKiller = (LivingEntity) killer;
		int recallLevel = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.RECALL.get(),
				livingKiller.getItemInHand(InteractionHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityDeathLast(LivingDropsEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.RECALL))
			return;
		Entity killer = event.getSource().getEntity();
		if (!(killer instanceof LivingEntity) || killer.level.isClientSide || event.isCanceled())
			return;
		Entity killed = event.getEntity();
		LivingEntity livingKiller = (LivingEntity) killer;
		int recallLevel = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.RECALL.get(),
				livingKiller.getItemInHand(InteractionHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
		RandomSource rand = killed.level.random;
		for (ItemEntity stack : event.getDrops())
			if (rand.nextFloat() <= recallLevel / 3.0f)
				stack.setPos(killer.xo, killer.yo, killer.zo);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityDeathExp(LivingExperienceDropEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.RECALL))
			return;
		Player killer = event.getAttackingPlayer();
		if (killer == null || killer.level.isClientSide || event.isCanceled())
			return;
		Entity killed = event.getEntity();
		int recallLevel = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.RECALL.get(),
				killer.getItemInHand(InteractionHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
		RandomSource rand = killed.level.random;
		int invertedLevel = (ModRegistry.Enchantments.RECALL.get().getMaxLevel() + 1) - recallLevel;
		if (invertedLevel <= 0)
			invertedLevel = 1;
		int movedExp = (int) Math.min(event.getDroppedExperience(),
				((rand.nextFloat() / 20) - 0.025f) + (event.getDroppedExperience() / invertedLevel));
		event.setDroppedExperience(event.getDroppedExperience() - movedExp);
		killer.level.addFreshEntity(new ExperienceOrb(killer.level, killer.xo, killer.yo, killer.zo, movedExp));
	}

}
