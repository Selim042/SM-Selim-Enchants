package selim.selim_enchants.enchants;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.SelimEnchants;

public class EnchantmentIllusory extends EnchantmentSelim {

	public EnchantmentIllusory() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "illusory");
		this.setRegistryName("illusory");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".illusory.desc"));
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 30 + (enchantmentLevel) * 10;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentBoomerang);
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack != null && (stack.getItem() instanceof ItemTool
				|| stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemShears);
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return false;
	}

}
