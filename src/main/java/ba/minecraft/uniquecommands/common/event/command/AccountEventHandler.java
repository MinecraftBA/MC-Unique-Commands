package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsModConfig;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.ChatHelper;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import ba.minecraft.uniquecommands.common.core.helper.ServerHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
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
		
		// IF: Account management is not enabled.
		if(!UniqueCommandsModConfig.ACCOUNTS_ENABLED) {
			
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
		
		// IF: Command is not login.
		if(segments[0] != "login") {
			
			// IF: Player was not logged in.
			if(!PlayerManager.isLoggedIn(player)) {
				
				// Stop execution of command.
				event.setCanceled(true);
			}
			
		}
	}
	
	@SubscribeEvent()
	public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		
		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {
			
			// Do nothing. Account management is for offline servers only.
			return;
		}
		
		// IF: Account management is not enabled.
		if(!UniqueCommandsModConfig.ACCOUNTS_ENABLED) {
			
			// Do nothing.
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
		
		// IF: Password was not set previously - it's a new player.
		if(passwordHash == null || passwordHash == "") {
			
			ChatHelper.sendWarningMessage(player, "To play on this server, you need to create an account.");
			ChatHelper.sendWarningMessage(player, "Please set your password with command: /login your_password");
			ChatHelper.sendWarningMessage(player, "You have 30 seconds to set the password.");
			ChatHelper.sendWarningMessage(player, "Make sure to write your password down and to keep it safe!");

			
		} else {

			ChatHelper.sendWarningMessage(player, "Please login with command: /login your_password");
			ChatHelper.sendWarningMessage(player, "You have 30 seconds to complete your login!");
		
		}

		// Clear login status to false.
		PlayerManager.setLoggedInStatus(player, false);

		// Start login timeout counter for player.
		PlayerManager.startCounter(player);
	}
	
	@SubscribeEvent()
	public static void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {

		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {
			
			// Do nothing. Account management is for offline servers only.
			return;
		}
		
		// IF: Account management is not enabled.
		if(!UniqueCommandsModConfig.ACCOUNTS_ENABLED) {
			
			// Do nothing.
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
		
		// Set login status to false.
		PlayerManager.setLoggedInStatus(player, false);
	}
	
	@SubscribeEvent()
	public static void onPlayerTick(final PlayerTickEvent event) {

		
		// IF: Event was fired on client side.
		if(event.side == LogicalSide.CLIENT) {
			return;
		}

		// IF: Server is running in online mode.
		if(ServerHelper.isOnlineMode()) {
			return;
		}
		
		// Get reference to player and cast it as ServerPlayer.
		ServerPlayer player = (ServerPlayer)event.player;
		
		// IF: Player is already logged in,
		if(PlayerManager.isLoggedIn(player)) {

			// Do nothing.
			return;
		}
		
		// Get game mode for player.
		ServerPlayerGameMode gameMode = player.gameMode;

		// Incomplete
	}
}
