package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public final class ChatHelper {

	public static void sendColoredMessage(ServerPlayer player, String message, ChatFormatting color)
	{
		MutableComponent component = Component.literal(message);

		component.setStyle(Style.EMPTY.withColor(color));

		player.sendSystemMessage(component);
		
	}
	
	public static void sendWarningMessage(ServerPlayer player, String message) {

		sendColoredMessage(player, message, ChatFormatting.YELLOW);

	}

	public static void sendDangerMessage(ServerPlayer player, String message) {

		sendColoredMessage(player, message, ChatFormatting.RED);

	}
}
