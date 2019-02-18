package selim.selim_enchants.enchants;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
		this.name = SelimEnchants.MOD_ID + ":" + "conversion";
		this.setRegistryName("conversion");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					TextFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".conversion.desc"));
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
