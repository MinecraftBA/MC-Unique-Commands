package ba.minecraft.uniquecommands.common.event.command;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import ba.minecraft.uniquecommands.common.command.die.DieCommand;
import ba.minecraft.uniquecommands.common.command.graveback.GravebackCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeClearCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeDeleteCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeListCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeReturnCommand;
import ba.minecraft.uniquecommands.common.command.home.HomeSetCommand;
import ba.minecraft.uniquecommands.common.command.jail.JailListCommand;
import ba.minecraft.uniquecommands.common.command.jail.JailRemoveCommand;
import ba.minecraft.uniquecommands.common.command.jail.JailSendCommand;
import ba.minecraft.uniquecommands.common.command.jail.JailSetCommand;
import ba.minecraft.uniquecommands.common.command.meet.MeetCommand;
import ba.minecraft.uniquecommands.common.command.roll.RollCommand;
import ba.minecraft.uniquecommands.common.command.seen.SeenCommand;
import ba.minecraft.uniquecommands.common.command.tp.TpBottomCommand;
import ba.minecraft.uniquecommands.common.command.tp.TpDownCommand;
import ba.minecraft.uniquecommands.common.command.tp.TpTopCommand;
import ba.minecraft.uniquecommands.common.command.tp.TpUpCommand;
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
		
		JailSetCommand.register(event.getDispatcher());
		JailRemoveCommand.register(event.getDispatcher());
		JailListCommand.register(event.getDispatcher());
		JailSendCommand.register(event.getDispatcher());
		
		SeenCommand.register(event.getDispatcher());
		WhereCommand.register(event.getDispatcher());
		GravebackCommand.register(event.getDispatcher());
		DieCommand.register(event.getDispatcher());
		RollCommand.register(event.getDispatcher());
		TpUpCommand.register(event.getDispatcher());
		TpDownCommand.register(event.getDispatcher());
		TpBottomCommand.register(event.getDispatcher());
		TpTopCommand.register(event.getDispatcher());
		MeetCommand.register(event.getDispatcher());
		
	}
	
}
