package selim.selim_enchants.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentReflection extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentReflection() {
		super(Rarity.RARE, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setRegistryName("reflection");
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 12 + (enchantmentLevel * 10);
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentConversion);
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
		if (!Registry.Enchantments.REFLECTION.isEnabled())
			return;
		EntityLivingBase entity = event.getEntityLiving();
		Entity damager = event.getSource().getTrueSource();
		if (damager instanceof EntityLivingBase) {
			EntityLivingBase damagerLiving = (EntityLivingBase) damager;
			ItemStack activeStack = entity.getActiveItemStack();
			int level = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.REFLECTION,
					activeStack);
			if (level <= 0)
				return;
			boolean shielding = activeStack != null && activeStack.getItem() instanceof ItemShield;
			if (!shielding)
				return;
			float damage = event.getAmount();
			damagerLiving.attackEntityFrom(DamageSource.causeThornsDamage(entity),
					damage * ((float) level / 5));
			activeStack.damageItem((int) (damage / (5 / (float) level)), entity);
			entity.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENCHANT_THORNS_HIT,
					SoundCategory.PLAYERS, 0.75f, 1.0f, true);
		}
	}

}
