package ba.minecraft.uniquecommands.common.command.tpup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TpUpCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("tpup")
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return tpup(source);
							}
						)
					);
	}
	
	private static int tpup(CommandSourceStack source) throws CommandSyntaxException {

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

		// Iterate through all blocks from player's current position to max world height.
		for(int i=y+3; i < 320; i++) {
			
			// Get position of block above.
			BlockPos blockPos = new BlockPos(x,i,z);
			
			BlockPos aboveBlockPos = blockPos.above();
			BlockPos belowBlockPos = blockPos.below();
			
			// Get blockstate of block above.
			BlockState blockState = level.getBlockState(blockPos);
			BlockState aboveBlockState = level.getBlockState(aboveBlockPos);
			BlockState belowBlockState = level.getBlockState(belowBlockPos);
			
			// IF: Block is free for transportation.
  		    if(blockState.is(Blocks.AIR)) {
  		    	if(aboveBlockState.is(Blocks.AIR)) {
  		    		if(!belowBlockState.is(Blocks.AIR)) {
  		    			player.teleportTo(level, x, i, z, yaw, pitch);
  		    			return 1;
  		    		} 
  		    	} 
  		    		
  			    	
  			    

				
		    }
  			
		}
		// Create error message.
			MutableComponent message = Component.literal(
				"There is no block above you. Have you made a base above height limit?"
			);
				
			// Send error message.
			source.sendFailure(message);
			
			return -1;
	}
}
