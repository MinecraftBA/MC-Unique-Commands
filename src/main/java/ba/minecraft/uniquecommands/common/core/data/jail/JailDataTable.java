package ba.minecraft.uniquecommands.common.core.data.jail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class JailDataTable extends SavedData {

	private static final String KEY = "Jails";
	
	private final List<JailDataRow> rows;
	
	public JailDataTable() {
		this(new ArrayList<JailDataRow>());
	}
	
	public JailDataTable(List<JailDataRow> jailsData) {
		this.rows = jailsData;
	}
	
	public static JailDataTable create() {
		return new JailDataTable(new ArrayList<JailDataRow>());
	}
	
    public static SavedData.Factory<JailDataTable> factory() {
		return new SavedData.Factory<>(JailDataTable::new, JailDataTable::load, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
	}
	
	public static JailDataTable load(CompoundTag compoundTag, Provider provider) {
			
		// Load list of NBTs from server data.
		ListTag listTag = compoundTag.getList(KEY, Tag.TAG_COMPOUND);
		
		// Create new empty list that will hold all data.
		ArrayList<JailDataRow> dataRows = new ArrayList<JailDataRow>();
		
		// Iterate through all NBTs.
		for(Tag tag : listTag) {
			
			// Deserialize NBT back to regular object.
			JailDataRow dataRow = JailDataRow.deserialize((CompoundTag)tag);
			
			// Add object to array of data.
			dataRows.add(dataRow);
		}
		
		// Create new instance of saved data class and provide data that was loaded to it.
		return new JailDataTable(dataRows);
	}
	
	public List<JailDataRow> getRows(){
		return this.rows;
	}
	
	public void upsertDataRow(JailDataRow newDataRow) {

		// Find existing log of player seen data based on username and UUID.
		Optional<JailDataRow> searchResult = this.rows
				.stream()
				.filter($p -> $p.getName().equals(newDataRow.getName()))
				.findFirst();

		// IF: Log exists.
		if(searchResult.isPresent()) {
			
			// Extract the log.
			JailDataRow existingDataRow = searchResult.get();

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
		for(JailDataRow dataRow : this.rows) {
			
			// Serialize player data to NBT.
			CompoundTag jailTag = dataRow.serialize();

			// Add NBT to list.
			listTag.add(jailTag);
		}
		
		// Store all NBTs to server data.
		compoundTag.put(KEY, listTag);

		// Return server data back for further processing.
		return compoundTag;
	}
	
}	

