package selim.selim_enchants;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SelimEnchants.MOD_ID)
public class TooltipHandler {

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		List<String> tooltip = event.getToolTip();
		for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			Enchantment ench = enchant.getKey();
			if (!(ench instanceof ITooltipInfo))
				continue;
			String name = ench.getTranslatedName(enchant.getValue());
			int index = -1;
			for (int i = 0; i < tooltip.size(); i++)
				if (name.equals(tooltip.get(i)))
					index = i;
			if (index == -1)
				continue;
			index++;
			if (!GuiScreen.isShiftKeyDown()) {
				tooltip.add(index, " " + ChatFormatting.DARK_GRAY + ChatFormatting.ITALIC
						+ I18n.format(SelimEnchants.MOD_ID + ":shift_for_info"));
				continue;
			}
			List<String> infoList = new LinkedList<>();
			ITooltipInfo tooltipInfo = (ITooltipInfo) ench;
			tooltipInfo.addInformation(stack, event.getEntity().world, infoList, event.getFlags());
			if (!infoList.isEmpty())
				for (int i = 0; i < infoList.size(); i++)
					tooltip.add(index + i,
							" " + ChatFormatting.DARK_GRAY + ChatFormatting.ITALIC + infoList.get(i));
			else
				tooltip.add(index, " " + ChatFormatting.DARK_GRAY + ChatFormatting.ITALIC
						+ I18n.format(SelimEnchants.MOD_ID + ":no_info_found"));
		}
	}

}
