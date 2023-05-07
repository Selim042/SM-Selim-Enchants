package selim.selim_enchants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.RegistryObject;

public class EnchantConfig extends ModConfig {

	public static ForgeConfigSpec CONFIG;

	public static ForgeConfigSpec.BooleanValue VERBOSE;

	private static final Map<String, ForgeConfigSpec.BooleanValue> ENABLED = new HashMap<>();

	public static ForgeConfigSpec.BooleanValue HEADS_WITHOUT_VORPAL;
	public static ForgeConfigSpec.IntValue MAX_FELLER_BLOCKS;

	public EnchantConfig(ModConfig.Type type, IConfigSpec<?> spec, ModContainer container, String fileName) {
		super(type, spec, container, fileName + ".toml");
	}

	static {
		ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
		registerCommonConfig(commonBuilder);
		CONFIG = commonBuilder.build();
	}

	public static void registerCommonConfig(ForgeConfigSpec.Builder commonBuilder) {
		commonBuilder.comment("Configs for Selim Enchants").push("general");

		VERBOSE = commonBuilder.comment("Should only be set to true if requested to do so.\nThis may spam your logs.")
				.define("verbose", false);
		HEADS_WITHOUT_VORPAL = commonBuilder.comment("Should mobs drop heads without Vorpal?")
				.define("heads_without_vorpal", true);
		MAX_FELLER_BLOCKS = commonBuilder.comment("Maximum number of blocks that Feller can break at once")
				.defineInRange("max_feller_blocks", 1024, 32, 4096);

		commonBuilder.pop().comment("Enable or disable each enchant added").push("enchantment_disabling");
		try {
			for (Field f : ModRegistry.Enchantments.class.getDeclaredFields()) {
				Object value = f.get(null);
				if (RegistryObject.class.isInstance(value)) {
					@SuppressWarnings("unchecked")
					RegistryObject<SelimEnchant> regObject = ((RegistryObject<SelimEnchant>) value);
					String name = regObject.getId().getPath();
					ENABLED.put(name, commonBuilder.define("enable_" + name, true));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEnabled(RegistryObject<? extends SelimEnchant> ench) {
		if (ench.isPresent())
			return isEnabled(ench.getId());
		return false;
	}

	public static boolean isEnabled(ResourceLocation loc) {
		return isEnabled(loc.getPath());
	}

	private static boolean isEnabled(String name) {
		if (!ENABLED.containsKey(name))
			return false;
		return ENABLED.get(name).get();
	}

	@Deprecated
	public static boolean isEnabled(SelimEnchant ench) {
		return isEnabled(Registry.ENCHANTMENT.getKey(ench));
	}

	// @RequiresMcRestart
	// @Name("Enable Amplify")
	// @Comment("Set to true to disable the amplify enchantment.")
	// public static boolean ENABLE_AMPLIFY =
	// config.getBoolean("enable_amplify", "general", true,
	// "Set to true to disable the amplify enchantment.");
	//
	// @RequiresMcRestart
	// @Name("Enable Banishing")
	// @Comment("Set to true to disable the banishing enchantment.")
	// public static boolean ENABLE_BANISHING = true;
	//
	// // @RequiresMcRestart
	// // @Name("Enable Feller")
	// // @Comment("Set to true to disable the feller enchantment.")
	// // public static final boolean ENABLE_FELLER = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Magma Walker")
	// @Comment("Set to true to disable the magma walker enchantment.")
	// public static boolean ENABLE_MAGMA_WALKER = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Recall")
	// @Comment("Set to true to disable the recall enchantment.")
	// public static boolean ENABLE_RECALL = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Uncivilized")
	// @Comment("Set to true to disable the uncivilized enchantment.")
	// public static boolean ENABLE_UNCIVILIZED = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Vorpal")
	// @Comment("Set to true to disable the vorpal enchantment.")
	// public static boolean ENABLE_VORPAL = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Warping")
	// @Comment("Set to true to disable the warping enchantment.")
	// public static boolean ENABLE_WARPING = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Wither")
	// @Comment("Set to true to disable the wither enchantment.")
	// public static boolean ENABLE_WITHER = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Tilling")
	// @Comment("Set to true to disable the tilling enchantment.")
	// public static boolean ENABLE_TILLING = true;
	//
	// @RequiresMcRestart
	// @Name("Enable Curse of Breaking")
	// @Comment("Set to true to disable the curse of breaking.")
	// public static boolean ENABLE_CURSE_BREAKING = true;

}
