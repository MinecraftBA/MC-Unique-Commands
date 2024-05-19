package ba.minecraft.uniquecommands.common.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public final class PlayerSeenDataTable extends SavedData {

	private static final String KEY = "Players";
	
	private final List<PlayerSeenDataRow> dataRows;
	
	public PlayerSeenDataTable(List<PlayerSeenDataRow> dataRows) {
		this.dataRows = dataRows;
	}
	
	public PlayerSeenDataTable() {
		this(new ArrayList<PlayerSeenDataRow>());
	}
	
	public static PlayerSeenDataTable create() {
		return new PlayerSeenDataTable(new ArrayList<PlayerSeenDataRow>());
	}
	
    public static SavedData.Factory<PlayerSeenDataTable> factory() {
		return new SavedData.Factory<>(PlayerSeenDataTable::new, PlayerSeenDataTable::load, DataFixTypes.PLAYER);
	}
	
	public static PlayerSeenDataTable load(CompoundTag compoundTag) {
		
		// Load list of NBTs from server data.
		ListTag listTag = compoundTag.getList(KEY, Tag.TAG_COMPOUND);
		
		// Create new empty list that will hold all data.
		ArrayList<PlayerSeenDataRow> dataRows = new ArrayList<PlayerSeenDataRow>();
		
		// Iterate through all NBTs.
		for(Tag tag : listTag) {
			
			// Deserialize NBT back to regular object.
			PlayerSeenDataRow dataRow = PlayerSeenDataRow.deserialize((CompoundTag)tag);
			
			// Add object to array of data.
			dataRows.add(dataRow);
		}
		
		// Create new instance of saved data class and provide data that was loaded to it.
		return new PlayerSeenDataTable(dataRows);
	}
	
	public List<PlayerSeenDataRow> getDataRows(){
		return this.dataRows;
	}
	
	public void upsertPlayerData(PlayerSeenDataRow playerData) {

		// Find existing log of player seen data based on username and UUID.
		Optional<PlayerSeenDataRow> searchResult = this.dataRows
				.stream()
				.filter($playerDataRow -> $playerDataRow.getPlayerName().contentEquals(playerData.getPlayerName()) && $playerDataRow.getPlayerId().equals(playerData.getPlayerId()))
				.findFirst();

		// IF: Log exists.
		if(searchResult.isPresent()) {
			
			// Extract the log.
			PlayerSeenDataRow existing = searchResult.get();

			// Change the log timestamp.
			existing.setTimeStamp(playerData.getTimestamp());

		} else {
			
			// Add new log.
			this.dataRows.add(playerData);
		}
		
		// Set data to be dirty as changes have been made.
		this.setDirty();
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, Provider pRegistries) {
		ListTag listTag = new ListTag();
		
		// Iterate through all player data.
		for(PlayerSeenDataRow dataRow : this.dataRows) {
			
			// Serialize player data to NBT.
			CompoundTag playerTag = dataRow.serialize();

			// Add NBT to list.
			listTag.add(playerTag);
		}

		// Store all NBTs to server data.
		compoundTag.put(KEY, listTag);

		// Return server data back for further processing.
		return compoundTag;
	}
}
