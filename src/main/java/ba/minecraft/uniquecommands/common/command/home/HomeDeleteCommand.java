package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class HomeDeleteCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("home")
					.then(
						Commands.literal("delete")
							.then(
								Commands.argument("name", StringArgumentType.word())
								.requires((source) -> {
									return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
								})
								.executes(
									(context) -> {
										CommandSourceStack source = context.getSource();
										String locName = StringArgumentType.getString(context, "name");
										return deleteHome(source, locName);
									}
								)

							)
					)
			);
	}
	
	private static int deleteHome(CommandSourceStack source,String locName) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.HOME_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		// Get reference to a player that called the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get reference to player's persistent data.
		CompoundTag data = player.getPersistentData();

		// Create key => uniquecommands:home:...
		String key = UniqueCommandsMod.MODID + ":home:" + locName;
		
		String coordsKey = key + ":coords";
		String dimKey = key + ":dim";
		
		// IF: Entries were found in persistent data = location was saved previously.
		if(data.contains(coordsKey) && data.contains(dimKey)) {

			// Remove entries.
			data.remove(coordsKey);
			data.remove(dimKey);
			
			source.sendSuccess(() -> {

				MutableComponent message = Component.literal(
						"Home " + locName + " was deleted."
				);

				return message;
				
			}, true);
			
			return 1;
		} else {

			MutableComponent message = Component.literal(
				"Home " + locName + " was not found!"
			);
			
			source.sendFailure(message);
			
			return -1;
		}
	}
}
