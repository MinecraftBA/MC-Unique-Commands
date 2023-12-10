package ba.minecraft.uniquecommands.common.command.where;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;

public final class WhereCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("where")
				.then(
					Commands.argument("playerName", StringArgumentType.word())
					.requires((source) -> {
						return source.hasPermission(UniqueCommandsModConfig.WHERE_OP_LEVEL);
					})
					.executes(
						(context) -> {
							CommandSourceStack source = context.getSource();
							String playerName = StringArgumentType.getString(context, "playerName");
							return displayCoordinates(source, playerName);
						}
					)
				)
		);
		
	}
	
	private static int displayCoordinates(CommandSourceStack source, String playerName) throws CommandSyntaxException {
		
		if(!UniqueCommandsModConfig.HOME_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to currently running Minecraft server instance.
		MinecraftServer server = source.getServer();
		
		// Get list of players that are currently online.
		PlayerList playerList = server.getPlayerList();
		
		// Get reference to player for provided player name.
		ServerPlayer serverPlayer = playerList.getPlayerByName(playerName);
		
		// IF: Player was not found.
		if (serverPlayer == null) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"Player " + playerName + " could not be found!"
			);
			
			// Send error message.
			source.sendFailure(message);

			// Return error code.
			return -1;
		}

		// Get position of lower player block.
		BlockPos playerPos = serverPlayer.blockPosition();
		
		// Get X, Y, Z coordinates of block position.
		int x = playerPos.getX();
		int y = playerPos.getY();
		int z = playerPos.getZ();

		// Get reference to a level where player is located.
		ServerLevel level = serverPlayer.serverLevel();

		// Get dimension in which level is located.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get resource location for dimension.
		ResourceLocation dimensionResLoc = dimension.location();
		
		// Get name of dimension and clear minecraft: prefix.
		String dimensionName = dimensionResLoc.toString().replace("minecraft:", "");
		
		// Send message to console.
		source.sendSuccess(() -> {

			// Create message to be displayed in console.		
			MutableComponent message = Component.translatable(
				"Player " + playerName + " is located at: " + x + " " + y + " " + z + " (" + dimensionName + ")"
			);
			
			return message;
			
		}, true);
		
		// Indicate success (1 = true).
		return 1;
	}
}
