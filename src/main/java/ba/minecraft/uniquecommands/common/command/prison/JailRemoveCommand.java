package ba.minecraft.uniquecommands.common.command.prison;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.JailManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

public class JailRemoveCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("jail")
					.then(
						Commands.literal("remove")
							.then(
								Commands.argument("name", StringArgumentType.word())
								.requires((source) -> {
									return source.hasPermission(UniqueCommandsModConfig.JAIL_OP_LEVEL);
								})
								.executes(
									(context) -> {
										CommandSourceStack source = context.getSource();
										String name = StringArgumentType.getString(context, "name");
										return deleteJail(source, name);
									}
								)

							)
					)
			);
	}
	
	private static int deleteJail(CommandSourceStack source,String name) throws CommandSyntaxException {

		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to level where command was issued.
		ServerLevel level = source.getLevel();

		// Try to remove jail.
		boolean isRemoved = JailManager.removeJail(level, name);

		// IF: Jail was not removed.
		if(!isRemoved) {
			
			MutableComponent message = Component.literal(
				"Jail " + name + " was not found!"
			);
			
			source.sendFailure(message);
			
			return -1;
		}
		
		source.sendSuccess(() -> {

			MutableComponent message = Component.literal(
				"Jail " + name + " was deleted."
			);

			return message;
			
		}, true);
		
		return 1;
	}
}