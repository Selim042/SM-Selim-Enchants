package selim.selim_enchants.enchants.iteratable;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchants;
import selim.selim_enchants.TickScheduler;
import selim.selim_enchants.enchants.type.SelimIteratableEnchant;
import selim.selim_enchants.utils.ObjectHolder;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class AmplifyEnchantment extends SelimIteratableEnchant {

	private static final String ITERATE_FLAG = "selim_enchant:iterate_amplify";

	public AmplifyEnchantment() {
		super(ITERATE_FLAG, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.AMPLIFY))
			return false;
		if (stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof ShovelItem)
			return true;
		if (stack.getItem() instanceof AxeItem)
			return false;
		return super.canEnchant(stack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.AMPLIFY))
			return false;
		if (stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof ShovelItem)
			return true;
		if (stack.getItem() instanceof AxeItem)
			return false;
		return super.canApplyAtEnchantingTable(stack);
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

	private static BlockPos nextPos(BlockPos oldPos, int x, int y, int side) {
		switch (side) {
		case 0:
			return oldPos.offset(x, y, 0);
		case 1:
			return oldPos.offset(0, x, y);
		case 2:
			return oldPos.offset(x, y, 0);
		case 3:
			return oldPos.offset(0, x, y);
		default:
			return oldPos;
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.AMPLIFY))
			return;
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		Level world = player.getLevel();
		if (world.isClientSide)
			return;
		if (itemStack != null && itemStack.isEnchanted() && !world.isClientSide && !player.isCrouching()
				&& EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.AMPLIFY.get(), itemStack) != 0) {
			if (isIterating(ModRegistry.Enchantments.AMPLIFY.get(), itemStack))
				return;
			int enchLevel = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.AMPLIFY.get(), itemStack);
			boolean isSpade = itemStack.getItem() instanceof ShovelItem;
			BlockPos origPos = event.getPos();
			BlockPos pos = origPos;

			// Shift the center pos down to eye level
			if (enchLevel > 1)
				pos = pos.relative(Direction.UP, enchLevel - 1);

			int efficiency = EnchantmentHelper.getBlockEfficiency(player) + 1;
			ServerPlayer sPlayer = (ServerPlayer) player;
			List<Integer> tasks = new LinkedList<>();
			ObjectHolder<Boolean> isBroken = new ObjectHolder<>(false);
			for (int x = enchLevel * -1; x <= enchLevel; x++) {
				for (int y = enchLevel * -1; y <= enchLevel; y++) {
					BlockPos iPos = nextPos(pos, x, y,
							(int) Math.floor((double) (player.getRotationVector().y * 4.0F / 360.0F) + 0.5D) & 3);
					BlockState iState = world.getBlockState(iPos);
					if (iState.getDestroySpeed(world, iPos) >= 0 && (itemStack.isCorrectToolForDrops(iState)
							|| (isSpade && ForgeHooks.isCorrectToolForDrops(iState, player)))) {
						if (iPos.equals(origPos))
							continue;

						tasks.add(TickScheduler.scheduleTask(world, (2 * origPos.distManhattan(iPos)) / efficiency,
								() -> {
									if (isBroken.getValue())
										return;
									setIterate(ModRegistry.Enchantments.AMPLIFY, itemStack, true);
									sPlayer.gameMode.destroyBlock(iPos);
									setIterate(ModRegistry.Enchantments.AMPLIFY, itemStack, false);
									itemStack.hurtAndBreak(0, player, (p) -> {
										p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
										isBroken.setValue(true);
										TickScheduler.cancelTasks(tasks);
									});
								}));
					}
				}
			}
		}
	}

}
