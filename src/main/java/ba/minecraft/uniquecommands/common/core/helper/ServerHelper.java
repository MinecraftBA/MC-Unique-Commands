package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class ServerHelper {

	public static boolean isOnlineMode() {
		
	    // Get the current Minecraft server instance
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        // Check if the server is in online mode
        if (server != null) {
            return server.usesAuthentication();
        }

         // Default to false if server instance is not available
        return false;
	}
	
	public static DimensionDataStorage getServerStorage(ServerLevel level) {
		
		// Get reference to running instance of Minecraft server.
		MinecraftServer server = level.getServer();
		
		// Get reference to Overworld as that world must always exist.
		ServerLevel overworld = server.getLevel(Level.OVERWORLD);
		
		// Return data storage for overworld to be used as server wide storage.
		return overworld.getDataStorage();
		
	}
}
