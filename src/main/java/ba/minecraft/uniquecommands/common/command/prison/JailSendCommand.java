package ba.minecraft.uniquecommands.common.command.prison;

import java.util.List;
import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.helper.JailManager;
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
import net.minecraft.server.players.PlayerList;

public final class JailSendCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("jail")
				.then(
					Commands.literal("send")
						.then(
							Commands.argument("name", StringArgumentType.word())
								.then(
									Commands.argument("player", StringArgumentType.word())	
										.requires((source) -> {
											return source.hasPermission(UniqueCommandsModConfig.JAIL_OP_LEVEL);
										})
										.executes(
											(context) -> {
												CommandSourceStack source = context.getSource();
												String playerName = StringArgumentType.getString(context, "player");
												String jailName = StringArgumentType.getString(context, "name");
												return sendJail(source, playerName, jailName);
											}
										)
								)
							
						)
				)
		);
		
	}
	
	private static int sendJail(CommandSourceStack source, String playerName, String name) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		ServerLevel level = source.getLevel();
		// Load location data from player persistance data.
		Optional<JailDataRow> optionalJail = JailManager.getJail(level, name);
		
		// IF: Location could not be determined.
		if (optionalJail == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Jail with name " + name + " was not set!"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		JailDataRow jail = optionalJail.get();
		
		// Get reference to server where code is running.
		MinecraftServer server = source.getServer();
		
		// Get list of players currently on the server.
		PlayerList playerList = server.getPlayerList();
		
		// Get array of players on the server.
		List<ServerPlayer> players = playerList.getPlayers();
		
		Optional<ServerPlayer> optionalPlayer =	players.stream()
									      .filter(p -> p.getGameProfile().getName().contentEquals(playerName))
									      .findFirst();
		// IF: Location could not be determined.
		if (optionalPlayer == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Player with the name " + optionalPlayer + " is not online"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		ServerPlayer player = optionalPlayer.get();
		
		boolean isTped = TeleportationHelper.teleportCommand(level, player, jail.getPosX(), jail.getPosY(), jail.getPosZ());
		
		if(!isTped) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Player with the name " + optionalPlayer + " was not teleported"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
			
		}
		
		// Send confirmation message.
		source.sendSuccess(() -> {

			// Create success message.
			MutableComponent message = Component.literal(
				"Sent " + playerName + " to jail " + jail.getName() + ": " + jail.getPosX() + " " + jail.getPosY() + " " + jail.getPosZ() + "."
			);
			
			return message;
			
		}, true);
		
		// Return success code.
		return 1;
		
	}
}
