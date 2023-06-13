package selim.selim_enchants.enchants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import selim.selim_enchants.EnchantConfig;
import selim.selim_enchants.ModRegistry;
import selim.selim_enchants.SelimEnchant;
import selim.selim_enchants.SelimEnchants;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class VorpalEnchantment extends SelimEnchant {

	public VorpalEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.VORPAL))
			return false;
		return stack.getItem() instanceof AxeItem ? true : super.canEnchant(stack);
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 10 + (enchantmentLevel) * 10;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return this.getMinCost(enchantmentLevel) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDropsEvent event) {
		if (!EnchantConfig.isEnabled(ModRegistry.Enchantments.VORPAL))
			return;
		Entity killer = event.getSource().getEntity();
		if (!(killer instanceof LivingEntity))
			return;
		Entity killed = event.getEntity();
		LivingEntity livingKiller = (LivingEntity) killer;
		ItemStack skull = getSkull(killed,
				EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.Enchantments.VORPAL.get(),
						livingKiller.getItemInHand(InteractionHand.MAIN_HAND)),
				EnchantmentHelper.getMobLooting(livingKiller));
		event.getDrops().add(new ItemEntity(killed.level, killed.xo, killed.yo, killed.zo, skull));
	}

	private static float getRate(int vorpal, int looting) {
		if (EnchantConfig.HEADS_WITHOUT_VORPAL.get() && vorpal == 0)
			return 2f;
		return (1f - (0.2f * vorpal)) * ((Enchantments.MOB_LOOTING.getMaxLevel() + 1 - looting) / 4f);
	}

	private static ItemStack getSkull(Entity entity, int vorpal, int looting) {
		if (!entity.level.isClientSide && entity.level.random.nextFloat() >= getRate(vorpal, looting)) {
			if (entity instanceof Skeleton)
				return new ItemStack(Items.SKELETON_SKULL);
			else if (entity instanceof WitherSkeleton)
				return new ItemStack(Items.WITHER_SKELETON_SKULL);
			else if (entity instanceof Zombie)
				return new ItemStack(Items.ZOMBIE_HEAD);
			else if (entity instanceof Player) {
				ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
				itemStack.setTag(new CompoundTag());
				itemStack.getTag().putString("SkullOwner", entity.getName().getString());
				return itemStack;
			} else if (entity instanceof Creeper)
				return new ItemStack(Items.CREEPER_HEAD);
		}
		return ItemStack.EMPTY;
	}

}
