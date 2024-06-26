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

public final class PlayerDeathDataTable extends SavedData {

	private static final String KEY = "Players";
	
	private final List<PlayerDeathDataRow> rows;
	
	public PlayerDeathDataTable() {
		this(new ArrayList<PlayerDeathDataRow>());
	}
	
	public PlayerDeathDataTable(List<PlayerDeathDataRow> playersData) {
		this.rows = playersData;
	}
	
	public static PlayerDeathDataTable create() {
		return new PlayerDeathDataTable(new ArrayList<PlayerDeathDataRow>());
	}
	
    public static SavedData.Factory<PlayerDeathDataTable> factory() {
		return new SavedData.Factory<>(PlayerDeathDataTable::new, PlayerDeathDataTable::load, DataFixTypes.PLAYER);
	}
	
	public static PlayerDeathDataTable load(CompoundTag compoundTag, Provider provider) {
			
		// Load list of NBTs from server data.
		ListTag listTag = compoundTag.getList(KEY, Tag.TAG_COMPOUND);
		
		// Create new empty list that will hold all data.
		ArrayList<PlayerDeathDataRow> dataRows = new ArrayList<PlayerDeathDataRow>();
		
		// Iterate through all NBTs.
		for(Tag tag : listTag) {
			
			// Deserialize NBT back to regular object.
			PlayerDeathDataRow dataRow = PlayerDeathDataRow.deserialize((CompoundTag)tag);
			
			// Add object to array of data.
			dataRows.add(dataRow);
		}
		
		// Create new instance of saved data class and provide data that was loaded to it.
		return new PlayerDeathDataTable(dataRows);
	}
	
	public List<PlayerDeathDataRow> getRows(){
		return this.rows;
	}
	
	public void upsertDataRow(PlayerDeathDataRow newDataRow) {

		// Find existing log of player seen data based on username and UUID.
		Optional<PlayerDeathDataRow> searchResult = this.rows
				.stream()
				.filter($p -> $p.getPlayerId().equals(newDataRow.getPlayerId()))
				.findFirst();

		// IF: Log exists.
		if(searchResult.isPresent()) {
			
			// Extract the log.
			PlayerDeathDataRow existingDataRow = searchResult.get();

			existingDataRow.setBlockPos(newDataRow.getBlockPos());
			
		} else {
			
			// Add new log.
			this.rows.add(newDataRow);
		}
		
		// Set data to be dirty as changes have been made.
		this.setDirty();
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, Provider pRegistries) {
		
		// Create new NBT list.
		ListTag listTag = new ListTag();
		
		// Iterate through all player data.
		for(PlayerDeathDataRow dataRow : this.rows) {
			
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
