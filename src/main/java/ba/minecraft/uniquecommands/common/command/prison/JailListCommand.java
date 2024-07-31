package ba.minecraft.uniquecommands.common.command.prison;

import java.util.List;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.helper.JailManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

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
		
		ServerLevel level = source.getLevel();
		
		List<JailDataRow> dataRows = JailManager.getJails(level);
		
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
