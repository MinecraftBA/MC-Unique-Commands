package ba.minecraft.uniquecommands.common.event.command;

import java.util.Set;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class CloneEventHandler {

	@SubscribeEvent()
	public static void onClone(final Clone event) {
		
	    // Get reference to original player.
		Player original = event.getOriginal();

		// Get reference to a level where player is.
		Level level = original.level();
		
		// IF: Code is executing on client side.
		if (level.isClientSide()) {
			return;
		}

		// Get reference to cloned player.
		Player clone = event.getEntity();

		// Get persisted data for original player.
		CompoundTag originalData = original.getPersistentData();
		
		// Get persisted data for cloned player.
		CompoundTag clonedData = clone.getPersistentData();
		
		// Get keys of all data stored for original player.
	    Set<String> keys = originalData.getAllKeys();
		
	    // Iterate through all keys.
	    for(String key : keys) {
	    	
	    	// IF: Key indicates it stored player home coordinates.
	    	if(key.startsWith(UniqueCommandsMod.MODID + ":home:")) {
	    		
	    		// IF: Key is for stored coordinates.
	    		if(key.endsWith(":coords")) {

	    			// Get coordinates.
		    		int[] coordinates = originalData.getIntArray(key);

		    		// Save them in persistent data of cloned player.
		    		clonedData.putIntArray(key, coordinates);
    			}
	    		
	    		// IF: Key is for stored dimension name.
	    		if(key.endsWith(":dim")) {

	    			// Get dimension name.
		    		String dimName = originalData.getString(key);

		    		// Save dimension name in persistent data of cloned player.
		    		clonedData.putString(key, dimName);
	    		}
	    	}
	    }
	}
}
