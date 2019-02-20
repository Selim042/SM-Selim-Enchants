package selim.selim_enchants.enchants;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentTilling extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentTilling() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setRegistryName("tilling");
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
		return this.getMinEnchantability(enchantmentLevel) + 20;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack != null && stack.getItem() instanceof ItemHoe;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack != null && stack.getItem() instanceof ItemHoe;
	}

	@SubscribeEvent
	public static void onHoeUse(UseHoeEvent event) {
		if (!Registry.Enchantments.TILLING.isEnabled())
			return;
		ItemUseContext ctx = event.getContext();
		ItemStack stack = ctx.getItem();
		EntityPlayer player = event.getEntityPlayer();
		World world = ctx.getWorld();
		IBlockState hoeState = world.getBlockState(ctx.getPos());
		Block hoeBlock = hoeState.getBlock();
		boolean hoed = false;
		if (hoeBlock == Blocks.GRASS_PATH || hoeBlock == Blocks.GRASS)
			hoed = true;
		else if (hoeBlock == Blocks.COARSE_DIRT || hoeBlock == Blocks.DIRT)
			hoed = true;
		int level = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.TILLING, stack);
		if (hoed && !player.isSneaking() && level != 0) {
			for (BlockPos pos : BlockPos.getAllInBox(ctx.getPos().subtract(new Vec3i(level, 1, level)),
					ctx.getPos().add(level, 1, level))) {
				if (pos.equals(ctx.getPos()) || pos.distanceSq(ctx.getPos()) > Math.pow(level, 2) + 1
						|| !world.isAirBlock(pos.up()))
					continue;
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				boolean canPlace = false;
				boolean placeDirt = false;
				if (block == Blocks.GRASS_PATH || block == Blocks.GRASS)
					canPlace = true;
				else if (block == Blocks.COARSE_DIRT || block == Blocks.DIRT) {
					canPlace = true;
					placeDirt = true;
				}
				if (canPlace) {
					world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 0.25F,
							1.0F);
					if (!world.isRemote) {
						world.setBlockState(pos, placeDirt ? Blocks.DIRT.getDefaultState()
								: Blocks.FARMLAND.getDefaultState(), 11);
						if (world.rand.nextBoolean())
							if (player instanceof EntityPlayerMP)
								stack.attemptDamageItem(1, world.rand, (EntityPlayerMP) player);
							else
								stack.attemptDamageItem(1, world.rand, null);
					}
				}
			}
		}
	}

}
