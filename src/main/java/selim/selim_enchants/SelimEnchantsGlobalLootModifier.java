package selim.selim_enchants;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import selim.selim_enchants.enchants.EnderShiftEnchantment.EnderShiftLootModifier;

public class SelimEnchantsGlobalLootModifier implements Codec<EnderShiftLootModifier> {

	@Override
	public <T> DataResult<T> encode(EnderShiftLootModifier input, DynamicOps<T> ops, T prefix) {
		return null;
	}

	@Override
	public <T> DataResult<Pair<EnderShiftLootModifier, T>> decode(DynamicOps<T> ops, T input) {
		return null;
	}

}
