package selim.selim_enchants;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import selim.selim_enchants.blocks.CooledMagmaBlock;
import selim.selim_enchants.curses.BreakingCurseEnchantment;
import selim.selim_enchants.enchants.ConversionEnchantment;
import selim.selim_enchants.enchants.EnderShiftEnchantment;
import selim.selim_enchants.enchants.MagmaWalkerEnchantment;
import selim.selim_enchants.enchants.RecallEnchantment;
import selim.selim_enchants.enchants.ReflectionEnchantment;
import selim.selim_enchants.enchants.VorpalEnchantment;
import selim.selim_enchants.enchants.damage.BanishingEnchantment;
import selim.selim_enchants.enchants.damage.UncivilizedEnchantment;
import selim.selim_enchants.enchants.damage.WarpedEnchantment;
import selim.selim_enchants.enchants.iteratable.AmplifyEnchantment;
import selim.selim_enchants.enchants.iteratable.FellerEnchantment;
import selim.selim_enchants.enchants.iteratable.TillingEnchantment;
import selim.selim_enchants.enchants.potion.VenomousEnchantment;
import selim.selim_enchants.enchants.potion.WitherEnchantment;
import selim.selim_enchants.enchants.type.SelimDamageEnchant;
import selim.selim_enchants.enchants.type.SelimIteratableEnchant;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class ModRegistry {

	public static class Blocks {

		static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
				SelimEnchants.MOD_ID);

		public static final RegistryObject<Block> COOLED_MAGMA = BLOCKS.register("cooled_magma",
				CooledMagmaBlock::new);;

	}

	public static class Enchantments {

		static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
				SelimEnchants.MOD_ID);

		public static final RegistryObject<SelimIteratableEnchant> AMPLIFY = ENCHANTMENTS.register("amplify",
				AmplifyEnchantment::new);
		public static final RegistryObject<SelimDamageEnchant> BANISHING = ENCHANTMENTS.register("banishing",
				BanishingEnchantment::new);
		public static final RegistryObject<SelimIteratableEnchant> FELLER = ENCHANTMENTS.register("feller",
				FellerEnchantment::new);
		public static final RegistryObject<SelimEnchant> MAGMA_WALKER = ENCHANTMENTS.register("magma_walker",
				MagmaWalkerEnchantment::new);
		public static final RegistryObject<SelimDamageEnchant> UNCIVILIZED = ENCHANTMENTS.register("uncivilized",
				UncivilizedEnchantment::new);
		public static final RegistryObject<SelimEnchant> VORPAL = ENCHANTMENTS.register("vorpal",
				VorpalEnchantment::new);
		public static final RegistryObject<SelimDamageEnchant> WARPING = ENCHANTMENTS.register("warped",
				WarpedEnchantment::new);
		public static final RegistryObject<SelimEnchant> WITHER = ENCHANTMENTS.register("wither",
				WitherEnchantment::new);
		public static final RegistryObject<SelimEnchant> RECALL = ENCHANTMENTS.register("recall",
				RecallEnchantment::new);
		public static final RegistryObject<SelimIteratableEnchant> TILLING = ENCHANTMENTS.register("tilling",
				TillingEnchantment::new);
		public static final RegistryObject<SelimEnchant> VENOMOUS = ENCHANTMENTS.register("venomous",
				VenomousEnchantment::new);
		public static final RegistryObject<SelimEnchant> CONVERSION = ENCHANTMENTS.register("conversion",
				ConversionEnchantment::new);
		public static final RegistryObject<SelimEnchant> REFLECTION = ENCHANTMENTS.register("reflection",
				ReflectionEnchantment::new);
		public static final RegistryObject<SelimEnchant> ENDER_SHIFT = ENCHANTMENTS.register("ender_shift",
				EnderShiftEnchantment::new);

		public static final RegistryObject<SelimEnchant> BREAKING_CURSE = ENCHANTMENTS.register("breaking_curse",
				BreakingCurseEnchantment::new);

	}

}
