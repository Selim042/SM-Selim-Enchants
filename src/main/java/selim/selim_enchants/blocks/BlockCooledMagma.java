package selim.selim_enchants.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import selim.selim_enchants.SelimEnchants;

public class BlockCooledMagma extends BlockMagma {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

	public BlockCooledMagma() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setRegistryName("cooled_magma");
		this.setUnlocalizedName(SelimEnchants.MOD_ID + ":" + "cooled_magma");
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(0.5F);
	}

	protected void turnIntoWater(World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState());
		worldIn.notifyNeighborsOfStateChange(pos, Blocks.LAVA, false);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(AGE)).intValue();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(MathHelper.clamp(meta, 0, 3)));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4))
			this.slightlyMelt(worldIn, pos, state, rand, true);
		else
			worldIn.scheduleUpdate(pos, this, rand.nextInt(20) + 20);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn,
			BlockPos neighbor) {
		if (blockIn == this) {
			int i = this.countNeighbors(worldIn, pos);
			if (i < 2)
				this.turnIntoWater(worldIn, pos);
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
		int i = ((Integer) state.getValue(AGE)).intValue();

		if (i < 3) {
			world.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
			world.scheduleUpdate(pos, this, rand.nextInt(20) + 20);
		} else {
			this.turnIntoWater(world, pos);

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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ItemStack.EMPTY;
	}

}