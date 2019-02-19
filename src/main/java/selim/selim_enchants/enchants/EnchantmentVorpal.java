package selim.selim_enchants.enchants;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentVorpal extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentVorpal() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName("vorpal");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		if (!this.isEnabled())
			tooltip.add(
					TextFormatting.DARK_RED + I18n.format(SelimEnchants.MOD_ID + ":enchant_disabled"));
		else
			tooltip.add(I18n.format("enchantment." + SelimEnchants.MOD_ID + ".vorpal.desc"));
	}

	@Override
	public boolean canApply(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		if (!this.isEnabled())
			return false;
		return true;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + (enchantmentLevel) * 10;
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
		return !(ench instanceof EnchantmentVorpal);
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDropsEvent event) {
		if (!Registry.Enchantments.VORPAL.isEnabled())
			return;
		Entity killer = event.getSource().getTrueSource();
		if (!(killer instanceof EntityLivingBase))
			return;
		Entity killed = event.getEntity();
		EntityLivingBase livingKiller = (EntityLivingBase) killer;
		ItemStack skull = getSkull(killed,
				EnchantmentHelper.getEnchantmentLevel(Registry.Enchantments.VORPAL,
						livingKiller.getHeldItem(EnumHand.MAIN_HAND)),
				EnchantmentHelper.getLootingModifier(livingKiller));
		event.getDrops().add(new EntityItem(killed.world, killed.posX, killed.posY, killed.posZ, skull));
	}

	private static float getRate(int vorpal, int looting) {
		// TODO: fix configs
		// if (!EnchantConfig.LOOTING_ONLY_HEADS && vorpal == 0)
		// return 2f;
		return (1f - (0.2f * vorpal))
				* ((ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("looting")).getMaxLevel()
						+ 1 - looting) / 4f);
	}

	private static ItemStack getSkull(Entity entity, int vorpal, int looting) {
		if (!entity.getEntityWorld().isRemote
				&& entity.world.rand.nextFloat() >= getRate(vorpal, looting)) {
			if (entity instanceof EntitySkeleton)
				return new ItemStack(Items.SKELETON_SKULL);
			else if (entity instanceof EntityWitherSkeleton)
				return new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
			else if (entity instanceof EntityZombie)
				return new ItemStack(Items.ZOMBIE_HEAD, 1);
			else if (entity instanceof EntityPlayer) {
				ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD, 1);
				itemStack.setTag(new NBTTagCompound());
				itemStack.getTag().putString("SkullOwner", entity.getName().getString());
				return itemStack;
			} else if (entity instanceof EntityCreeper)
				return new ItemStack(Items.CREEPER_HEAD, 1);
		}
		return ItemStack.EMPTY;
	}

}
