package ba.minecraft.uniquecommands.common.event.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.helper.ServerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class AccountEventHandler {

	@SubscribeEvent()
	public static void onCommand(final CommandEvent event) {

		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {
			
			// Do nothing.
			return;
		}

		// Get reference to command parsing results.
		ParseResults<CommandSourceStack> parseResults = event.getParseResults();

		// Get reference to a command execution context.
		CommandContextBuilder<CommandSourceStack> context = parseResults.getContext();

		// Get reference to command source.
		CommandSourceStack source = context.getSource();

		// Get reference to player.
		ServerPlayer player = source.getPlayer();

		// IF: Command was not executed by player.
		if(player == null) {
			
			// Do nothing.
			return;
		}
		
		// Get reference to command reader.
		ImmutableStringReader reader = parseResults.getReader();
		
		// Get full command.
		String commandString = reader.getString();

		// Split command to segments.
		String[] segments = commandString.split("\\s+");
		
		if(segments[0] != "login") {
			
		}
	}
	
	@SubscribeEvent()
	public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		
		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {
			
			// Do nothing.
			return;
		}
		
		// Get reference to player.
		Player player = event.getEntity();
		
		

	}
}
