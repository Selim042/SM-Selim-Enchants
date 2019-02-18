package selim.selim_enchants.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCooledMagma extends BlockMagma {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

	public BlockCooledMagma() {
		super(Block.Properties.create(Material.ROCK, MaterialColor.NETHERRACK).lightValue(3)
				.needsRandomTick().hardnessAndResistance(0.5F));
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
		this.setRegistryName("cooled_magma");
		// TODO: find setter for harvest tool and level
		// this.setHarvestLevel("pickaxe", 0);
	}

	protected void turnIntoLava(World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState());
		worldIn.notifyNeighborsOfStateChange(pos, Blocks.LAVA);
	}

	// TODO: find CreativeTabs replacement
	// @Override
	// public CreativeTabs getCreativeTabToDisplayOn() {
	// return null;
	// }

	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
		if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4))
			this.slightlyMelt(worldIn, pos, state, rand, true);
		else
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, rand.nextInt(20) + 20);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn,
			BlockPos neighbor) {
		if (blockIn == this) {
			int i = this.countNeighbors(worldIn, pos);
			if (i < 2)
				this.turnIntoLava(worldIn, pos);
		}
	}

	private int countNeighbors(World p_185680_1_, BlockPos p_185680_2_) {
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values()) {
			if (p_185680_1_.getBlockState(p_185680_2_.offset(enumfacing)).getBlock() == this) {
				++i;
				if (i >= 4)
					return i;
			}
		}

		return i;
	}

	protected void slightlyMelt(World world, BlockPos pos, IBlockState state, Random rand,
			boolean p_185681_5_) {
		int i = ((Integer) state.get(AGE)).intValue();

		if (i < 3) {
			world.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
			world.getPendingBlockTicks().scheduleTick(pos, this, rand.nextInt(20) + 20);
		} else {
			this.turnIntoLava(world, pos);

			if (p_185681_5_) {
				for (EnumFacing enumfacing : EnumFacing.values()) {
					BlockPos blockpos = pos.offset(enumfacing);
					IBlockState iblockstate = world.getBlockState(blockpos);
					if (iblockstate.getBlock() == this)
						this.slightlyMelt(world, blockpos, iblockstate, rand, false);
				}
			}
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, IBlockReader world,
			BlockPos pos, EntityPlayer player) {
		return ItemStack.EMPTY;
	}

}