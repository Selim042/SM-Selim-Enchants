package selim.selim_enchants.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentConversion extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentConversion() {
		super(Rarity.RARE, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setRegistryName("conversion");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + (enchantmentLevel * 10);
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentReflection);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack != null && stack.getItem() instanceof ItemShield;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack != null && stack.getItem() instanceof ItemShield;
	}

	@SubscribeEvent
	public static void onDamaged(LivingAttackEvent event) {
		if (!Registry.Enchantments.CONVERSION.isEnabled())
			return;
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack activeStack = entity.getActiveItemStack();
		boolean shielding = activeStack != null && activeStack.getItem() instanceof ItemShield;
		int level = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.CONVERSION, activeStack);
		if (!shielding || level <= 0)
			return;
		float damage = event.getAmount();
		entity.heal(damage * ((float) level / 10));
		entity.world.playSound(entity.posX, entity.posY, entity.posZ,
				SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.75f, 1.0f, true);
	}

}
