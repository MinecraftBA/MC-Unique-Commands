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

public final class HomeListCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("list")
					.requires((source) -> {
						return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
					})
					.executes(
						(context) -> {
							CommandSourceStack source = context.getSource();
							return listHome(source);
						}
					)
				)
		);
		
	}
	
	private static int listHome(CommandSourceStack source) throws CommandSyntaxException {
		if(!UniqueCommandsModConfig.HOME_ENABLED) {
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
		
		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// Get list of all keys for stored data.
		Set<String> keys = data.getAllKeys();
		
		for(String key : keys) {

			if(key.startsWith(UniqueCommandsMod.MODID + ":home:") && key.endsWith(":coords")) {

				String locName = key.replace(":coords", "")
									.replace(UniqueCommandsMod.MODID + ":home:", "");
				
				int[] coordinates = data.getIntArray(key);

				// Extract coordinates from array.
				int x = coordinates[0];
				int y = coordinates[1];
				int z = coordinates[2];

				String resLocId = data.getString(UniqueCommandsMod.MODID + ":home:" + locName + ":dim")
						.replace("minecraft:", ""); // Remove minecraft: prefix from location for better displaying.
				
				// Send confirmation message.
				source.sendSuccess(() -> {

					// Create success message.
					MutableComponent message = Component.translatable(
							"Home " + locName + " is set to: " + x + " " + y + " " + z + " (" + resLocId + ")"
					);
					
					return message;
					
				}, true);
				
			}
			
		}

		// Return success code.
		return 1;
	}
}
