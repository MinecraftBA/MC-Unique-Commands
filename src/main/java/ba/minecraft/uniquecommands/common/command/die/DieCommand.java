package ba.minecraft.uniquecommands.common.command.die;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class DieCommand {
	
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("die")
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return die(source);
							}
						)
					);
	}
	
	private static int die(CommandSourceStack source) throws CommandSyntaxException {

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
