package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class LivingDeathEventHandler {
	
	@SubscribeEvent()
	public static void onLivingDeath(final LivingDeathEvent entity) {
		


	}
	
}
