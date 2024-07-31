package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class LivingDeathEventHandler {
	
	@SubscribeEvent()
	public static void onLivingDeath(final LivingDeathEvent event) {
		
		// Get reference to entity that has died.
		LivingEntity entity = event.getEntity();
		
		// Get reference to level where entity has died.
		Level level = entity.level();
		
		// IF: Code is executing on client side.
		if(level.isClientSide()) {
			
			// Do nothing.
			return;
		}
		// IF: Entity is not player.
		if(!(entity instanceof ServerPlayer)) {
			
			// Do nothing.
			return;
		}

		// Cast entity to player.
		ServerPlayer player = (ServerPlayer)entity;
		
		// Save death information.
		PlayerManager.saveDeathData(player);
	}
	
}
