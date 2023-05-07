package selim.selim_enchants.enchants.iteratable;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchants;
import selim.selim_enchants.TickScheduler;
import selim.selim_enchants.enchants.type.SelimIteratableEnchant;
import selim.selim_enchants.utils.MutableUseOnContext;
import selim.selim_enchants.utils.ObjectHolder;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class TillingEnchantment extends SelimIteratableEnchant {

	private static final String ITERATE_FLAG = "selim_enchant:iterate_tilling";

	public TillingEnchantment() {
		super(ITERATE_FLAG, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
	}

	@Override
	public int getMaxLevel() {
		return 2;
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
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.TILLING))
			return false;
		return stack.getItem() instanceof HoeItem;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.TILLING))
			return false;
		return stack.getItem() instanceof HoeItem;
	}

	@SubscribeEvent
	public static void onHoeUse(BlockEvent.BlockToolModificationEvent event) {
		if (!event.getToolAction().equals(ToolActions.HOE_TILL))
			return;
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.TILLING))
			return;
		ItemStack stack = event.getHeldItemStack();
		if (isIterating(ModRegistry.Enchantments.TILLING, stack))
			return;
		int level = EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.TILLING.get(), stack);
		Player player = event.getPlayer();
		if (level <= 0 || player.isCrouching())
			return;
		Level world = event.getPlayer().getLevel();
		BlockPos hoedPos = event.getPos();
		List<Integer> tasks = new LinkedList<>();
		ObjectHolder<Boolean> isBroken = new ObjectHolder<>(false);
		for (BlockPos iPos : BlockPos.betweenClosed(hoedPos.subtract(new Vec3i(level, 1, level)),
				hoedPos.offset(level, 1, level))) {
			if (iPos.equals(hoedPos) || !world.isEmptyBlock(iPos.above()))
				continue;

			// TODO: implement the timing thing
//			tasks.add(TickScheduler.scheduleTask(world, (2 * hoedPos.distManhattan(iPos)), () -> {
			if (isBroken.getValue())
				return;
			setIterate(ModRegistry.Enchantments.TILLING, stack, true);
			stack.useOn(new MutableUseOnContext(event.getContext()).setBlockPos(iPos));
			setIterate(ModRegistry.Enchantments.TILLING, stack, false);
			stack.hurtAndBreak(0, player, (p) -> {
				p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
				isBroken.setValue(true);
				TickScheduler.cancelTasks(tasks);
			});
//			}));
		}
	}

}
