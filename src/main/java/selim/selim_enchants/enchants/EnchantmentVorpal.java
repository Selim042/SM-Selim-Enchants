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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.EnchantmentSelim;
import selim.selim_enchants.ITooltipInfo;
import selim.selim_enchants.Registry;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class EnchantmentVorpal extends EnchantmentSelim implements ITooltipInfo {

	public EnchantmentVorpal() {
		super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName(SelimEnchants.MOD_ID + ":" + "vorpal");
		this.setRegistryName("vorpal");
	}

	@SideOnly(Side.CLIENT)
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
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		if (!EnchantConfig.isEnabled(this))
			return -1;
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

	@ObjectHolder("minecraft:skull")
	private static Item SKULL;

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
		switch (vorpal) {
		case 0:
			return 1f - (looting * 0.1f);
		case 1:
			if (looting == 0)
				return 0.99f;
			return 0.99f * ((4 - looting) / 4f);
		case 2:
			if (looting == 0)
				return 0.66f;
			return 0.66f * ((4 - looting) / 4f);
		case 3:
			if (looting == 0)
				return 0.33f;
			return 0.33f * ((4 - looting) / 4f);
		default:
			return 0f;
		}
	}

	private static ItemStack getSkull(Entity entity, int vorpal, int looting) {
		if (!entity.getEntityWorld().isRemote
				&& entity.world.rand.nextFloat() >= getRate(vorpal, looting)) {
			if (entity instanceof EntitySkeleton)
				return new ItemStack(SKULL);
			else if (entity instanceof EntityWitherSkeleton)
				return new ItemStack(SKULL, 1, 1);
			else if (entity instanceof EntityZombie)
				return new ItemStack(SKULL, 1, 2);
			else if (entity instanceof EntityPlayer) {
				ItemStack itemStack = new ItemStack(SKULL, 1, 3);
				itemStack.setTagCompound(new NBTTagCompound());
				itemStack.getTagCompound().setString("SkullOwner", entity.getName());
				return itemStack;
			} else if (entity instanceof EntityCreeper)
				return new ItemStack(SKULL, 1, 4);
		}
		return ItemStack.EMPTY;
	}

}
