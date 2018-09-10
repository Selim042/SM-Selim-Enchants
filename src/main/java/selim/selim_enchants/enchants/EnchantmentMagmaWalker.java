package selim.selim_enchants.enchants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentMagmaWalker extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentMagmaWalker() {
		super(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_FEET,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
		this.setName(SelimEnchants.MOD_ID + ":" + "magma_walker");
		this.setRegistryName("magma_walker");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					ChatFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else {
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":magma_walker_desc"));
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":magma_walker_desc_1"));
		}
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return enchantmentLevel * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		if (!EnchantConfig.isEnabled(this))
			return -1;
		return this.getMinEnchantability(enchantmentLevel) + 15;
	}

	private static void freezeNearby(EntityLivingBase living, World worldIn, BlockPos pos, int level) {
		if (living.onGround) {
			float radius = (float) Math.min(16, 2 + level);
			BlockPos.MutableBlockPos upperPos = new BlockPos.MutableBlockPos(0, 0, 0);

			for (BlockPos.MutableBlockPos lavaPos : BlockPos.getAllInBoxMutable(
					pos.add((double) (-radius), -1.0D, (double) (-radius)),
					pos.add((double) radius, -1.0D, (double) radius))) {
				if (lavaPos.distanceSqToCenter(living.posX, living.posY,
						living.posZ) <= (double) (radius * radius)) {
					upperPos.setPos(lavaPos.getX(), lavaPos.getY() + 1, lavaPos.getZ());
					IBlockState airState = worldIn.getBlockState(upperPos);
					if (airState.getMaterial() == Material.AIR) {
						IBlockState lavaState = worldIn.getBlockState(lavaPos);

						if (lavaState.getMaterial() == Material.LAVA
								&& (lavaState.getBlock() == Blocks.LAVA
										|| lavaState.getBlock() == Blocks.FLOWING_LAVA)
								&& lavaState.getValue(BlockLiquid.LEVEL).intValue() == 0
								&& worldIn.mayPlace(Registry.Blocks.COOLED_MAGMA, lavaPos, false,
										EnumFacing.DOWN, (Entity) null)) {
							worldIn.setBlockState(lavaPos,
									Registry.Blocks.COOLED_MAGMA.getDefaultState());
							worldIn.scheduleUpdate(lavaPos.toImmutable(), Registry.Blocks.COOLED_MAGMA,
									MathHelper.getInt(living.getRNG(), 60, 120));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != Enchantments.DEPTH_STRIDER
				&& ench != Enchantments.FROST_WALKER;
	}

	@SubscribeEvent
	public static void onWalk(LivingEvent.LivingUpdateEvent event) {
		if (!Registry.Enchantments.MAGMA_WALKER.isEnabled())
			return;
		EntityLivingBase entity = event.getEntityLiving();
		int enchLevel = EnchantmentHelper.getMaxEnchantmentLevel(Registry.Enchantments.MAGMA_WALKER,
				entity);
		if (enchLevel != 0 && !entity.getEntityWorld().isRemote && enchLevel > 0 && hasMoved(entity))
			freezeNearby(entity, entity.getEntityWorld(), new BlockPos(entity), enchLevel);
	}

	private static Map<Integer, Double> PREV_X = new HashMap<>();
	private static Map<Integer, Double> PREV_Z = new HashMap<>();

	private static boolean hasMoved(EntityLivingBase living) {
		int id = living.getEntityId();
		double currX = living.posX;
		double currZ = living.posZ;
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
