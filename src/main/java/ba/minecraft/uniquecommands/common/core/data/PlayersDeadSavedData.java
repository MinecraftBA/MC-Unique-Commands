package ba.minecraft.uniquecommands.common.core.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
	
	
}	
