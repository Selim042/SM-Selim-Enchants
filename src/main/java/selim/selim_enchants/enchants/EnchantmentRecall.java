package selim.selim_enchants.enchants;

import java.util.Collection;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentRecall extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentRecall() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.BOW,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		this.setRegistryName("recall");
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack.getItem() instanceof ItemBow;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 22 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentAmplify);
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityDeathFirst(LivingDropsEvent event) {
		if (!Registry.Enchantments.RECALL.isEnabled())
			return;
		Entity killer = event.getSource().getTrueSource();
		if (!(killer instanceof EntityLivingBase) || killer.world.isRemote || event.isCanceled())
			return;
		EntityLivingBase livingKiller = (EntityLivingBase) killer;
		int recallLevel = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.RECALL,
				livingKiller.getHeldItem(EnumHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
		if (!event.isCanceled())
			event.getEntity().captureDrops(event.getDrops());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityDeathLast(LivingDropsEvent event) {
		if (!Registry.Enchantments.RECALL.isEnabled())
			return;
		Entity killer = event.getSource().getTrueSource();
		if (!(killer instanceof EntityLivingBase) || killer.world.isRemote || event.isCanceled())
			return;
		Entity killed = event.getEntity();
		EntityLivingBase livingKiller = (EntityLivingBase) killer;
		int recallLevel = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.RECALL,
				livingKiller.getHeldItem(EnumHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
		Random rand = killed.world.rand;
		Collection<EntityItem> drops = killed.captureDrops();
		if (drops == null)
			return;
		for (EntityItem stack : drops)
			if (rand.nextFloat() <= recallLevel / 3.0f)
				stack.setLocationAndAngles(killer.posX, killer.posY, killer.posZ, stack.rotationYaw,
						stack.rotationPitch);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityDeathExp(LivingExperienceDropEvent event) {
		if (!Registry.Enchantments.RECALL.isEnabled())
			return;
		EntityPlayer killer = event.getAttackingPlayer();
		if (killer == null || killer.world.isRemote || event.isCanceled())
			return;
		Entity killed = event.getEntity();
		int recallLevel = EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.RECALL,
				killer.getHeldItem(EnumHand.MAIN_HAND));
		if (recallLevel <= 0)
			return;
		Random rand = killed.world.rand;
		int invertedLevel = (Registry.Enchantments.RECALL.getMaxLevel() + 1) - recallLevel;
		if (invertedLevel <= 0)
			invertedLevel = 1;
		int movedExp = (int) Math.min(event.getDroppedExperience(),
				((rand.nextFloat() / 20) - 0.025f) + (event.getDroppedExperience() / invertedLevel));
		event.setDroppedExperience(event.getDroppedExperience() - movedExp);
		killer.world.spawnEntity(
				new EntityXPOrb(killer.world, killer.posX, killer.posY, killer.posZ, movedExp));
	}

}
