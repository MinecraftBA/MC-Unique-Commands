package ba.minecraft.uniquecommands.common.command.graveback;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.LocationHelper;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.models.LocationData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class GravebackCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("graveback")
				.requires((source) -> {
					return source.hasPermission(UniqueCommandsModConfig.GRAVEBACK_OP_LEVEL);
						})
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return graveback(source);
							}
						)
					);
			
	}
	
	private static int graveback(CommandSourceStack source) throws CommandSyntaxException {
		
		if(!UniqueCommandsModConfig.GRAVEBACK_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get rotation of player.
		float yaw = player.getYRot();
		float pitch = player.getXRot();
		
		LocationData deathLocation = PlayerManager.loadDeathData(player);
		
		// IF: Data location was found.
		if(deathLocation != null) {
			
			// Get coordinates from data row.
			int posX = deathLocation.getX();
			int posY = deathLocation.getY();
			int posZ = deathLocation.getZ();
			
			// Get dimension resource identifier.
			String dimResId = deathLocation.getDimensionResId();
			
			// Get reference to Minecraft server
			MinecraftServer server = player.getServer();
			
			// Convert dimension resource Id to Level.
			ServerLevel serverLevel = LocationHelper.getLevel(server, dimResId);
			
			// Teleport player to coordinates.
			player.teleportTo(serverLevel,posX,posY,posZ,yaw,pitch);
			
			return 1;
			
		} else {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"You have never died on this world before. Have you considered playing hardcore?"
			);
				
			// Send error message.
			source.sendFailure(message);
			
			return -1;
		}
		
	
	}
	
}
