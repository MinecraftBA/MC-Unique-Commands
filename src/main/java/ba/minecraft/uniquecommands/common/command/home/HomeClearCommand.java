package ba.minecraft.uniquecommands.common.command.home;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class HomeClearCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("home")
					.then(
						Commands.literal("clear")
						.requires((source) -> {
							return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
						})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									return clearHome(source);
								}
							)
					)
			);
	}
	
	private static int clearHome(CommandSourceStack source) throws CommandSyntaxException {
		
		if(!UniqueCommandsModConfig.HOME_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		// Get reference to a player that has executed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get player's persistent data.
		CompoundTag data = player.getPersistentData();
		
		// Get all keys for stored player data.
		Set<String> keysSet = data.getAllKeys();
		
		// Preload all keys to a separate array (to avoid concurrent error).
		String[] keys = new String[keysSet.size()];
		keysSet.toArray(keys);

		boolean removed = false;
		
		// Iterate through all keys.
		for(String key : keys) {
			
			// IF: Key is created by home commands.
			if(key.startsWith(UniqueCommandsMod.MODID + ":home:")) {
				
				// Remove it.
				data.remove(key);
				
				removed = true;
			}
			
		}

		// IF: At least 1 location has been removed.
		if(removed) {

			// Send confirmation message.
			source.sendSuccess(() -> {

				// Create success message.
				 MutableComponent message = Component.literal(
					"All homes have been cleared."
				);
				
				return message;
				
			}, true);
			
			return 1;
			
		} else {

			// Create failure message.
			MutableComponent message = Component.literal(
				"No homes were found/cleared."
			);
			
			// Send confirmation message.
			source.sendFailure(message);

			return -1;	

		}
	}
}

