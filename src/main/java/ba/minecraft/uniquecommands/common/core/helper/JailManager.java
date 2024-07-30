package ba.minecraft.uniquecommands.common.core.helper;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import ba.minecraft.uniquecommands.common.core.data.PlayerSeenDataRow;
import ba.minecraft.uniquecommands.common.core.data.PlayerSeenDataTable;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataRow;
import ba.minecraft.uniquecommands.common.core.data.jail.JailDataTable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class JailManager {
	
	public static void setJail(ServerLevel level, String name, BlockPos blockPos) {
				
		// Get resource key for the dimension of level.
		ResourceKey<Level> dimension = level.dimension();
		
		// Get location of dimension resource.
		ResourceLocation resLoc = dimension.location();
		
		// Get name of the dimension.
		String dimName = resLoc.toString();
		
		int x = blockPos.getX();
		int y = blockPos.getY();
		int z = blockPos.getZ();
		
		// Create saved data.
		JailDataRow jailData = new JailDataRow(name, dimName, x, y, z);

		// Get reference to server persistent data.
		DimensionDataStorage storage = level.getDataStorage();

		// Load players saved data.
		JailDataTable savedData = tryLoadPlayersSeenData(storage);

		// Insert or update data for specific jail.
		savedData.upsertJailData(jailData);

		// Save data to server.
		storage.set(SEENS_KEY, savedData);
	}
	
}
