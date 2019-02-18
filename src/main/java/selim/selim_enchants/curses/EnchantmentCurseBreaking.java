package selim.selim_enchants.curses;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentCurseBreaking extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentCurseBreaking() {
		super(Rarity.COMMON, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.values());
		this.name = SelimEnchants.MOD_ID + ":curse_breaking";
		this.setRegistryName("curse_breaking");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					TextFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".curse_breaking.desc"));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMaxEnchantability(enchantmentLevel);
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
		if (!Registry.Enchantments.CURSE_BREAKING.isEnabled())
			return;
		damageItem(event.getPlayer(), event.getPlayer().getHeldItemMainhand());
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event) {
		if (!Registry.Enchantments.CURSE_BREAKING.isEnabled())
			return;
		Entity attacker = event.getSource().getTrueSource();
		if (attacker instanceof EntityLivingBase) {
			EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
			damageItem(livingAttacker, livingAttacker.getHeldItemMainhand());
		}
		EntityLivingBase attacked = event.getEntityLiving();
		if (attacked != null)
			for (ItemStack stack : attacked.getArmorInventoryList())
				damageItem(attacked, stack);
	}

	private static void damageItem(EntityLivingBase entity, ItemStack stack) {
		if (EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.CURSE_BREAKING, stack) > 0)
			stack.damageItem(1, entity);
	}

}
