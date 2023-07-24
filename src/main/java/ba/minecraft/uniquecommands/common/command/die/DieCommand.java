package ba.minecraft.uniquecommands.common.command.die;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
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
								String playerName = StringArgumentType.getString(context, "playerName");
								return die(source,playerName);
							}
						)
					);
			
	}
	private static int die(CommandSourceStack source,String playerName ) throws CommandSyntaxException {

		// Get reference to player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		
		player.kill();

			// Create message to be displayed in console.		
		source.sendSuccess(() -> {

			// Create message to be displayed in console.		
			MutableComponent message = Component.translatable(
				"command." + UniqueCommandsMod.MODID + ".die.success", 
				playerName
			);
			

			return message;
			
		}, true);
		return 1;
	}
}
