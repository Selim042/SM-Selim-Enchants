package selim.selim_enchants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import selim.selim_enchants.enchants.EnderShiftEnchantment.EnderShiftLootModifier;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = SelimEnchants.MOD_ID)
public class SelimEnchants {

	public static final String MOD_ID = "selim_enchants";
	public static final String NAME = "Selim's Enchants";
	public static final String VERSION = "3.0.1";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public SelimEnchants() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModRegistry.Blocks.BLOCKS.register(modEventBus);
		ModRegistry.Enchantments.ENCHANTMENTS.register(modEventBus);
		GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModLoadingContext.get().getActiveContainer().addConfig(new EnchantConfig(ModConfig.Type.COMMON,
				EnchantConfig.CONFIG, ModLoadingContext.get().getActiveContainer(), MOD_ID + "-common"));
	}

	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister
			.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);
	private static final RegistryObject<Codec<EnderShiftLootModifier>> ENDER_SHIFT = GLM.register("ender_shift",
			EnderShiftLootModifier.CODEC);

	@SubscribeEvent
	public static void runData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new DataProvider(event.getGenerator(), MOD_ID));
	}

	private static class DataProvider extends GlobalLootModifierProvider {
		public DataProvider(DataGenerator gen, String modid) {
			super(gen, modid);
		}

		@Override
		protected void start() {
			add("ender_shift",
					new EnderShiftLootModifier(new LootItemCondition[] { MatchTool
							.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(
									ModRegistry.Enchantments.ENDER_SHIFT.get(), MinMaxBounds.Ints.atLeast(1))))
							.build() }));
		}
	}

}
