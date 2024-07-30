package ba.minecraft.uniquecommands.common.command.tp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.TeleportationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TpBottomCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("tpbottom")
				.requires((source) -> {
					return source.hasPermission(UniqueCommandsModConfig.TP_OP_LEVEL);
				})
				.executes(
					(context) -> {
						CommandSourceStack source = context.getSource();
						return tpbottom(source);
					}
				)
			);
	}
	
	private static int tpbottom(CommandSourceStack source) throws CommandSyntaxException {
		
		if(!UniqueCommandsModConfig.TP_ENABLED) {
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
		
		// Get reference to a level where player is located.
		ServerLevel level = player.serverLevel();
		
		// Get position of player.
		BlockPos playerPos = player.blockPosition();

		// Get player coordinates.
		int x = playerPos.getX();
		int y = playerPos.getY();
		int z = playerPos.getZ();
		
		// Iterate from the world buttom until next free space below player.
		for(int i= -64; i < y-3; i++) {

			// Teleport player to location.
			boolean isTeleported = TeleportationHelper.teleportCommand(level, player, x, i, z);

			// IF: Teleportation was successful.
			if (isTeleported) {

				// Indicate success.
				return 1;
			}
  			
		}

		// Create error message.
		MutableComponent message = Component.literal(
				"There is nothing below you where you can teleport and stand on."
		);
			
		// Send error message.
		source.sendFailure(message);
		
		return -1;
	}
}
