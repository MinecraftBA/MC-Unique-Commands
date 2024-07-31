package ba.minecraft.uniquecommands.common.command.prison;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.JailManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class JailSetCommand {
	   
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("jail")
				.then(
					Commands.literal("set")
						.then(
							Commands.argument("name", StringArgumentType.word())
							.requires((source) -> {
								return source.hasPermission(UniqueCommandsModConfig.JAIL_OP_LEVEL);
							})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									String name = StringArgumentType.getString(context, "name");
									return setJail(source, name);
								}
							)

						)
				)
		);
	}
	
	private static int setJail(CommandSourceStack source, String name) throws CommandSyntaxException {
			
			if(!UniqueCommandsModConfig.JAIL_ENABLED) {
				// Create error message.
				MutableComponent message = Component.literal(
					"Command is not enabled. Hey, not my fault!"
				);
					
				// Send error message.
				source.sendFailure(message);
	
				return -1;
			}
			
			// Get reference to a level where command was executed.
			ServerLevel level = source.getLevel();
			
			// Get reference to player that has typed the command.
			ServerPlayer player = source.getPlayerOrException();

			// Get position where player is standing.
			BlockPos blockPos = player.blockPosition();
			
			// Save current location to jail data.
			JailManager.setJail(level, name, blockPos);
	
			// Create message to be displayed in console.		
			MutableComponent message = Component.literal(
				"Jail " + name + " is set to: " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ() + ""
			);
	
			// Send message to console.
			source.sendSuccess(() -> message, true);
			
			// Indicate success (1 = true).
			return 1;
			
		}
}
