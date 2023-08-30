package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.command.die.DieCommand;
import ba.minecraft.uniquecommands.common.command.graveback.GravebackCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeClearCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeDeleteCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeListCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeReturnCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeSetCommand;
import ba.minecraft.uniquecommands.common.command.roll.RollCommand;
import ba.minecraft.uniquecommands.common.command.seen.SeenCommand;
import ba.minecraft.uniquecommands.common.command.tpup.TpUpCommand;
import ba.minecraft.uniquecommands.common.command.where.WhereCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Bus.FORGE)
public final class RegisterCommandsEventHandler {

	@SubscribeEvent()
	public static void onRegisterCommands(final RegisterCommandsEvent event) {

		HomeSetCommand.register(event.getDispatcher());
		HomeReturnCommand.register(event.getDispatcher());
		HomeListCommand.register(event.getDispatcher());
		HomeClearCommand.register(event.getDispatcher());
		HomeDeleteCommand.register(event.getDispatcher());
		
		SeenCommand.register(event.getDispatcher());
		WhereCommand.register(event.getDispatcher());
		GravebackCommand.register(event.getDispatcher());
		DieCommand.register(event.getDispatcher());
		RollCommand.register(event.getDispatcher());
		TpUpCommand.register(event.getDispatcher());
		
	}
	
}
