package ba.minecraft.uniquecommands.common.command.account;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

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
		
		// Indicate success (1 = true).
		return 1;
		
	}
	
}
