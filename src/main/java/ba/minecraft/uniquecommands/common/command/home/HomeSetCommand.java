package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class HomeSetCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("home")
				.then(
					Commands.literal("set")
						.then(
							Commands.argument("name", StringArgumentType.word())
								.executes(
									(context) -> {
										CommandSourceStack source = context.getSource();
										String locName = StringArgumentType.getString(context, "name");
										return setHome(source, locName);
									}
								)

						)
				)
		);
	}
	
	private static int setHome(CommandSourceStack source, String locName) throws CommandSyntaxException {

		// Get reference to player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Save current location to player data.
		PlayerManager.saveLocationData(player, locName);
		
		// Get position of lower player block.
		BlockPos playerPos = player.blockPosition();
		
		// Get X, Y, Z coordinates of block position.
		int x = playerPos.getX();
		int y = playerPos.getY();
		int z = playerPos.getZ();

		// Create message to be displayed in console.		
		MutableComponent message = Component.literal(
			"Home " + locName + " is set to: " + x + " " + y + " " + z + ""
		);

		// Send message to console.
		source.sendSuccess(() -> message, true);
		
		// Indicate success (1 = true).
		return 1;
		
	}
}
