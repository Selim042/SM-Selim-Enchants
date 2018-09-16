package selim.selim_enchants;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import selim.selim_enchants.blocks.BlockCooledMagma;
import selim.selim_enchants.curses.EnchantmentCurseBreaking;
import selim.selim_enchants.enchants.EnchantmentAmplify;
import selim.selim_enchants.enchants.EnchantmentBanishing;
import selim.selim_enchants.enchants.EnchantmentConversion;
import selim.selim_enchants.enchants.EnchantmentMagmaWalker;
import selim.selim_enchants.enchants.EnchantmentRecall;
import selim.selim_enchants.enchants.EnchantmentReflection;
import selim.selim_enchants.enchants.EnchantmentTilling;
import selim.selim_enchants.enchants.EnchantmentUncivilized;
import selim.selim_enchants.enchants.EnchantmentVenomous;
import selim.selim_enchants.enchants.EnchantmentVorpal;
import selim.selim_enchants.enchants.EnchantmentWarping;
import selim.selim_enchants.enchants.EnchantmentWither;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class Registry {

	@GameRegistry.ObjectHolder(SelimEnchants.MOD_ID)
	public static class Blocks {

		public static final Block COOLED_MAGMA = null;

	}

	@GameRegistry.ObjectHolder(SelimEnchants.MOD_ID)
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

		public static final EnchantmentSelim CURSE_BREAKING = null;

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockCooledMagma());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(new EnchantmentAmplify());
		event.getRegistry().register(new EnchantmentBanishing());
		// event.getRegistry().register(new EnchantmentFeller());
		event.getRegistry().register(new EnchantmentMagmaWalker());
		event.getRegistry().register(new EnchantmentUncivilized());
		event.getRegistry().register(new EnchantmentVorpal());
		event.getRegistry().register(new EnchantmentWarping());
		event.getRegistry().register(new EnchantmentWither());
		event.getRegistry().register(new EnchantmentRecall());
		event.getRegistry().register(new EnchantmentTilling());
		event.getRegistry().register(new EnchantmentVenomous());
		event.getRegistry().register(new EnchantmentConversion());
		event.getRegistry().register(new EnchantmentReflection());

		event.getRegistry().register(new EnchantmentCurseBreaking());
	}

}
