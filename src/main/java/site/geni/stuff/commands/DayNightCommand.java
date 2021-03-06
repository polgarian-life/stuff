package site.geni.stuff.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import site.geni.stuff.util.AutoAppendTextComponent;
import site.geni.stuff.util.AutoFormatTextComponent;

public class DayNightCommand {
	private final static TextComponent DAY_MESSAGE = new AutoAppendTextComponent(TextFormat.GOLD, "Time set to day. (", new AutoFormatTextComponent("0", TextFormat.GREEN), ")");
	private final static TextComponent NIGHT_MESSAGE = new AutoAppendTextComponent(TextFormat.GOLD, "Time set to night. (", new AutoFormatTextComponent("13000", TextFormat.GREEN), ")");


	public static void register() {
		/* register cycle command */
		ServerStartCallback.EVENT.register(
				server -> server.getCommandManager().getDispatcher().register(
						ServerCommandManager.literal("cycle").executes(
								context -> onCycleCommand(context)
						)
				)
		);

		/* register day command */
		ServerStartCallback.EVENT.register(
				server -> server.getCommandManager().getDispatcher().register(
						ServerCommandManager.literal("day").executes(
								context -> onDayCommand(context)
						)
				)
		);

		/* register night command */
		ServerStartCallback.EVENT.register(
				server -> server.getCommandManager().getDispatcher().register(
						ServerCommandManager.literal("night").executes(
								context -> onNightCommand(context)
						)
				)
		);
	}

	private static int onCycleCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final ServerPlayerEntity player = context.getSource().getPlayer();
		final long time = player.world.getTimeOfDay();

		if (time >= 12516) {
			player.world.setTimeOfDay(0);
			context.getSource().sendFeedback(DAY_MESSAGE, false);
		} else {
			player.world.setTimeOfDay(13000);
			context.getSource().sendFeedback(NIGHT_MESSAGE, false);
		}

		return 1;
	}

	private static int onDayCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final ServerPlayerEntity player = context.getSource().getPlayer();

		player.world.setTimeOfDay(0);
		context.getSource().sendFeedback(DAY_MESSAGE, false);

		return 1;
	}

	private static int onNightCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final ServerPlayerEntity player = context.getSource().getPlayer();

		player.world.setTimeOfDay(13000);
		context.getSource().sendFeedback(NIGHT_MESSAGE, false);

		return 1;
	}
}
