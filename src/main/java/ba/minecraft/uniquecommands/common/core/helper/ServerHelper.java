package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.server.MinecraftServer;
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
}
