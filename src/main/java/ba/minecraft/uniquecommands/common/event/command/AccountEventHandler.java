package ba.minecraft.uniquecommands.common.event.command;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.helper.ServerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
			
			// Do nothing. Account management is only for offline servers.
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
			
			// Do nothing. Account management is for offline servers only.
			return;
		}
		
		// Get reference to player.
		Player entity = event.getEntity();
		
		// Get reference to a level where code is executing.
		Level level = entity.level();
		
		// IF: Code is executing on client side.
		if(level.isClientSide())
		{
			// Do nothing.
			return;
		}
		
		// Cast entity to server player.
		ServerPlayer player = (ServerPlayer)entity;

		// Get password hash.
		String passwordHash = PlayerManager.loadPassword(player);
		
		if(passwordHash == null || passwordHash == "") {
			// TODO: Add code here that asks player to set password for the first time.
		} else {
			// TODO: Add code here that requires player to login.
			// Check if player was previously logged in.
		}
		
		
	}
}
