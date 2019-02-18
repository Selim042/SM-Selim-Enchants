package selim.selim_enchants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

// TODO: figure out how to set name and version now
//@Mod(modid = SelimEnchants.MOD_ID, name = SelimEnchants.NAME, version = SelimEnchants.VERSION)
@Mod(SelimEnchants.MOD_ID)
public class SelimEnchants {

	public static final String MOD_ID = "selim_enchants";
	public static final String NAME = "Selim's Enchants";
	public static final String VERSION = "2.1.0";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private void setup(final FMLCommonSetupEvent event) {
		// TODO: fix configs
		// EnchantConfig.load(event.getSuggestedConfigurationFile());
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// TODO: figured out how configs work now
		// EnchantConfig.init();
	}

	private void doClientStuff(final FMLDedicatedServerSetupEvent event) {
		// TODO: figured out how configs work now
		// EnchantConfig.init();
	}

}
