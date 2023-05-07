package selim.selim_enchants;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DisabledEnchantCleanup {

	public static final String DISABLED_ENCHANT_TAG = "selim_enchants:disabled_enchs";

	private static final Set<UUID> CLEANED = new HashSet<>();

	@SubscribeEvent
	public static void cleanupEnchants(PlayerTickEvent event) {
		if (CLEANED.contains(event.player.getUUID()))
			return;
		Player player = event.player;
		cleanInventory(player.getInventory());
		CLEANED.add(player.getUUID());
	}

	private static void cleanInventory(Inventory inv) {
		for (ItemStack stack : inv.items)
			cleanStack(stack);
	}

	private static void cleanStack(ItemStack stack) {
		removeDisabled(stack);
		addEnabled(stack);
	}

	private static void removeDisabled(ItemStack stack) {
		if (!stack.isEnchanted())
			return;
		CompoundTag stackTag = stack.getTag();
		ListTag disabled = stackTag.getList(DISABLED_ENCHANT_TAG, 10);
		ListTag enchs = stack.getEnchantmentTags();
		for (Tag tag : enchs) {
			if (!(tag instanceof CompoundTag))
				continue;
			CompoundTag cTag = (CompoundTag) tag;
			if (cTag.getString("id").startsWith(SelimEnchants.MOD_ID)
					&& !EnchantConfig.isEnabled(new ResourceLocation(cTag.getString("id")))) {
				disabled.add(tag);
			}
		}
		enchs.removeAll(disabled);

		stackTag.put(DISABLED_ENCHANT_TAG, disabled);
	}

	private static void addEnabled(ItemStack stack) {
		if (!stack.hasTag())
			return;
		CompoundTag stackTag = stack.getTag();
		ListTag enabled = new ListTag();
		ListTag disabled = stackTag.getList(DISABLED_ENCHANT_TAG, 10);
		for (Tag tag : disabled) {
			if (!(tag instanceof CompoundTag))
				continue;
			CompoundTag cTag = (CompoundTag) tag;
			if (cTag.getString("id").startsWith(SelimEnchants.MOD_ID)
					&& EnchantConfig.isEnabled(new ResourceLocation(cTag.getString("id")))) {
				enabled.add(tag);
			}
		}
		disabled.removeAll(enabled);

		Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(stack);
		Map<Enchantment, Integer> enabledEnchs = EnchantmentHelper.deserializeEnchantments(enabled);
		enchs.putAll(enabledEnchs);
		EnchantmentHelper.setEnchantments(enchs, stack);
	}

}
