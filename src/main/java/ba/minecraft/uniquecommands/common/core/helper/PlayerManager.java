package ba.minecraft.uniquecommands.common.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;

import com.mojang.authlib.GameProfile;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.data.PlayerDeathDataRow;
import ba.minecraft.uniquecommands.common.core.data.PlayerSeenDataRow;
import ba.minecraft.uniquecommands.common.core.data.PlayerDeathDataTable;
import ba.minecraft.uniquecommands.common.core.data.PlayerSeenDataTable;
import ba.minecraft.uniquecommands.common.core.models.LocationData;
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
	
    private static final ConcurrentHashMap<UUID, Boolean> activeLogins = new ConcurrentHashMap<>();
    
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
		PlayerSeenDataRow playerData = new PlayerSeenDataRow(LocalDateTime.now(), playerId, playerName);

		// Get reference to server persistent data.
		DimensionDataStorage storage = serverLevel.getDataStorage();

		// Load players saved data.
		PlayerSeenDataTable savedData = tryLoadPlayersSeenData(storage);

		// Insert or update data for specific player.
		savedData.upsertPlayerData(playerData);

		// Save data to server.
		storage.set(SEENS_KEY, savedData);
	}		
	
	public static List<PlayerSeenDataRow> getSeen(ServerLevel serverLevel, String playerName) {
		
		// Get reference to level storage.
		DimensionDataStorage dataStorage = serverLevel.getDataStorage();
		
		// Load players saved data.
		PlayerSeenDataTable savedData = tryLoadPlayersSeenData(dataStorage);
		
		// Get data for all players.
		List<PlayerSeenDataRow> playersData = savedData.getDataRows();
		
		// Return only players that match the player name.
		return playersData.stream()
					      .filter(p -> p.getPlayerName().contentEquals(playerName))
					      .toList();
	}
	
	private static PlayerSeenDataTable tryLoadPlayersSeenData(DimensionDataStorage storage) {

		// Load saved data based on the key.
		PlayerSeenDataTable savedData = storage.get(PlayerSeenDataTable.factory(), SEENS_KEY);
		
		// IF: Data was never saved before.
		if(savedData == null) {
			savedData = PlayerSeenDataTable.create();
		}
		
		return savedData;
	}
	
	private static PlayerDeathDataTable loadPlayerDeathsDataTable(DimensionDataStorage dataStorage) {

		// Load saved deaths data table based on the key which is stored in deaths.dat file.
		PlayerDeathDataTable dataTable = dataStorage.get(PlayerDeathDataTable.factory(), DEATHS_KEY);
		
		// IF: Data table was never saved before / it does not exist.
		if(dataTable == null) {
			
			// Create data table for the first time and save it.
			dataTable = PlayerDeathDataTable.create();
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
		PlayerDeathDataTable dataTable = loadPlayerDeathsDataTable(dataStorage);

		// Insert or update data for specific player.
		dataTable.upsertDataRow(dataRow);

		// Save data to server.
		dataStorage.set(DEATHS_KEY, dataTable);
	}	
	
	public static Optional<PlayerDeathDataRow> loadDeathData(ServerLevel serverLevel, UUID playerId) {
		
		// Get reference to a level data storage.
		DimensionDataStorage dataStorage = serverLevel.getDataStorage();
		
		// Load data table with deaths of all players.
		PlayerDeathDataTable dataTable = loadPlayerDeathsDataTable(dataStorage);
		
		// Get data rows for all players.
		List<PlayerDeathDataRow> dataRows = dataTable.getRows();
		
		// Try to find player by his ID.
		Optional<PlayerDeathDataRow> searchResult =  dataRows.stream()
					      .filter($row -> $row.getPlayerId().equals(playerId))
					      .findFirst();
		
		// Return search result.
		return searchResult;
	}
	
	public static LocationData getPlayerLocation(ServerPlayer player) 
	{
		// Get position of lower player block.
		BlockPos playerPos = player.blockPosition();
		
		// Get reference to level at which player is.
		ServerLevel level = player.serverLevel();
		
		// Get resource key for the dimension of level.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get location of dimension resource.
		ResourceLocation dimensionResLoc = dimension.location();

		// Get dimension resource location identifier.
		String dimensionResId = dimensionResLoc.toString();
		
		// Create location instance.
		LocationData location = new LocationData(playerPos, dimensionResId);

		return location;
	}
	
	public static LocationData saveLocationData(ServerPlayer player, String locGroup, String locName) {

		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// Get current location information for player.
		LocationData location = getPlayerLocation(player);

		// Create key => uniquecommands:home:{locName}
		String key = getLocKey(locGroup, locName);
		
		// Save array of coordinates in persistent data with key uniquecommands:home:{locName}:coords
		data.putIntArray(key + ":coords", location.getCoords());
		
		// Save information about dimension in persistent data with key uniquecommands:home:{locName}:dim
		data.putString(key + ":dim", location.getDimensionResId());
		
		return location;
	}
	
	public static LocationData loadLocationData(ServerPlayer player, String locGroup, String locName) {

		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// Create key => uniquecommands:home:{locName}
		String key = getLocKey(locGroup, locName);

		// Retrieve coordinates by providing key to persistent data.
		int[] coordinates = data.getIntArray(key + ":coords");

		// IF: Coordinates were not saved previously.
		if (coordinates.length == 0) {
			
			// Exit as location does not exist.
			return null;
		}
		
		// Extract coordinates from array.
		int x = coordinates[0];
		int y = coordinates[1];
		int z = coordinates[2];

		// Create block position from coordinates.
		BlockPos blockPos = new BlockPos(x, y, z);

		// Get ID of resource location for dimension.
		String dimensionResId = data.getString(key + ":dim");

		// Create new location.
		LocationData location = new LocationData(blockPos, dimensionResId);
		
		return location;
	}

	private static String getLocKey(String locGroup, String locName) {
		return UniqueCommandsMod.MODID + ":" + locGroup + ":" + locName;
	}
	
	public static void savePassword(ServerPlayer player, String password) {

		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// GEnerate password hash.
		String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
		
		// Save player designated password.
		data.putString("password", passwordHash);

	}
	
	public static String loadPassword(ServerPlayer player) {

		// Get reference to personal persistent data of player.
		CompoundTag data = player.getPersistentData();

		// Load password hash.
		String passwordHash = data.getString("password");
		
		return passwordHash;
	}
	
	public static void setLoggedInStatus(ServerPlayer player, Boolean isLoggedIn) {

		// Get unique identifier of player.
		UUID playerId = player.getUUID();

		// Set logged in status.
		activeLogins.put(playerId, isLoggedIn);
	}
	
	public static Boolean isLoggedIn(ServerPlayer player) {
		
		// Get unique identifier of player.
		UUID playerId = player.getUUID();
		
		// IF: Player was not registered in active logins.
		if(!activeLogins.containsKey(playerId)) {
			return false;
		}
		
		// Get login 
		Boolean isLoggedIn = activeLogins.get(playerId);

		return isLoggedIn;
	}
	
}
