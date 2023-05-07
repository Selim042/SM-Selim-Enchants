package selim.selim_enchants;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class SelimEnchant extends Enchantment implements ITooltipInfo {

	protected SelimEnchant(Enchantment.Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable(this.descriptionId + ".desc"));
	}

	@Deprecated
	public final boolean isEnabled() {
		return EnchantConfig.isEnabled(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(this))
			return false;
		return super.canEnchant(stack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!EnchantConfig.isEnabled(this))
			return false;
		return super.canApplyAtEnchantingTable(stack);
	}

	public void doPostShielded(LivingEntity defender, DamageSource source, float blocked, int level) {
	}

	@SubscribeEvent
	public static void onShielded(ShieldBlockEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getDamageSource();
		ItemStack stack = entity.getUseItem();
		for (Entry<Enchantment, Integer> e : EnchantmentHelper.getEnchantments(stack).entrySet()) {
			if (e.getKey() instanceof SelimEnchant)
				((SelimEnchant) e.getKey()).doPostShielded(entity, source, event.getOriginalBlockedDamage(),
						e.getValue());
		}
	}

//	@Override
//	public final Component getFullname(int level) {
//		if (!isEnabled())
//			return ChatFormatting.DARK_RED + super.getTranslatedName(level);
//		return super.getTranslatedName(level);
//	}

}
