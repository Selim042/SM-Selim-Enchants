package selim.selim_enchants.enchants;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
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
public class EnchantmentTilling extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentTilling() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "tilling");
		this.setRegistryName("tilling");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					ChatFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":tilling_desc"));
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 8;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		if (!EnchantConfig.isEnabled(this))
			return -1;
		return this.getMinEnchantability(enchantmentLevel) + 20;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemHoe;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemHoe;
	}

	@SubscribeEvent
	public static void onHoeUse(UseHoeEvent event) {
		if (!Registry.Enchantments.TILLING.isEnabled())
			return;
		ItemStack stack = event.getCurrent();
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		IBlockState hoeState = world.getBlockState(event.getPos());
		Block hoeBlock = hoeState.getBlock();
		boolean hoed = false;
		if (hoeBlock == Blocks.GRASS_PATH || hoeBlock == Blocks.GRASS)
			hoed = true;
		else if (hoeBlock == Blocks.DIRT) {
			DirtType dirtType = hoeState.getValue(BlockDirt.VARIANT);
			if (dirtType == DirtType.COARSE_DIRT || dirtType == DirtType.DIRT)
				hoed = true;
		}
		int level = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.TILLING, stack);
		if (hoed && !player.isSneaking() && level != 0) {
			for (BlockPos pos : BlockPos.getAllInBox(event.getPos().subtract(new Vec3i(level, 1, level)),
					event.getPos().add(level, 1, level))) {
				if (pos.equals(event.getPos()) || pos.distanceSq(event.getPos()) > Math.pow(level, 2) + 1
						|| !world.isAirBlock(pos.up()))
					continue;
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				boolean canPlace = false;
				boolean placeDirt = false;
				if (block == Blocks.GRASS_PATH || block == Blocks.GRASS)
					canPlace = true;
				else if (block == Blocks.DIRT) {
					DirtType dirtType = state.getValue(BlockDirt.VARIANT);
					if (dirtType == DirtType.COARSE_DIRT || dirtType == DirtType.DIRT) {
						canPlace = true;
						placeDirt = dirtType == DirtType.COARSE_DIRT
								&& hoeState.getBlock() == Blocks.DIRT
								&& hoeState.getValue(BlockDirt.VARIANT) == DirtType.COARSE_DIRT;
					}
				}
				if (canPlace) {
					world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 0.25F,
							1.0F);
					if (!world.isRemote) {
						world.setBlockState(pos,
								placeDirt
										? Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT,
												BlockDirt.DirtType.DIRT)
										: Blocks.FARMLAND.getDefaultState(),
								11);
						// TODO: Fix null param
						if (world.rand.nextBoolean())
							stack.attemptDamageItem(1, world.rand, null);
					}
				}
			}
		}
	}

}
