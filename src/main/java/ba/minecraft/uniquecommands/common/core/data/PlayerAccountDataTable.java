package ba.minecraft.uniquecommands.common.core.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public final class PlayerAccountDataTable extends SavedData {

	private static final String KEY = "Players";
	
	private final List<PlayerAccountDataRow> dataRows;
	
	public PlayerAccountDataTable(List<PlayerAccountDataRow> dataRows) {
		this.dataRows = dataRows;
	}
	
	public PlayerAccountDataTable() {
		this(new ArrayList<PlayerAccountDataRow>());
	}

	public static PlayerAccountDataTable create() {
		return new PlayerAccountDataTable(new ArrayList<PlayerAccountDataRow>());
	}
	
    public static SavedData.Factory<PlayerAccountDataTable> factory() {
		return new SavedData.Factory<>(PlayerAccountDataTable::new, PlayerAccountDataTable::load, DataFixTypes.PLAYER);
	}
    
	public static PlayerAccountDataTable load(CompoundTag compoundTag) {
		
		// Load list of NBTs from server data.
		ListTag listTag = compoundTag.getList(KEY, Tag.TAG_COMPOUND);
		
		// Create new empty list that will hold all data.
		ArrayList<PlayerAccountDataRow> dataRows = new ArrayList<PlayerAccountDataRow>();
		
		// Iterate through all NBTs.
		for(Tag tag : listTag) {
			
			// Deserialize NBT back to regular object.
			PlayerAccountDataRow dataRow = PlayerAccountDataRow.deserialize((CompoundTag)tag);
			
			// Add object to array of data.
			dataRows.add(dataRow);
		}
		
		// Create new instance of saved data class and provide data that was loaded to it.
		return new PlayerAccountDataTable(dataRows);
	}
	
	public List<PlayerAccountDataRow> getPlayersData(){
		return this.dataRows;
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, Provider pRegistries) {
		ListTag listTag = new ListTag();
		
		// Iterate through all player data.
		for(PlayerAccountDataRow dataRow : this.dataRows) {
			
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
