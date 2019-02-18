package selim.selim_enchants;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import selim.selim_enchants.blocks.BlockCooledMagma;
import selim.selim_enchants.curses.EnchantmentCurseBreaking;
import selim.selim_enchants.enchants.EnchantmentAmplify;
import selim.selim_enchants.enchants.EnchantmentBanishing;
import selim.selim_enchants.enchants.EnchantmentConversion;
import selim.selim_enchants.enchants.EnchantmentEnderShift;
import selim.selim_enchants.enchants.EnchantmentIllusory;
import selim.selim_enchants.enchants.EnchantmentMagmaWalker;
import selim.selim_enchants.enchants.EnchantmentRecall;
import selim.selim_enchants.enchants.EnchantmentReflection;
import selim.selim_enchants.enchants.EnchantmentTilling;
import selim.selim_enchants.enchants.EnchantmentUncivilized;
import selim.selim_enchants.enchants.EnchantmentVenomous;
import selim.selim_enchants.enchants.EnchantmentVorpal;
import selim.selim_enchants.enchants.EnchantmentWarping;
import selim.selim_enchants.enchants.EnchantmentWither;
import selim.selim_enchants.entities.EntityFlyingTool;
import selim.selim_enchants.entities.FlyingToolRenderer;

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
		public static final EnchantmentSelim ENDER_SHIFT = null;
		public static final EnchantmentSelim ILLUSORY = null;

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
		registry.register(new EnchantmentIllusory());

		registry.register(new EnchantmentCurseBreaking());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registry.register(EntityEntryBuilder.create().id(new ResourceLocation("flying_tool"), 0)
				.entity(EntityFlyingTool.class).tracker(32, 2, true)
				.name(SelimEnchants.MOD_ID + ":flying_tool").build());
	}

}
