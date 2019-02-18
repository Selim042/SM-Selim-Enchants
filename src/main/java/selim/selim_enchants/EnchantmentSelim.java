package selim.selim_enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

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
	public ITextComponent func_200305_d(int level) {
		if (!isEnabled())
			return new TextComponentString(
					TextFormatting.DARK_RED + super.func_200305_d(level).getFormattedText());
		return super.func_200305_d(level);
	}

}
