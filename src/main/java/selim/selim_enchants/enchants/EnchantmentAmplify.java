package selim.selim_enchants.enchants;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentAmplify extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentAmplify() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.DIGGER,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName("amplify");
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack.getItem() instanceof ItemPickaxe || stack.getItem() instanceof ItemSpade;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		// TODO: fix configs
		// if (!EnchantConfig.isEnabled(this))
		// return (Integer.MAX_VALUE / 4) * 3;
		return 17 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentAmplify);
	}

	private static BlockPos nextPos(BlockPos oldPos, int x, int y, int side) {
		switch (side) {
		case 0:
			return oldPos.add(x, y, 0);
		case 1:
			return oldPos.add(0, x, y);
		case 2:
			return oldPos.add(x, y, 0);
		case 3:
			return oldPos.add(0, x, y);
		default:
			return oldPos;
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!Registry.Enchantments.AMPLIFY.isEnabled())
			return;
		EntityPlayer player = event.getPlayer();
		ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
		World world = player.getEntityWorld();
		if (itemStack != null && itemStack.isEnchanted() && !world.isRemote && !player.isSneaking()
				&& EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.AMPLIFY,
						itemStack) != 0) {
			int enchLevel = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.AMPLIFY,
					itemStack);
			boolean isSpade = itemStack.getItem() instanceof ItemSpade;
			BlockPos pos = event.getPos();

			// Shift the center pos down to eye level
			if (enchLevel > 1)
				pos = pos.offset(EnumFacing.UP, enchLevel - 1);

			for (int x = enchLevel * -1; x <= enchLevel; x++) {
				for (int y = enchLevel * -1; y <= enchLevel; y++) {
					BlockPos newPos = nextPos(pos, x, y,
							MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
					IBlockState blockState = world.getBlockState(newPos);
					if (blockState.getBlockHardness(world, newPos) >= 0
							&& (itemStack.canHarvestBlock(blockState) || (isSpade && blockState
									.getBlock().isToolEffective(blockState, ToolType.SHOVEL)))) {
						if (!player.isCreative())
							if (player instanceof EntityPlayerMP)
								itemStack.attemptDamageItem(1, player.getRNG(), (EntityPlayerMP) player);
							else
								itemStack.attemptDamageItem(1, player.getRNG(), null);

						// Destroy blocks properly so that the
						// game thinks the player actually did it
						IBlockState state = world.getBlockState(newPos);
						state.getBlock().removedByPlayer(state, world, newPos, player, true,
								state.getFluidState());
						state.getBlock().harvestBlock(world, player, newPos, state, null, itemStack);

						// world.destroyBlock(newPos, !player.isCreative());
					}
				}
			}
		}
	}

}
