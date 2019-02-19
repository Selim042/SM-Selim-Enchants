package selim.selim_enchants;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import selim.selim_enchants.blocks.BlockCooledMagma;
import selim.selim_enchants.curses.EnchantmentCurseBreaking;
import selim.selim_enchants.enchants.EnchantmentAmplify;
import selim.selim_enchants.enchants.EnchantmentBanishing;
import selim.selim_enchants.enchants.EnchantmentConversion;
import selim.selim_enchants.enchants.EnchantmentEnderShift;
import selim.selim_enchants.enchants.EnchantmentMagmaWalker;
import selim.selim_enchants.enchants.EnchantmentRecall;
import selim.selim_enchants.enchants.EnchantmentReflection;
import selim.selim_enchants.enchants.EnchantmentTilling;
import selim.selim_enchants.enchants.EnchantmentUncivilized;
import selim.selim_enchants.enchants.EnchantmentVenomous;
import selim.selim_enchants.enchants.EnchantmentVorpal;
import selim.selim_enchants.enchants.EnchantmentWarping;
import selim.selim_enchants.enchants.EnchantmentWither;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

	@ObjectHolder(SelimEnchants.MOD_ID)
	public static class Blocks {

		public static final Block COOLED_MAGMA = null;

	}

	@ObjectHolder(SelimEnchants.MOD_ID)
	public static class Enchantments {

		public static final EnchantmentSelim AMPLIFY = null;
		public static final EnchantmentSelim BANISHING = null;
		// public static final EnchantmentSelim FELLER = null;
		public static final EnchantmentSelim MAGMA_WALKER = null;
		public static final EnchantmentSelim UNCIVILIZED = null;
		public static final EnchantmentSelim VORPAL = null;
		public static final EnchantmentSelim WARPING = null;
		public static final EnchantmentSelim WITHER = null;
		public static final EnchantmentSelim RECALL = null;
		public static final EnchantmentSelim TILLING = null;
		public static final EnchantmentSelim VENOM = null;
		public static final EnchantmentSelim CONVERSION = null;
		public static final EnchantmentSelim REFLECTION = null;
		public static final EnchantmentSelim ENDER_SHIFT = null;

		public static final EnchantmentSelim CURSE_BREAKING = null;

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockCooledMagma());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Enchantment> event) {
		IForgeRegistry<Enchantment> registry = event.getRegistry();
		registry.register(new EnchantmentAmplify());
		registry.register(new EnchantmentBanishing());
		// registry.register(new EnchantmentFeller());
		registry.register(new EnchantmentMagmaWalker());
		registry.register(new EnchantmentUncivilized());
		registry.register(new EnchantmentVorpal());
		registry.register(new EnchantmentWarping());
		registry.register(new EnchantmentWither());
		registry.register(new EnchantmentRecall());
		registry.register(new EnchantmentTilling());
		registry.register(new EnchantmentVenomous());
		registry.register(new EnchantmentConversion());
		registry.register(new EnchantmentReflection());
		registry.register(new EnchantmentEnderShift());

		registry.register(new EnchantmentCurseBreaking());
	}

}
