package ba.minecraft.uniquecommands.common.command.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
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
		
		// Get reference to a player that called the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get reference to player's persistent data.
		CompoundTag data = player.getPersistentData();

		// Create key => experimentalmod:home
		String key = UniqueCommandsMod.MODID + ":home:" + locName;
		
		String coordsKey = key + ":coords";
		String dimKey = key + ":dim";
		
		// IF: Entries were found in persistent data = location was saved previously.
		if(data.contains(coordsKey) && data.contains(dimKey)) {

			// Remove entries.
			data.remove(coordsKey);
			data.remove(dimKey);
			
			MutableComponent message = Component.translatable(
				"command."  + UniqueCommandsMod.MODID + ".home_delete.success", locName
			);
			
			source.sendSuccess(message, true);
			
			return 1;
		} else {

			MutableComponent message = Component.translatable(
				"command."  + UniqueCommandsMod.MODID + ".home_delete.failure", locName
			);
			
			source.sendFailure(message);
			
			return -1;
		}
	}
}
