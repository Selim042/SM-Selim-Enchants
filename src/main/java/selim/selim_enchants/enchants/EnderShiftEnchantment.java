package selim.selim_enchants.enchants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;
import selim.selim_enchants.SelimEnchants;
import selim.selim_enchants.utils.SelimEnchantUtils;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnderShiftEnchantment extends SelimEnchant {

	public EnderShiftEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 25 + (enchantmentLevel) * 10;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return ench != this && !(ench instanceof RecallEnchantment);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.ENDER_SHIFT))
			return false;
		return stack != null && (stack.getItem() instanceof TieredItem || stack.getItem() instanceof SwordItem
				|| stack.getItem() instanceof BowItem);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.ENDER_SHIFT))
			return;
		Player player = event.getPlayer();
		int level = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.ENDER_SHIFT.get(),
				player.getItemInHand(InteractionHand.MAIN_HAND));
		if (level <= 0)
			return;
		if (!event.isCanceled())
			handleDropStacks((Level) event.getLevel(), event.getPos(), player, event.getState());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onMobDeathFirst(LivingDropsEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.ENDER_SHIFT))
			return;
		Entity killer = event.getSource().getEntity();
		if (!(killer instanceof Player) || killer.level.isClientSide || event.isCanceled())
			return;
		Player livingKiller = (Player) killer;
		int level = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.ENDER_SHIFT.get(),
				livingKiller.getItemInHand(InteractionHand.MAIN_HAND));
		if (level <= 0)
			return;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMobDeathLast(LivingDropsEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.ENDER_SHIFT))
			return;
		Entity trueSource = event.getSource().getEntity();
		if (trueSource instanceof Player) {
			Player player = (Player) trueSource;
			int level = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.ENDER_SHIFT.get(),
					player.getItemInHand(InteractionHand.MAIN_HAND));
			if (level <= 0)
				return;
			event.setCanceled(handleDropEntities(event.getEntity().level, new BlockPos(event.getEntity().position()),
					player, event.getDrops()));
		}
	}

	private static boolean handleDropEntities(Level world, BlockPos dropPos, Player player,
			Collection<ItemEntity> dropEntities) {
		List<ItemStack> dropStacks = new ArrayList<>();
		for (ItemEntity i : dropEntities)
			dropStacks.add(i.getItem());
		return handleDropStacks(world, dropPos, player, dropStacks);
	}

	private static boolean handleDropStacks(Level world, BlockPos dropPos, Player player, BlockState state) {
		if (player == null)
			return false;
		BlockEntity blockentity = state.hasBlockEntity() ? world.getBlockEntity(dropPos) : null;

		ItemStack stackCopy = player.getMainHandItem().copy();
		SelimEnchantUtils.removeEnchant(ModRegistry.Enchantments.ENDER_SHIFT.get(), stackCopy);
		LootContext.Builder lootBuilder = (new LootContext.Builder((ServerLevel) world)).withRandom(world.random)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(dropPos))
				.withParameter(LootContextParams.TOOL, stackCopy)
				.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, player);
		List<ItemStack> drops = state.getDrops(lootBuilder);
		return handleDropStacks(world, dropPos, player, drops);
	}

	private static boolean handleDropStacks(Level world, BlockPos dropPos, Player player, List<ItemStack> drops) {
		int level = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.ENDER_SHIFT.get(),
				player.getItemInHand(InteractionHand.MAIN_HAND));
		if (level <= 0)
			return false;
		PlayerEnderChestContainer enderChest = player.getEnderChestInventory();
		List<ItemStack> toDrop = new ArrayList<>();
		for (ItemStack s : drops) {
			ItemStack leftover = enderChest.addItem(s);
			if (leftover != null && !leftover.isEmpty())
				toDrop.add(leftover);
		}
		for (ItemStack s : toDrop) {
			ItemEntity item = new ItemEntity(world, dropPos.getX() + 0.5f, dropPos.getY(), dropPos.getZ() + 0.5f, s);
			world.addFreshEntity(item);
		}
		return true;
	}

	public static class EnderShiftLootModifier extends LootModifier {

		public static final Supplier<Codec<EnderShiftLootModifier>> CODEC = Suppliers.memoize(
				() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, EnderShiftLootModifier::new)));

		public EnderShiftLootModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn);
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}

		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
				LootContext context) {
			return new ObjectArrayList<>();
		}

	}

}
