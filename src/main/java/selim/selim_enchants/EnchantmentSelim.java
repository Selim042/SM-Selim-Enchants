package selim.selim_enchants;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

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
			return ChatFormatting.DARK_RED + super.getTranslatedName(level);
		return super.getTranslatedName(level);
	}

}
