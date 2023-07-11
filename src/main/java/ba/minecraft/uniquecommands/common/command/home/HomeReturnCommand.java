package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class HomeReturnCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("return")
						.then(
							Commands.argument("name", StringArgumentType.word())
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
			MutableComponent message = Component.translatable(
				"command." + UniqueCommandsMod.MODID + ".home_return.failure"
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

		// Create resource location.
		ResourceLocation resLoc = new ResourceLocation(resLocId);
		
		// Get resource key for dimension.
		ResourceKey<Level> resKey = ResourceKey.create(Registries.DIMENSION, resLoc);
		
		// Get reference to server where code is running.
		MinecraftServer server = player.getServer();

		// Get reference to level that was saved with command.
		ServerLevel level = server.getLevel(resKey);
		
		float yaw = player.getYRot();
		float pitch = player.getXRot();
		
		// Teleport player to coordinates.
		player.teleportTo(level, x, y, z, yaw, pitch);
		
		// Send confirmation message.
		source.sendSuccess(() -> {

			// Create success message.
			MutableComponent message = Component.translatable(
				"command."  + UniqueCommandsMod.MODID + ".home_return.success", locName, x, y, z
			);
			
			return message;
			
		}, true);
		
		// Return success code.
		return 1;
		
	}
}
