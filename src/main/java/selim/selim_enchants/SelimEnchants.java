package selim.selim_enchants;

import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import selim.selim_enchants.entities.EntityFlyingTool;
import selim.selim_enchants.entities.FlyingToolRenderer;

@Mod(modid = SelimEnchants.MOD_ID, name = SelimEnchants.NAME, version = SelimEnchants.VERSION)
public class SelimEnchants {

	public static final String MOD_ID = "selim_enchants";
	public static final String NAME = "Selim's Enchants";
	public static final String VERSION = "2.1.0";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		EnchantConfig.load(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		EnchantConfig.init();

		IllusoryBehavior illusoryBehavior = new IllusoryBehavior();
		for (Entry<ResourceLocation, Item> e : ForgeRegistries.ITEMS.getEntries()) {
			Item item = e.getValue();
			if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemShears)
				if (Registry.Enchantments.ILLUSORY.canApply(new ItemStack(item)))
					BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, illusoryBehavior);
		}

		// TODO: Finish Ender IO compat
		// if (Loader.isModLoaded(CompatModIDs.ENDER_IO)) {
		// FMLInterModComms.sendMessage(CompatModIDs.ENDER_IO, "recipe:xml",
		// "<enderio:recipes xmlns:enderio=\"http://enderio.com/recipes\" "
		// + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		// + "xsi:schemaLocation=\"http://enderio.com/recipes recipes.xsd \">"
		//
		// + "<recipe name=\"Enchanter: selim_enchants:amplify\"
		// required=\"false\" disabled=\"false\">"
		// + "<enchanting><input name=\"minecraft:apple\" amount=\"16\"/>"
		// + "<enchantment name=\"selim_enchants:amplify\"
		// costMultiplier=\"1\"/>"
		// + "</enchanting></recipe>"
		//
		// + "</enderio:recipes>");
		// }
	}

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void clientPreInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingTool.class,
				new IRenderFactory<EntityFlyingTool>() {

					@Override
					public Render<? super EntityFlyingTool> createRenderFor(RenderManager manager) {
						return new FlyingToolRenderer(manager, Minecraft.getMinecraft().getRenderItem());
					}
				});
	}

	public static FakePlayerSelim getFakePlayer(Chunk chunk) {
		int dimId = chunk.getWorld().provider.getDimension();
		GameProfile profile = new GameProfile(
				UUID.fromString("e2d50290-b" + Integer.toHexString(chunk.x).substring(2, 5) + "-4"
						+ Integer.toHexString(chunk.z).substring(0, 3) + "-a8a1-d1de51c"
						+ String.format("%05x", dimId & 0xFFFFF)),
				"[" + SelimEnchants.NAME + " " + dimId + "(" + chunk.x + "," + chunk.z + ")]");
		return new FakePlayerSelim(chunk.getWorld(), profile);
	}

}
