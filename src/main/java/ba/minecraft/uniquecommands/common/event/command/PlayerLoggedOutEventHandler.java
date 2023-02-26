package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class PlayerLoggedOutEventHandler {

	@SubscribeEvent()
	public static void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {
		
		PlayerManager.setSeen(event.getEntity());

	}

}
