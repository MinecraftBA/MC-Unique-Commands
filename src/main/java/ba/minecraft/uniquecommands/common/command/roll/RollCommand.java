package ba.minecraft.uniquecommands.common.command.roll;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
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
		ServerLevel level = source.getLevel();
		RandomSource random = level.getRandom();
		int roll = random.nextInt(maxValue);
		if(!(maxValue >= 0)) {
			
			// Create error message.
			MutableComponent message = Component.literal(
				"You can't type a negative integer"
			);
				
			// Send error message.
			source.sendFailure(message);

			return -1;
		}
		if(!(roll <= maxValue)) {
			return -1;
			
		}
		source.sendSuccess(() -> {

			// Create message to be displayed in console.		
			MutableComponent message = Component.literal(
					"You rolled "+ random + "."
				);
			

			return message;
			
		}, true);
		return 1;

		
	}
}
