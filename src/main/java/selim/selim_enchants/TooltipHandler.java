package selim.selim_enchants;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SelimEnchants.MOD_ID)
public class TooltipHandler {

	private final static String ENCHANT_DESC_ID = "enchdesc";

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		List<Component> tooltip = event.getToolTip();

		// show disabled enchants
		if (event.getFlags().isAdvanced() && stack.isEnchanted()) {
			CompoundTag nbt = stack.getTag();
			if (nbt.contains(DisabledEnchantCleanup.DISABLED_ENCHANT_TAG)) {
				ListTag disEnch = nbt.getList(DisabledEnchantCleanup.DISABLED_ENCHANT_TAG, 10);
				if (disEnch.size() > 0) {
					tooltip.add(Component.translatable(SelimEnchants.MOD_ID + ".num_enchants_disabled", disEnch.size())
							.withStyle(ChatFormatting.DARK_GRAY));
					for (Entry<Enchantment, Integer> e : EnchantmentHelper.deserializeEnchantments(disEnch).entrySet())
						tooltip.add(Component.literal(" ").append(e.getKey().getFullname(e.getValue()).plainCopy())
								.withStyle(ChatFormatting.DARK_GRAY));
				}
			}
		}

		for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			Enchantment ench = enchant.getKey();
			if (!(ench instanceof ITooltipInfo))
				continue;
			Component name = ench.getFullname(enchant.getValue());
			int index = -1;
			for (int i = 0; i < tooltip.size(); i++)
				if (name.equals(tooltip.get(i)))
					index = i;
			if (index == -1)
				continue;
			index++;
			// Print out disabled text before skipping
			// if there are other tooltip mods
			if (ench instanceof SelimEnchant && !EnchantConfig.isEnabled((SelimEnchant) ench)) {
				tooltip.add(index,
						Component.literal(" ").append(Component.translatable(SelimEnchants.MOD_ID + ".enchant_disabled")
								.withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)));
				continue;
			}
			// Skip if other tooltip mods are installed
			if (ModList.get().isLoaded(ENCHANT_DESC_ID))
				continue;

			// "Shift for more info" text
			if (!Screen.hasShiftDown()) {
				tooltip.add(index,
						Component.literal(" ").append(Component.translatable(SelimEnchants.MOD_ID + ".shift_for_info")
								.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
				continue;
			}

			// Get and translate ITooltipInfo text
			List<Component> infoList = new LinkedList<>();
			ITooltipInfo tooltipInfo = (ITooltipInfo) ench;
			tooltipInfo.addInformation(stack, event.getEntity().level, infoList, event.getFlags());
			if (!infoList.isEmpty())
				for (int i = 0; i < infoList.size(); i++)
					tooltip.add(index + i, Component.literal(" ").append(infoList.get(i))
							.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
			else
				tooltip.add(index,
						Component.literal(" ").append(Component.translatable(SelimEnchants.MOD_ID + ":no_info_found")
								.withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)));
		}
	}

}
