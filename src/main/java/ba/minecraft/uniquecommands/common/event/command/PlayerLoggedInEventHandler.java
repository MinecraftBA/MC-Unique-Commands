package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.core.helper.PlayerManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class PlayerLoggedInEventHandler {

	@SubscribeEvent()
	public static void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		
		// Get reference to player.
		Player player = event.getEntity();
		
		// Set player seen timestamp.
		PlayerManager.setSeen(player);

		
	}

}
