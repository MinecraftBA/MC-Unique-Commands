package ba.minecraft.uniquecommands.common.command.roll;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public final class RollCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("roll")
				.then(
					Commands.argument("maxValue", IntegerArgumentType.integer())
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								int maxValue = IntegerArgumentType.getInteger(context, "maxValue");
								return roll(source, maxValue);
							}
						)
				)
		);
	}
	
	private static int roll(CommandSourceStack source, int maxValue) throws CommandSyntaxException {
		
		// IF: Max value is not at least 2.
		if(maxValue < 2) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"Max roll value must be greater or equal to 2."
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		
		// Get reference to a level where command was executed.
		ServerLevel level = source.getLevel();
		
		// Get reference to random generator.
		RandomSource random = level.getRandom();
		
		// Create random roll.
		int roll = random.nextInt(maxValue) + 1;

		// Get reference to a player that has typed the command.
		ServerPlayer player = source.getPlayerOrException();
		
		// Get player profile.
		GameProfile playerProfile = player.getGameProfile();
		
		// Get player name.
		String playerName = playerProfile.getName();
		
		// Create message to be displayed in console.		
		MutableComponent message = Component.literal(
			playerName + " rolled: "+ roll + "/" + maxValue
		);

		// Send message to all players.
		source.sendSystemMessage(message);
		
		return 1;
	}
}
