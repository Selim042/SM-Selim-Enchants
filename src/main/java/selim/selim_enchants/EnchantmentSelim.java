package selim.selim_enchants;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EnchantmentSelim extends Enchantment implements ITooltipInfo {

	protected EnchantmentSelim(Rarity rarityIn, EnumEnchantmentType typeIn,
			EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	public final boolean isEnabled() {
		// TODO: fix configs
		// return EnchantConfig.isEnabled(this);
		return true;
	}

	// @Override
	// public final String getTranslatedName(int level) {
	// if (!isEnabled())
	// return TextFormatting.DARK_RED + super.getTranslatedName(level);
	// return super.getTranslatedName(level);
	// }

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(new TextComponentTranslation(SelimEnchants.MOD_ID + ":enchant_disabled")
					.applyTextStyle(TextFormatting.DARK_RED));
		else
			tooltip.add(new TextComponentTranslation(this.getDefaultTranslationKey() + ".desc"));
	}

	@Override
	public ITextComponent func_200305_d(int level) {
		if (!isEnabled())
			return super.func_200305_d(level).applyTextStyle(TextFormatting.DARK_RED);
		return super.func_200305_d(level);
	}

}
