package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.ServerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class HomeReturnCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("return")
						.then(
							Commands.argument("name", StringArgumentType.word())
							.requires((source) -> {
								return source.hasPermission(UniqueCommandsModConfig.HOME_OP_LEVEL);
							})
							.executes(
								(context) -> {
									CommandSourceStack source = context.getSource();
									String locName = StringArgumentType.getString(context, "name");
									return returnHome(source, locName);
								}
							)
						)
				)
		);
		
	}
	
	private static int returnHome(CommandSourceStack source, String locName) throws CommandSyntaxException {
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
		
		// Create key => experimentalmod:home
		String key = UniqueCommandsMod.MODID + ":home:" + locName;
		
		// Retrieve coordinates by providing key to persistent data.
		int[] coordinates = data.getIntArray(key + ":coords");
		
		// IF: Coordinates were not saved previously.
		if (coordinates.length == 0) {
			
			// Create error message.
			MutableComponent message = Component.literal(
					"Home with name " + locName + " was not set!"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}
		
		// Extract coordinates from array.
		int x = coordinates[0];
		int y = coordinates[1];
		int z = coordinates[2];
		
		// Get ID of resource location for dimension.
		String resLocId = data.getString(key + ":dim");

		// Get reference to server where code is running.
		MinecraftServer server = player.getServer();

		// Get server level based on its resource identifier.
		ServerLevel level = ServerHelper.getLevel(server, resLocId);

		float yaw = player.getYRot();
		float pitch = player.getXRot();
		
		// Teleport player to coordinates.
		player.teleportTo(level, x, y, z, yaw, pitch);
		
		// Send confirmation message.
		source.sendSuccess(() -> {

			// Create success message.
			MutableComponent message = Component.literal(
				"Returned to home " + locName + ": " + x + " " + y + " " + z + ""
			);
			
			return message;
			
		}, true);
		
		// Return success code.
		return 1;
		
	}
}
