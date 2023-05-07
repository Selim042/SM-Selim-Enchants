package selim.selim_enchants.enchants;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class MagmaWalkerEnchantment extends SelimEnchant {

	public MagmaWalkerEnchantment() {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return enchantmentLevel * 10;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return this.getMinCost(enchantmentLevel) + 15;
	}

	// from FrostWalker.onEntityMoved
	private static void freezeNearby(LivingEntity pLiving, Level pLevel, BlockPos pPos, int pLevelConflicting) {
		if (pLiving.isOnGround()) {
			BlockState blockstate = ModRegistry.Blocks.COOLED_MAGMA.get().defaultBlockState();
			float f = (float) Math.min(16, 2 + pLevelConflicting);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset((double) (-f), -1.0D, (double) (-f)),
					pPos.offset((double) f, -1.0D, (double) f))) {
				if (blockpos.closerToCenterThan(pLiving.position(), (double) f)) {
					blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
					BlockState blockstate1 = pLevel.getBlockState(blockpos$mutableblockpos);
					if (blockstate1.isAir()) {
						BlockState blockstate2 = pLevel.getBlockState(blockpos);
						boolean isFull = blockstate2.getBlock() == Blocks.LAVA
								&& blockstate2.getValue(LiquidBlock.LEVEL) == 0;
						if (blockstate2.getMaterial() == Material.LAVA && isFull
								&& blockstate.canSurvive(pLevel, blockpos)
								&& pLevel.isUnobstructed(blockstate, blockpos, CollisionContext.empty())
								&& !net.minecraftforge.event.ForgeEventFactory
										.onBlockPlace(
												pLiving, net.minecraftforge.common.util.BlockSnapshot
														.create(pLevel.dimension(), pLevel, blockpos),
												net.minecraft.core.Direction.UP)) {
							pLevel.setBlockAndUpdate(blockpos, blockstate);
							pLevel.scheduleTick(blockpos, ModRegistry.Blocks.COOLED_MAGMA.get(),
									Mth.nextInt(pLiving.getRandom(), 60, 120));
						}
					}
				}
			}

		}
	}

	@Override
	public boolean checkCompatibility(Enchantment ench) {
		return ench != this && ench != Enchantments.DEPTH_STRIDER && ench != Enchantments.FROST_WALKER;
	}

	@SubscribeEvent
	public static void onWalk(LivingEvent.LivingTickEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.MAGMA_WALKER))
			return;
		LivingEntity entity = event.getEntity();
		int enchLevel = EnchantmentHelper.getEnchantmentLevel(ModRegistry.Enchantments.MAGMA_WALKER.get(), entity);
		if (enchLevel != 0 && !entity.level.isClientSide && enchLevel > 0 && hasMoved(entity))
			freezeNearby(entity, entity.level, new BlockPos(entity.position()), enchLevel);
	}

	private static Map<Integer, Double> PREV_X = new HashMap<>();
	private static Map<Integer, Double> PREV_Z = new HashMap<>();

	private static boolean hasMoved(LivingEntity living) {
		int id = living.getId();
		double currX = living.xo;
		double currZ = living.zo;
		if (!PREV_X.containsKey(id) || !PREV_Z.containsKey(id)) {
			PREV_X.put(id, currX);
			PREV_Z.put(id, currZ);
			return false;
		}
		double prevX = PREV_X.get(id);
		double prevZ = PREV_Z.get(id);
		PREV_X.put(id, currX);
		PREV_Z.put(id, currZ);
		return Math.abs(currX - prevX) > 0.0001 || Math.abs(currZ - prevZ) > 0.0001;
	}

}
