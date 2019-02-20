package selim.selim_enchants;

// TODO: figured out how configs work now and uncomment all
public class EnchantConfig {

	// private static ForgeConfigSpec config;
	// private static boolean loaded = false;
	// private static boolean inited = false;
	//
	// private static final Map<EnchantmentSelim, Boolean> ENABLED = new
	// HashMap<>();
	// public static boolean LOOTING_ONLY_HEADS = true;
	//
	// protected static void load(File configFile) {
	// if (loaded)
	// return;
	// config = new ForgeConfigSpec(configFile, SelimEnchants.VERSION);
	// loaded = true;
	// }
	//
	// protected static void init() {
	// if (inited)
	// return;
	// try {
	// for (Field f : Registry.Enchantments.class.getDeclaredFields()) {
	// EnchantmentSelim ench = (EnchantmentSelim) f.get(null);
	// if (ench != null)
	// ENABLED.put(ench, config.getBoolean(
	// "enable_" + ench.getRegistryName().getPath(), "general", true, ""));
	// }
	// } catch (IllegalArgumentException | IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// LOOTING_ONLY_HEADS = config.getBoolean("looting_only_heads", "vorpal",
	// true,
	// "Allows mob heads to drop at a low rate when only looting is applied");
	// config.save();
	// inited = true;
	// }
	//
	// public static boolean isEnabled(EnchantmentSelim ench) {
	// if (!ENABLED.containsKey(ench))
	// return false;
	// return ENABLED.get(ench);
	// }

}
