package selim.selim_enchants;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ITooltipInfo {

	@OnlyIn(Dist.CLIENT)
	public default void addInformation(ItemStack stack, @Nullable Level level, List<Component> tooltip,
			TooltipFlag flagIn) {
	}

}
