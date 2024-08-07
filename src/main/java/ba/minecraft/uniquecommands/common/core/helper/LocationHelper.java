package ba.minecraft.uniquecommands.common.core.helper;

import ba.minecraft.uniquecommands.common.core.models.LocationData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class LocationHelper {

	public static ServerLevel getLevel(MinecraftServer server, String dimensionResId) {
		
		// Create resource location based on resource ID.
		ResourceLocation dimensionResLoc = ResourceLocation.parse(dimensionResId);

		// Get resource key for dimension.
		ResourceKey<Level> dimensionResKey = ResourceKey.create(Registries.DIMENSION, dimensionResLoc);

		// Get reference to level that matches resource key.
		ServerLevel level = server.getLevel(dimensionResKey);

		return level;
	}
	
	public static String getDimensionResId(ServerLevel level) {

		// Get resource key for the dimension of level.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get location of dimension resource.
		ResourceLocation resLoc = dimension.location();
		
		// Get resource ID of the dimension.
		String dimId = resLoc.toString();
		
		return dimId;
	}
	
	public static LocationData getPlayerLocation(ServerPlayer player) 
	{
		// Get position of lower player block.
		BlockPos playerPos = player.blockPosition();
		
		// Get reference to level at which player is.
		ServerLevel level = player.serverLevel();
		
		// Get dimension resource location identifier.
		String dimensionResId = getDimensionResId(level);
		
		// Create location instance.
		LocationData location = new LocationData(playerPos, dimensionResId);

		return location;
	}
}
