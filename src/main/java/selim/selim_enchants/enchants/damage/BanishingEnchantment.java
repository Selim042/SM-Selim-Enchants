package selim.selim_enchants.enchants.damage;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import selim.selim_enchants.enchants.type.SelimDamageEnchant;

public class BanishingEnchantment extends SelimDamageEnchant {

	public BanishingEnchantment() {
		super("selim_enchants:banished", 2.5f, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

}