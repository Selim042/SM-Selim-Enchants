package selim.selim_enchants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

//@Config(modid = SelimEnchants.MOD_ID)
public class EnchantConfig {

	private static Configuration config;
	private static boolean loaded = false;
	private static boolean inited = false;

	private static final Map<EnchantmentSelim, Boolean> ENABLED = new HashMap<>();

	protected static void load(File configFile) {
		if (loaded)
			return;
		config = new Configuration(configFile, SelimEnchants.VERSION);
		loaded = true;
	}

	protected static void init() {
		if (inited)
			return;
		try {
			for (Field f : Registry.Enchantments.class.getDeclaredFields()) {
				EnchantmentSelim ench = (EnchantmentSelim) f.get(null);
				if (ench != null)
					ENABLED.put(ench, config.getBoolean(
							"enable_" + ench.getRegistryName().getResourcePath(), "general", true, ""));
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		config.save();
		inited = true;
	}

	public static boolean isEnabled(EnchantmentSelim ench) {
		if (!ENABLED.containsKey(ench))
			return false;
		return ENABLED.get(ench);
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
