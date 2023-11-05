package ba.minecraft.uniquecommands.common.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.mojang.authlib.GameProfile;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.data.PlayerDeathDataRow;
import ba.minecraft.uniquecommands.common.core.data.PlayerSeenData;
import ba.minecraft.uniquecommands.common.core.data.PlayerDeathsDataTable;
import ba.minecraft.uniquecommands.common.core.data.PlayersSeenSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;

public final class PlayerManager {

	private static final String SEENS_KEY = "seens";
	
	private static final String DEATHS_KEY = "deaths";

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
		Level level = player.level();
		
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
		PlayersSeenSavedData savedData = tryLoadPlayersSeenData(storage);

		// Insert or update data for specific player.
		savedData.upsertPlayerData(playerData);

		// Save data to server.
		storage.set(SEENS_KEY, savedData);
	}		
	
	public static List<PlayerSeenData> getSeen(ServerLevel serverLevel, String playerName) {
		
		// Get reference to level storage.
		DimensionDataStorage dataStorage = serverLevel.getDataStorage();
		
		// Load players saved data.
		PlayersSeenSavedData savedData = tryLoadPlayersSeenData(dataStorage);
		
		// Get data for all players.
		List<PlayerSeenData> playersData = savedData.getPlayersData();
		
		// Return only players that match the player name.
		return playersData.stream()
					      .filter(p -> p.getPlayerName().contentEquals(playerName))
					      .toList();
	}
	
	private static PlayersSeenSavedData tryLoadPlayersSeenData(DimensionDataStorage storage) {

		// Load saved data based on the key.
		PlayersSeenSavedData savedData = storage.get(PlayersSeenSavedData.factory(), SEENS_KEY);
		
		// IF: Data was never saved before.
		if(savedData == null) {
			savedData = PlayersSeenSavedData.create();
		}
		
		return savedData;
	}
	
	private static PlayerDeathsDataTable loadPlayerDeathsDataTable(DimensionDataStorage dataStorage) {

		// Load saved deaths data table based on the key which is stored in deaths.dat file.
		PlayerDeathsDataTable dataTable = dataStorage.get(PlayerDeathsDataTable.factory(), DEATHS_KEY);
		
		// IF: Data table was never saved before / it does not exist.
		if(dataTable == null) {
			
			// Create data table for the first time and save it.
			dataTable = PlayerDeathsDataTable.create();
		}
		
		// Return data table.
		return dataTable;
		
	}
	
	public static void saveDeathData(Player player) {
		
		// Get reference to a level where player has died.
		Level level = player.level();
				
		// IF: Code is executing on client side.
		if (level.isClientSide()) {
			
			// Do nothing.
			return;
		}
		
		// Cast level to ServerLevel (since it is not client side).
		ServerLevel serverLevel = (ServerLevel)level;

		// Get UUID of player.
		UUID playerId = player.getUUID();
		
		// Get resource key for the dimension of level.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get location of dimension resource.
		ResourceLocation resLoc = dimension.location();
		
		// Get name of the dimension.
		String dimName = resLoc.toString();
		
		// Get current position of player.
		BlockPos playerPos = player.blockPosition();

		// Get coordinates of current position.
		int posX = playerPos.getX();
		int posY = playerPos.getY();
		int posZ = playerPos.getZ();
		
		// Create object that will hold information about player's death.
		PlayerDeathDataRow dataRow = new PlayerDeathDataRow(playerId,dimName,posX,posY,posZ);
		
		// Get reference to server persistent data.
		DimensionDataStorage dataStorage = serverLevel.getDataStorage();

		// Load saved death data for all players.
		PlayerDeathsDataTable dataTable = loadPlayerDeathsDataTable(dataStorage);

		// Insert or update data for specific player.
		dataTable.upsertDataRow(dataRow);

		// Save data to server.
		dataStorage.set(DEATHS_KEY, dataTable);
	}	
	
	public static Optional<PlayerDeathDataRow> loadDeathData(ServerLevel serverLevel, UUID playerId) {
		
		// Get reference to a level data storage.
		DimensionDataStorage dataStorage = serverLevel.getDataStorage();
		
		// Load data table with deaths of all players.
		PlayerDeathsDataTable dataTable = loadPlayerDeathsDataTable(dataStorage);
		
		// Get data rows for all players.
		List<PlayerDeathDataRow> dataRows = dataTable.getRows();
		
		// Try to find player by his ID.
		Optional<PlayerDeathDataRow> searchResult =  dataRows.stream()
					      .filter($row -> $row.getPlayerId().equals(playerId))
					      .findFirst();
		
		// Return search result.
		return searchResult;
	}
	
	public static void saveLocationData(ServerPlayer player, String locName) {

		// Get position of lower player block.
		BlockPos playerPos = player.blockPosition();
		
		// Get X, Y, Z coordinates of block position.
		int x = playerPos.getX();
		int y = playerPos.getY();
		int z = playerPos.getZ();
		
		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// Create key => experimentalmod:home
		String key = UniqueCommandsMod.MODID + ":home:" + locName;
		
		// Save array of coordinates in persistent data under key.
		data.putIntArray(key + ":coords", new int[] { x, y, z });
		
		// Get reference to level at which player is.
		ServerLevel level = player.serverLevel();
		
		// Get resource key for the dimension of level.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get location of dimension resource.
		ResourceLocation resLoc = dimension.location();
		
		// Save information about level.
		data.putString(key + ":dim", resLoc.toString());
	}
}
