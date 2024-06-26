package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.models.LocationData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class HomeSetCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("set")
						.then(
							Commands.argument("name", StringArgumentType.word())
							.requires((source) -> {
								return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
							})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									String locName = StringArgumentType.getString(context, "name");
									return setHome(source, locName);
								}
							)

						)
				)
		);
	}
	
	private static int setHome(CommandSourceStack source, String locName) throws CommandSyntaxException {
		
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
		
		// Save current location to player data.
		LocationData location = PlayerManager.saveLocationData(player, "home", locName);

		// Create message to be displayed in console.		
		MutableComponent message = Component.literal(
			"Home " + locName + " is set to: " + location.getX() + " " + location.getY() + " " + location.getZ() + ""
		);

		// Send message to console.
		source.sendSuccess(() -> message, true);
		
		// Indicate success (1 = true).
		return 1;
		
	}
}
