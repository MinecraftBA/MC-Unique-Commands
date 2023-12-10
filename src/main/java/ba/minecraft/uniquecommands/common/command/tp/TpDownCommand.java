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

public class TpDownCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("tpdown")
				.requires((source) -> {
					return source.hasPermission(UniqueCommandsModConfig.TP_OP_LEVEL);
				})
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return tpdown(source);
							}
						)
					);
	}
	
	private static int tpdown(CommandSourceStack source) throws CommandSyntaxException {
		
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
		
		// Get player's current rotation.
		float yaw = player.getYRot();
		float pitch = player.getXRot();

		// Iterate through all blocks from player's current position to world bottom.
		for(int i=y-3; i > -64; i--) {
			
			// Get position of block above.
			BlockPos blockPos = new BlockPos(x,i,z);

			// IF: Location is safe for teleportation.
			if(TeleportationHelper.isSafe(level, blockPos)) {
				
				// Teleport player.
	    		player.teleportTo(level, x, i, z, yaw, pitch);
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

