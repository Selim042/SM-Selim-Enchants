package selim.selim_enchants.enchants.iteratable;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
public class FellerEnchantment extends SelimIteratableEnchant {

	private static final String ITERATE_FLAG = "selim_enchants:iterate_feller";

	public FellerEnchantment() {
		super(ITERATE_FLAG, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.FELLER))
			return false;
		return stack.getItem() instanceof AxeItem;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.FELLER))
			return false;
		return stack.getItem() instanceof AxeItem;
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event.getLevel().isClientSide() && EnchantConfig.isEnabled(ModRegistry.Enchantments.FELLER))
			return;
		Player player = event.getPlayer();
		if (player.isCrouching())
			return;
		ItemStack tool = player.getMainHandItem();
		if (isIterating(ModRegistry.Enchantments.FELLER, tool))
			return;
		BlockState state = event.getState();
		boolean isLog = state.getTags().anyMatch((tag) -> tag.location().toString().equals("minecraft:logs"));
		if (!isLog)
			return;
		int fellLevel = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.FELLER.get(), tool);
		if (fellLevel <= 0)
			return;
		runFeller(player, new LinkedList<>(), tool, event.getPos(), new LinkedList<>(), 0);
	}

	private static void runFeller(Player player, List<Integer> tasks, ItemStack stack, BlockPos pos,
			List<BlockPos> visited, int depth) {
		if (visited.size() > EnchantConfig.MAX_FELLER_BLOCKS.get() || visited.contains(pos) || player.isCrouching())
			return;
		visited.add(pos);
		Level level = player.level;
		BlockState state = level.getBlockState(pos);
		boolean isLog = state.getTags().anyMatch((tag) -> tag.location().toString().equals("minecraft:logs"));

		if (!isLog)
			return;
		ServerPlayer sPlayer = (ServerPlayer) player;
		ObjectHolder<Boolean> isBroken = new ObjectHolder<>(false);
		tasks.add(TickScheduler.scheduleTask(level, depth, () -> {
			if (isBroken.getValue())
				return;
			setIterate(ModRegistry.Enchantments.FELLER, stack, true);
			sPlayer.gameMode.destroyBlock(pos);
			setIterate(ModRegistry.Enchantments.FELLER, stack, false);
			stack.hurtAndBreak(0, player, (p) -> {
				p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
				isBroken.setValue(true);
				TickScheduler.cancelTasks(tasks);
			});
		}));
		for (BlockPos iPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 1, 1)))
			runFeller(player, tasks, stack, iPos.immutable(), visited, depth + 1);
	}

}
