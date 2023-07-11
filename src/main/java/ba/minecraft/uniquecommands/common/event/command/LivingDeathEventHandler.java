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
		
		LivingEntity entity = event.getEntity();
		
		Level level = entity.level();
		
		
		if(level.isClientSide()) {
			
			return;
		}
		
		if(!(entity instanceof ServerPlayer)) {
			
			return;
		}
		
		ServerPlayer player = (ServerPlayer)entity;
		
		PlayerManager.saveDeathData(player);

	}
	
}
