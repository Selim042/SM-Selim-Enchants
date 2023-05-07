package selim.selim_enchants.utils;

import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SelimEnchantUtils {

	public static void removeEnchant(Enchantment ench, ItemStack stack) {
		Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(stack);
		enchs.remove(ench);
		EnchantmentHelper.setEnchantments(enchs, stack);
	}

}
