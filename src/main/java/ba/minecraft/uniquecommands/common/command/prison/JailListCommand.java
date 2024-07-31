package ba.minecraft.uniquecommands.common.command.prison;

import java.util.List;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.helper.JailsManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

public final class JailListCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("jail")
				.then(
					Commands.literal("list")
					.requires((source) -> {
						return source.hasPermission(UniqueCommandsModConfig.JAIL_OP_LEVEL);
					})
					.executes(
						(context) -> {
							CommandSourceStack source = context.getSource();
							return listJails(source);
						}
					)
				)
		);
		
	}
	
	private static int listJails(CommandSourceStack source) throws CommandSyntaxException {

		if(!UniqueCommandsModConfig.JAIL_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to a level where command was issued.
		ServerLevel level = source.getLevel();
		
		// Get list of all previously saved jails.
		List<JailDataRow> dataRows = JailsManager.getJails(level);
		
		// Iterate throw jails.
		for(JailDataRow dataRow : dataRows) {
			
			// Send confirmation message.
			source.sendSuccess(() -> {

				// Create success message.
				MutableComponent message = Component.translatable(
						"Jail " + dataRow.getName() + " is set to: " + dataRow.getPosX() + " " + dataRow.getPosY() + " " + dataRow.getPosZ() + " (" + dataRow.getDimension() + ")"
				);
				
				return message;
				
			}, true);
				
		}

		// Return success code.
		return 1;
	}
}
