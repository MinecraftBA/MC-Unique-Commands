package ba.minecraft.uniquecommands.common.command.seen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.data.PlayerSeenData;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

public final class SeenCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("seen")
				.then(
					Commands.argument("playerName", StringArgumentType.word())
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								String playerName = StringArgumentType.getString(context, "playerName");
								return displaySeen(source, playerName);
							}
						)
				)
		);
		
	}
	
	private static int displaySeen(CommandSourceStack source, String playerName) throws CommandSyntaxException {

		// Get reference to server.
		ServerLevel serverLevel = source.getLevel();
		
		// Get seen data saved for provided player name.
		List<PlayerSeenData> playersSeenData = PlayerManager.getSeen(serverLevel, playerName);
		
		// IF: Data was not saved for provided player name.
		if (playersSeenData.size() == 0) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"Player " + playerName + " was never seen before." 
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
			
		}

		playersSeenData.forEach($playerSeenData -> {
			
			// Get player UUID from saved data.
			UUID playerUuid = $playerSeenData.getPlayerId();

			// Get first 8 characters of player UUID - not more to avoid impersonation.
			String playerId = StringUtils.truncate(playerUuid.toString(), 8);
			
			// Get timestamp when player was last seen.
			LocalDateTime timestamp = $playerSeenData.getTimestamp();

			// Create date/time formatter.
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
			
			// Format timestamp to human readable format.
			String formattedTimestamp = timestamp.format(formatter);
			
			// Send message to console.
			source.sendSuccess(() -> {

				// Create message to be displayed in console.		
				MutableComponent message = Component.literal(
					"Player " + playerName + " was last seen " + formattedTimestamp + " (UUID: " + playerId + ")"
				);

				return message;
				
			}, true);
		});
		
		// Indicate success (1 = true).
		return 1;
		
	}
}
