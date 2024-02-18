package ba.minecraft.uniquecommands.common.command.account;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.helper.ServerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public final class LoginCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("login")
				.then(
					Commands.argument("password", StringArgumentType.word())
					.executes(
						(context) -> {
							CommandSourceStack source = context.getSource();
							String password = StringArgumentType.getString(context, "password");
							return login(source, password);
						}
					)
				)
		);
		
	}
	
	private static int login(CommandSourceStack source, String password) throws CommandSyntaxException {

		// IF: Accounts are not enabled.
		if(!UniqueCommandsModConfig.ACCOUNTS_ENABLED) {
			// Create error message.
			MutableComponent message = Component.literal(
				"Command is not enabled. Hey, not my fault!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {

			// Create error message.
			MutableComponent message = Component.literal(
				"Server is running in online mode, login is not required!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;

		}
		
		// Get reference to player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// IF: Player is already logged in.
		if(PlayerManager.isLoggedIn(player)) {
			// Create error message.
			MutableComponent message = Component.literal(
				"You are already logged in!"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Indicate success (1 = true).
		return 1;
		
	}
	
}
