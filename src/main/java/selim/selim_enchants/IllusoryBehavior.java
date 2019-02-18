package selim.selim_enchants;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import selim.selim_enchants.entities.EntityFlyingTool;

public class IllusoryBehavior implements IBehaviorDispenseItem {

	private static final BehaviorDefaultDispenseItem DEFAULT_DISPENSE = new BehaviorDefaultDispenseItem();

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		int illusoryLevel = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.ILLUSORY, stack);
		IBlockState state = source.getBlockState();
		if (illusoryLevel <= 0 || !(state.getBlock() instanceof BlockDispenser))
			return DEFAULT_DISPENSE.dispense(source, stack);
		EnumFacing facing = state.getValue(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		stack.attemptDamageItem(1, source.getWorld().rand, null);
		EntityFlyingTool tool = new EntityFlyingTool(source.getWorld(), stack, true, pos.getX() + 0.5f,
				pos.getY() + 0.2f, pos.getZ() + 0.5f);
		tool.rotationYaw = facing.getHorizontalAngle();
		source.getWorld().spawnEntity(tool);
		return stack;
	}

}
