package selim.selim_enchants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SelimEnchants.MOD_ID, name = SelimEnchants.NAME, version = SelimEnchants.VERSION)
public class SelimEnchants {

	public static final String MOD_ID = "selim_enchants";
	public static final String NAME = "Selim's Enchants";
	public static final String VERSION = "2.0.1";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		EnchantConfig.load(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		EnchantConfig.init();
	}

}
