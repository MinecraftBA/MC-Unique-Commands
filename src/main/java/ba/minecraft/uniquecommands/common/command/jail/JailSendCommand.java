package ba.minecraft.uniquecommands.common.command.jail;

import java.util.List;
import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.helper.JailsManager;
import ba.minecraft.uniquecommands.common.core.helper.TeleportationHelper;
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
												String name = StringArgumentType.getString(context, "name");
												String playerName = StringArgumentType.getString(context, "player");
												return sendJail(source, name, playerName);
											}
										)
								)
							
						)
				)
		);
		
	}
	
	private static int sendJail(CommandSourceStack source, String name, String playerName) throws CommandSyntaxException {

		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to a level where command was issued.
		ServerLevel level = source.getLevel();

		// Try get previously saved jail.
		Optional<JailDataRow> optionalJail = JailsManager.getJail(level, name);
		
		// IF: Jail was not previously saved..
		if (!optionalJail.isPresent()) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Jail with name " + name + " was not set!"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		// Get reference to jail.
		JailDataRow jail = optionalJail.get();
		
		// Get reference to server where code is running.
		MinecraftServer server = source.getServer();
		
		// Get list of players currently on the server.
		PlayerList playerList = server.getPlayerList();
		
		// Get array of players on the server.
		List<ServerPlayer> players = playerList.getPlayers();
		
		// Try locating player by name.
		Optional<ServerPlayer> optionalPlayer =	players.stream()
									      .filter(p -> p.getGameProfile().getName().contentEquals(playerName))
									      .findFirst();

		// IF: Player was not online.
		if (!optionalPlayer.isPresent()) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Player with the name " + playerName + " is not online"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		// Get reference to player.
		ServerPlayer player = optionalPlayer.get();
		
		// Try teleporting player to jail.
		boolean isTeleported = TeleportationHelper.teleportCommand(level, player, jail.getPosX(), jail.getPosY(), jail.getPosZ());
		
		// IF: There was error teleporting player.
		if(!isTeleported) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Player with the name " + playerName + " was not teleported to jail named " + name + "."
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
