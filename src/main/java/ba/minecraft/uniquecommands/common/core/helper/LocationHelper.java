package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class LocationHelper {

	public static ServerLevel getLevel(MinecraftServer server, String dimensionResId) {
		
		// Create resource location based on resource ID.
		ResourceLocation dimensionResLoc = ResourceLocation.withDefaultNamespace(dimensionResId);
		
		// Get resource key for dimension.
		ResourceKey<Level> dimensionResKey = ResourceKey.create(Registries.DIMENSION, dimensionResLoc);

		// Get reference to level that matches resource key.
		ServerLevel level = server.getLevel(dimensionResKey);
		
		return level;
	}
}
