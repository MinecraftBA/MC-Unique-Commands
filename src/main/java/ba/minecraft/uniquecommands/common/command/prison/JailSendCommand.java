package ba.minecraft.uniquecommands.common.command.prison;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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

public final class JailSendCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("return")
						.then(
							Commands.argument("name", StringArgumentType.word())
							.requires((source) -> {
								return source.hasPermission(UniqueCommandsModConfig.JAIL_OP_LEVEL);
							})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									String playerName = StringArgumentType.getString(context, "name");
									return sendJail(source, playerName);
								}
							)
						)
				)
		);
		
	}
	
	private static int sendJail(CommandSourceStack source, String playerName) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
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

		// Load location data from player persistance data.
		LocationData location = PlayerManager.loadLocationData(player, "jail",  playerName);
		
		// IF: Location could not be determined.
		if (location == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Jail with name " + playerName + " was not set!"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		// Get reference to server where code is running.
		MinecraftServer server = player.getServer();

		// Get server level based on its resource identifier.
		ServerLevel level = LocationHelper.getLevel(server, location.getDimensionResId());

		float yaw = player.getYRot();
		float pitch = player.getXRot();
		
		// Teleport player to coordinates.
		player.teleportTo(level, location.getX(), location.getY(), location.getZ(), yaw, pitch);
		
		// Send confirmation message.
		source.sendSuccess(() -> {

			// Create success message.
			MutableComponent message = Component.literal(
				"Sent " + playerName + " to jail: " + location.getX() + " " + location.getY() + " " + location.getZ() + ""
			);
			
			return message;
			
		}, true);
		
		// Return success code.
		return 1;
		
	}
}
