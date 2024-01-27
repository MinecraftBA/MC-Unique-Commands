package ba.minecraft.uniquecommands.common.command.home;

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

public final class HomeReturnCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("return")
						.then(
							Commands.argument("name", StringArgumentType.word())
							.requires((source) -> {
								return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
							})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									String locName = StringArgumentType.getString(context, "name");
									return returnHome(source, locName);
								}
							)
						)
				)
		);
		
	}
	
	private static int returnHome(CommandSourceStack source, String locName) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.HOME_ENABLED) {
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
		LocationData location = PlayerManager.loadLocationData(player,  locName);
		
		// IF: Location could not be determined.
		if (location == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Home with name " + locName + " was not set!"
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
				"Returned to home " + locName + ": " + location.getX() + " " + location.getY() + " " + location.getZ() + ""
			);
			
			return message;
			
		}, true);
		
		// Return success code.
		return 1;
		
	}
}
