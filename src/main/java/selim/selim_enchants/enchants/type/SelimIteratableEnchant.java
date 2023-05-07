package selim.selim_enchants.enchants.type;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.RegistryObject;
import selim.selim_enchants.SelimEnchant;

public class SelimIteratableEnchant extends SelimEnchant {

	private final String iterateFlag;

	protected SelimIteratableEnchant(String iterateFlag, Rarity rarityIn, EnchantmentCategory typeIn,
			EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.iterateFlag = iterateFlag;
	}

	private String getIterateFlag() {
		return this.iterateFlag;
	}

	public static void setIterate(RegistryObject<SelimIteratableEnchant> ench, ItemStack stack,
			boolean isIterating) {
		setIterate(ench.get(), stack, isIterating);
	}

	public static void setIterate(SelimIteratableEnchant ench, ItemStack stack, boolean isIterating) {
		CompoundTag tag = stack.getOrCreateTag();
		String iterateFlag = ench.getIterateFlag();
		tag.putBoolean(iterateFlag, isIterating);
		stack.setTag(tag);
	}

	public static boolean isIterating(RegistryObject<SelimIteratableEnchant> ench, ItemStack stack) {
		return isIterating(ench.get(), stack);
	}

	public static boolean isIterating(SelimIteratableEnchant ench, ItemStack stack) {
		if (!stack.hasTag())
			return false;
		String iterateFlag = ench.getIterateFlag();
		return stack.getTag().getBoolean(iterateFlag);
	}

	public void setIterate(ItemStack stack, boolean isIterating) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean(iterateFlag, isIterating);
		stack.setTag(tag);
	}

	public boolean isIterating(ItemStack stack) {
		if (!stack.hasTag())
			return false;
		return stack.getTag().getBoolean(iterateFlag);
	}

}
