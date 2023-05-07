package selim.selim_enchants.enchants.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.ForgeRegistries;
import selim.selim_enchants.enchants.type.SelimPotionEnchant;

public class VenomousEnchantment extends SelimPotionEnchant {

	public VenomousEnchantment() {
		super((u, t, l) -> new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("poison")),
				60, l - 1), Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON,
				new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

}
