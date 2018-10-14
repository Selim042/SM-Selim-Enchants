package selim.selim_enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;

public class EnchantmentSelim extends Enchantment implements ITooltipInfo {

	protected EnchantmentSelim(Rarity rarityIn, EnumEnchantmentType typeIn,
			EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	public final boolean isEnabled() {
		return EnchantConfig.isEnabled(this);
	}

	@Override
	public final String getTranslatedName(int level) {
		if (!isEnabled())
			return TextFormatting.DARK_RED + super.getTranslatedName(level);
		return super.getTranslatedName(level);
	}

}
