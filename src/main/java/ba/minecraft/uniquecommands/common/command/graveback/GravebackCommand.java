package ba.minecraft.uniquecommands.common.command.graveback;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.LocationHelper;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.helper.TeleportationHelper;
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
		
		LocationData deathLocation = PlayerManager.loadDeathData(player);
		
		// IF: Death location was not determined.
		if(deathLocation == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"You have never died on this world before. Have you considered playing hardcore?"
			);
				
			// Send error message.
			source.sendFailure(message);
			
			return -1;
		}
		
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

		// Teleport player.
		boolean isTeleported = TeleportationHelper.teleportCommand(serverLevel, player, posX, posY, posZ);
		
		// IF: Teleportation was not successful.
		if(!isTeleported) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"Teleportation to place of your demise has failed. :("
			);
				
			// Send error message.
			source.sendFailure(message);
			
			return -1;
		}
		
		// Create message to be displayed in console.		
		MutableComponent message = Component.literal(
			"You have been returned to place of your death: " + posX + " " + posY + " " + posZ + ""
		);

		// Send message to console.
		source.sendSuccess(() -> message, true);
		
		return 1;
	
	}
	
}
