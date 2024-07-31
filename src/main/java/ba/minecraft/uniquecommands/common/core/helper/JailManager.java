package ba.minecraft.uniquecommands.common.core.helper;

import java.util.List;
import java.util.Optional;

import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataTable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class JailManager {
	
	private static final String JAILS_KEY = "jails";
	
	private static JailDataTable loadJailDataTable(DimensionDataStorage dataStorage) {

		// Load saved deaths data table based on the key which is stored in deaths.dat file.
		JailDataTable dataTable = dataStorage.get(JailDataTable.factory(), JAILS_KEY);
		
		// IF: Data table was never saved before / it does not exist.
		if(dataTable == null) {
			
			// Create data table for the first time and save it.
			dataTable = JailDataTable.create();
		}
		
		// Return data table.
		return dataTable;
		
	}
	
	public static void setJail(ServerLevel level, String name, BlockPos blockPos) {
				
		// Get resource ID of the dimension.
		String dimensionResId = LocationHelper.getDimensionId(level);
		
		int x = blockPos.getX();
		int y = blockPos.getY();
		int z = blockPos.getZ();
		
		// Create saved data.
		JailDataRow jailData = new JailDataRow(name, dimensionResId, x, y, z);

		// Get reference to server persistent data.
		DimensionDataStorage storage = ServerHelper.getDataStorage(level);

		// Load players saved data.
		JailDataTable dataTable = loadJailDataTable(storage);

		// Insert or update data for specific jail.
		dataTable.upsertDataRow(jailData);

		// Save data to server.
		storage.set(JAILS_KEY, dataTable);
	}
	
	
	public static Optional<JailDataRow> getJail(ServerLevel serverLevel, String name) {
		
		// Get reference to level storage.
		DimensionDataStorage dataStorage = ServerHelper.getDataStorage(serverLevel);
		
		// Load players saved data.
		JailDataTable savedData = loadJailDataTable(dataStorage);
		
		// Get data for all jails.
		List<JailDataRow> data = savedData.getRows();
		
		// Return only jails that match the jail name.
		Optional<JailDataRow> dataRow = data.stream()
					      .filter(p -> p.getName().contentEquals(name))
					      .findFirst();
		return dataRow;
	}
	
	public static List<JailDataRow> getJails(ServerLevel serverLevel) {
		
		// Get reference to level storage.
		DimensionDataStorage dataStorage = ServerHelper.getDataStorage(serverLevel);
		
		// Load players saved data.
		JailDataTable savedData = loadJailDataTable(dataStorage);
		
		// Get data for all jails.
		List<JailDataRow> data = savedData.getRows();
		
		return data;
	}
	
}
