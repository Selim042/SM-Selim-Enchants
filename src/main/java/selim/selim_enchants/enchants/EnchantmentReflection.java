package selim.selim_enchants.enchants;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentReflection extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentReflection() {
		super(Rarity.RARE, EnumEnchantmentType.ALL,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "reflection");
		this.setRegistryName("reflection");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					ChatFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else {
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":reflection_desc"));
			tooltip.add(I18n.format(SelimEnchants.MOD_ID + ":reflection_desc_1"));
		}
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
		return stack != null && stack.getItem() instanceof ItemShield;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemShield;
	}

	@SubscribeEvent
	public static void onDamaged(LivingAttackEvent event) {
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
			entity.world.playSound(entity.posX, entity.posY, entity.posZ,
					new SoundEvent(new ResourceLocation("minecraft", "enchant.thorns.hit")),
					SoundCategory.PLAYERS, 0.75f, 1.0f, true);
		}
	}

}
