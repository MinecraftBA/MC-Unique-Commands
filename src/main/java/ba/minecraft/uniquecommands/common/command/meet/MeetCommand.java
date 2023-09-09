package ba.minecraft.uniquecommands.common.command.meet;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public class MeetCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("meet")
						.executes(
							(context) -> {
								CommandSourceStack source = context.getSource();
								return meet(source);
							}
						)
					);
			
	}
	
	private static int meet(CommandSourceStack source) throws CommandSyntaxException {
		ServerPlayer summoner = source.getPlayerOrException();
		BlockPos playerPos = summoner.blockPosition();
		ServerLevel level = summoner.serverLevel();
		// Get player coordinates.
		int x = playerPos.getX();
		int y = playerPos.getY();
		int z = playerPos.getZ();
		MinecraftServer server = source.getServer();
		PlayerList playerList = server.getPlayerList();
		List<ServerPlayer> listOfPlayers = playerList.getPlayers();
		int sizeOfList = listOfPlayers.size();
		if(sizeOfList == 1) {
			MutableComponent message = Component.literal(
					"There is no other players on this server."
			);
				
			// Send error message.
			source.sendFailure(message);
			
			return -1;
		}
		for(ServerPlayer player : listOfPlayers) {
			float yaw = player.getYRot();
			float pitch = player.getXRot();
			player.teleportTo(level, x, y, z, yaw, pitch);
			
		}
		
		return 1;
	}
}
