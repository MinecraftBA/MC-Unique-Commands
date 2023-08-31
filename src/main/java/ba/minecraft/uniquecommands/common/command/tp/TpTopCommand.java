package ba.minecraft.uniquecommands.common.command.tp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.helper.TeleportationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TpTopCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("tptop")
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return tptop(source);
							}
						)
					);
	}
	
	private static int tptop(CommandSourceStack source) throws CommandSyntaxException {

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

		// Iterate from the top of the world until next location above player.
		for(int i= 320; i > y+3; i--) {
			
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
			"There is nothing above you where you can teleport and stand on."
		);
			
		// Send error message.
		source.sendFailure(message);
		
		return -1;
	}
	
}
