package ba.minecraft.uniquecommands.common.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

public class PlayersDeadSavedData extends SavedData {

	private static final String KEY = "Players";
	
	private final List<PlayerDeadData> playersData;
	
	public PlayersDeadSavedData(List<PlayerDeadData> playersData) {
		this.playersData = playersData;
	}
	
	public static PlayersDeadSavedData create() {
		return new PlayersDeadSavedData(new ArrayList<PlayerDeadData>());
	}
	
	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		
		// Create new NBT list.
		ListTag listTag = new ListTag();
		
		// Iterate through all player data.
		for(PlayerDeadData playerData : this.playersData) {
			
			// Serialize player data to NBT.
			CompoundTag playerTag = playerData.serialize();

			// Add NBT to list.
			listTag.add(playerTag);
		}
		
		// Store all NBTs to server data.
		compoundTag.put(KEY, listTag);

		// Return server data back for further processing.
		return compoundTag;
	}
	
	public static PlayersDeadSavedData load(CompoundTag compoundTag) {
			
			// Load list of NBTs from server data.
			ListTag listTag = compoundTag.getList(KEY, Tag.TAG_COMPOUND);
			
			// Create new empty list that will hold all data.
			ArrayList<PlayerDeadData> playersData = new ArrayList<PlayerDeadData>();
			
			// Iterate through all NBTs.
			for(Tag tag : listTag) {
				
				// Deserialize NBT back to regular object.
				PlayerDeadData playerData = PlayerDeadData.deserialize((CompoundTag)tag);
				
				// Add object to array of data.
				playersData.add(playerData);
			}
			
			// Create new instance of saved data class and provide data that was loaded to it.
			return new PlayersDeadSavedData(playersData);
		}
	
	public List<PlayerDeadData> getPlayersData(){
		return this.playersData;
	}
	
	public void upsertPlayerData(PlayerDeadData playerData) {

		// Find existing log of player seen data based on username and UUID.
		Optional<PlayerDeadData> searchResult = this.playersData
				.stream()
				.filter($p -> $p.getPlayerId().equals(playerData.getPlayerId()))
				.findFirst();

		// IF: Log exists.
		if(searchResult.isPresent()) {
			
			// Extract the log.
			PlayerDeadData existing = searchResult.get();

			existing.setBlockPos(playerData.getBlockPos());
		} else {
			
			// Add new log.
			this.playersData.add(playerData);
		}
		
		// Set data to be dirty as changes have been made.
		this.setDirty();
	}
	
}	
