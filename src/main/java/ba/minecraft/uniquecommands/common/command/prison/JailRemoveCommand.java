package ba.minecraft.uniquecommands.common.command.prison;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.JailManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

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
										String locName = StringArgumentType.getString(context, "name");
										return deleteJail(source, locName);
									}
								)

							)
					)
			);
	}
	
	private static int deleteJail(CommandSourceStack source,String locName) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		ServerLevel level = source.getLevel();
		boolean exists = locName != null;
		// IF: Entries were found in persistent data = location was saved previously.
		if(exists) {

			// Remove entries.
			JailManager.removeJail(null, locName);
			
			source.sendSuccess(() -> {

				MutableComponent message = Component.literal(
						"Jail " + locName + " was deleted."
				);

				return message;
				
			}, true);
			
			return 1;
		} else {

			MutableComponent message = Component.literal(
				"Jail " + locName + " was not found!"
			);
			
			source.sendFailure(message);
			
			return -1;
		}
	}
}