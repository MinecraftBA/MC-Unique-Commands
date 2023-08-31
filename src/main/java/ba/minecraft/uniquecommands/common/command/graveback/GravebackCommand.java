package ba.minecraft.uniquecommands.common.command.graveback;

import java.util.Optional;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.data.PlayerDeathDataRow;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class GravebackCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("graveback")
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return graveback(source);
							}
						)
					);
			
	}
	
	private static int graveback(CommandSourceStack source) throws CommandSyntaxException {

		// Get reference to player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get level on which is player currently.
		ServerLevel serverLevel = player.serverLevel();
		
		// Get player UUID.
		UUID playerId = player.getUUID();
		
		// Get rotation of player.
		float yaw = player.getYRot();
		float pitch = player.getXRot();
		
		Optional<PlayerDeathDataRow> optionalDataRow = PlayerManager.loadDeathData(serverLevel, playerId);
		
		// IF: Data row was found.
		if(optionalDataRow.isPresent()) {
			
			// Get data row value.
			PlayerDeathDataRow dataRow = optionalDataRow.get();
				
			// Get coordinates from data row.
			int posX = dataRow.getPosX();
			int posY = dataRow.getPosY();
			int posZ = dataRow.getPosZ();
			
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
