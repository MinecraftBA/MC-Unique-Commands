package ba.minecraft.uniquecommands.common.core.data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;

public final class PlayerSeenDataRow {

	private final static String UUID_KEY = "UUID";
	private final static String NAME_KEY = "Name";
	private final static String TIMESTAMP_KEY = "Timestamp";
	
	private final UUID playerId;
	private final String playerName;

	private LocalDateTime timestamp;

	public PlayerSeenDataRow(LocalDateTime timestamp, UUID playerId, String playerName) {
		this.timestamp = timestamp;
		this.playerId = playerId;
		this.playerName = playerName;
	}
	
	public CompoundTag serialize() {
		CompoundTag compoundTag = new CompoundTag();

		// Get current zone offset.
		ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
		
		// Convert timestamp to epoch.
		long epochTimestamp = timestamp.toEpochSecond(zoneOffset);
		
		compoundTag.putLong(TIMESTAMP_KEY, epochTimestamp);
		compoundTag.putString(NAME_KEY, playerName);
		compoundTag.putUUID(UUID_KEY, playerId);
		return compoundTag;
	}

	public static PlayerSeenDataRow deserialize(CompoundTag compoundTag) {
		
		// Get stored local date when player was last seen.
		long epochTimestamp = compoundTag.getLong(TIMESTAMP_KEY);

		// Get current zone offset.
		ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();

		// Convert to timestamp.
		LocalDateTime timestamp = LocalDateTime.ofEpochSecond(epochTimestamp, 0, zoneOffset);
		
		// Get player UUID.
		UUID playerId = compoundTag.getUUID(UUID_KEY);

		// Get player name.
		String playerName = compoundTag.getString(NAME_KEY);
		
		// Create new instance of saved data.
		PlayerSeenDataRow savedData = new PlayerSeenDataRow(timestamp, playerId, playerName);
		
		return savedData;
	}
	
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimeStamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	public UUID getPlayerId() {
		return this.playerId;
	}

	public String getPlayerName() {
		return this.playerName;
	}
}
