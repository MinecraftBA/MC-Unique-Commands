package ba.minecraft.uniquecommands.common.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.mojang.authlib.GameProfile;

import ba.minecraft.uniquecommands.common.core.data.PlayerSeenData;
import ba.minecraft.uniquecommands.common.core.data.PlayersSeenSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;

public final class PlayerManager {

	private static final String SEENS_KEY = "seens";

	private static final Map<UUID, Long> teleports = 
			new HashMap<UUID, Long>();
	
	public static void setTeleportTimestamp(UUID playerId) {
		teleports.put(playerId, LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset()));
	}
	
	public static long getTeleportOffset(UUID playerId) {
		if(teleports.containsKey(playerId)) {
			return LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset()) - teleports.get(playerId);
		} else {
			return Long.MAX_VALUE; // Equals to teleported long time ago.
		}
	}
	
	public static void setSeen(Player player) {

		// Get reference to a level where player was before logging out.
		Level level = player.getLevel();
		
		// IF: Code is executing on client side.
		if (level.isClientSide()) {
			return;
		}

		// Cast level to ServerLevel (since it is not client side.
		ServerLevel serverLevel = (ServerLevel)level;

		// Get UUID of player.
		UUID playerId = player.getUUID();
		
		// Get player profile.
		GameProfile playerProfile = player.getGameProfile();
		
		// Get name of player.
		String playerName = playerProfile.getName();
		
		// Create saved data.
		PlayerSeenData playerData = new PlayerSeenData(LocalDateTime.now(), playerId, playerName);

		// Get reference to server persistent data.
		DimensionDataStorage storage = serverLevel.getDataStorage();

		// Load players saved data.
		PlayersSeenSavedData savedData = tryLoadPlayersSavedData(storage);

		// Insert or update data for specific player.
		savedData.upsertPlayerData(playerData);

		// Save data to server.
		storage.set(SEENS_KEY, savedData);
	}
	
	public static List<PlayerSeenData> getSeen(ServerLevel serverLevel, String playerName) {
		
		// Get reference to level storage.
		DimensionDataStorage storage = serverLevel.getDataStorage();
		
		// Load players saved data.
		PlayersSeenSavedData savedData = tryLoadPlayersSavedData(storage);
		
		// Get data for all players.
		List<PlayerSeenData> playersData = savedData.getPlayersData();
		
		// Return only players that match the player name.
		return playersData.stream()
					      .filter(p -> p.getPlayerName().contentEquals(playerName))
					      .toList();
	}
	
	private static PlayersSeenSavedData tryLoadPlayersSavedData(DimensionDataStorage storage) {

		// Load saved data based on the key.
		PlayersSeenSavedData savedData = storage.get(PlayersSeenSavedData::load, SEENS_KEY);
		
		// IF: Data was never saved before.
		if(savedData == null) {
			savedData = PlayersSeenSavedData.create();
		}
		
		return savedData;
	}
}
