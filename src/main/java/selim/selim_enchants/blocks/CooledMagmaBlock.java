package selim.selim_enchants.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class CooledMagmaBlock extends MagmaBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	public CooledMagmaBlock() {
		super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops()
				.lightLevel((p_152684_) -> {
					return 3;
				}).randomTicks().strength(0.5F).isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> {
					return p_187424_.fireImmune();
				}).hasPostProcess((p1, p2, p3) -> true).emissiveRendering((p1, p2, p3) -> true));
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
	}

	protected void turnIntoLava(BlockState pState, Level pLevel, BlockPos pPos) {
		pLevel.setBlockAndUpdate(pPos, Blocks.LAVA.defaultBlockState());
		pLevel.neighborChanged(pPos, Blocks.LAVA, pPos);
	}

	@Override
	public void fillItemCategory(CreativeModeTab pTab, NonNullList<ItemStack> pItems) {
	}

	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if ((pRandom.nextInt(3) == 0 || this.fewerNeigboursThan(pLevel, pPos, 4))
				&& this.slightlyMelt(pState, pLevel, pPos)) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (Direction direction : Direction.values()) {
				blockpos$mutableblockpos.setWithOffset(pPos, direction);
				BlockState blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
				if (blockstate.is(this) && !this.slightlyMelt(blockstate, pLevel, blockpos$mutableblockpos)) {
					pLevel.scheduleTick(blockpos$mutableblockpos, this, Mth.nextInt(pRandom, 20, 40));
				}
			}

		} else {
			pLevel.scheduleTick(pPos, this, Mth.nextInt(pRandom, 20, 40));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos,
			boolean pIsMoving) {
		if (pBlock.defaultBlockState().is(this) && this.fewerNeigboursThan(pLevel, pPos, 2))
			this.turnIntoLava(pState, pLevel, pPos);

		super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
	}

	private boolean fewerNeigboursThan(BlockGetter pLevel, BlockPos pPos, int pNeighborsRequired) {
		int i = 0;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (Direction direction : Direction.values()) {
			blockpos$mutableblockpos.setWithOffset(pPos, direction);
			if (pLevel.getBlockState(blockpos$mutableblockpos).is(this)) {
				++i;
				if (i >= pNeighborsRequired) {
					return false;
				}
			}
		}

		return true;
	}

	protected boolean slightlyMelt(BlockState state, Level world, BlockPos pos) {
		int i = state.getValue(AGE);
		if (i < 3) {
			world.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
			return false;
		} else {
			this.turnIntoLava(state, world, pos);
			return true;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(AGE);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
		return ItemStack.EMPTY;
	}

}