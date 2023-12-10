package ba.minecraft.uniquecommands.common.command.die;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class DieCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("die")
						.requires((source) -> {
							return source.hasPermission(UniqueCommandsModConfig.DIE_OP_LEVEL);
						})
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return die(source);
							}
						)
					);
	}
	
	private static int die(CommandSourceStack source) throws CommandSyntaxException {

		// IF: Command is not enabled.
		if(!UniqueCommandsModConfig.DIE_ENABLED) {
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

		// IF: Player is not alive.
		if(!player.isAlive()) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"You are already dead. How did you manage to type this command?"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Kill the player.
		player.kill();

		return 1;
	}
}
