package selim.selim_enchants.enchants;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.SelimEnchants;

//@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentFeller extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentFeller() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.DIGGER,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "feller");
		this.setRegistryName("feller");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					TextFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".feller.desc"));
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		if (!EnchantConfig.isEnabled(this))
			return -1;
		return super.getMaxEnchantability(enchantmentLevel);
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentFeller);
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemAxe;
	}

	// @SubscribeEvent
	// public static void onBlockBreak(BlockEvent.BreakEvent event) {
	// if (!Registry.Enchantments.FELLER.isEnabled())
	// return;
	// EntityPlayer player = event.getPlayer();
	// ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
	// World world = player.getEntityWorld();
	// BlockPos pos = event.getPos();
	// IBlockState blockState = world.getBlockState(pos);
	// if (itemStack != null && itemStack.isItemEnchanted() && !world.isRemote
	// && !player.isSneaking()) {
	// int enchLevel = EnchantmentHelper.getEnchantmentLevel(
	// Registry.Enchants.FELLER, itemStack);
	// if (enchLevel == 1) {
	// if (blockState.getBlock() instanceof BlockLog) {
	// int val = 1;
	// boolean foundLeaves = false;
	// boolean isCont = false;
	// boolean isTree = false;
	// while (true) {
	// BlockPos nextPos = pos.add(0, val, 0);
	// IBlockState nextBlockState = world.getBlockState(nextPos);
	// if (nextBlockState.getBlock() instanceof BlockLeaves) {
	// foundLeaves = true;
	// break;
	// } else {
	// if (!(blockState.getBlock().equals(nextBlockState.getBlock()))) {
	// isCont = true;
	// } else {
	// isCont = false;
	// break;
	// }
	// }
	// // if
	// // (nextBlockState.getBlock().isAir(nextBlockState,world,nextPos))
	// // {
	// // if (isCont && foundLeaves) {
	// // isTree = true;
	// // break;
	// // }
	// // }
	// val++;
	// }
	// for (int i = 0; i < val; i++) {
	// if (!player.isCreative())
	// // TODO: Replace null param
	// itemStack.attemptDamageItem(1, player.getRNG(), null);
	// world.destroyBlock(pos.add(0, i, 0), !player.isCreative());
	// }
	// System.out.println("");
	// System.out.println(foundLeaves);
	// System.out.println(isCont);
	// System.out.println(isTree);
	// System.out.println(val);
	// }
	// }
	// }
	// }

}
