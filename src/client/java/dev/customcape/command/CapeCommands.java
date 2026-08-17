package dev.customcape.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.customcape.CapeCatalog;
import dev.customcape.CapeManager;
import dev.customcape.CustomCape;
import dev.customcape.gui.CapeSelectScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public final class CapeCommands {
	private CapeCommands() {
	}

	private static final SuggestionProvider<FabricClientCommandSource> CAPE_SUGGESTIONS = (ctx, builder) -> {
		for (CapeCatalog.Entry entry : CapeManager.get().allEntries()) {
			builder.suggest(entry.id());
		}
		return builder.buildFuture();
	};

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> dispatcher.register(
			literal("customcape")
				.then(literal("reload").executes(ctx -> {
					CapeManager.get().reloadCustom();
					int count = CapeManager.get().textures().customEntries().size();
					ctx.getSource().sendFeedback(Component.translatable("command.customcape.reloaded", count));
					return count;
				}))
				.then(literal("clear").executes(ctx -> {
					CapeManager.get().select(CustomCape.VANILLA_ID);
					ctx.getSource().sendFeedback(Component.translatable("command.customcape.cleared"));
					return 1;
				}))
				.then(literal("list").executes(ctx -> {
					for (CapeCatalog.Entry entry : CapeManager.get().allEntries()) {
						ctx.getSource().sendFeedback(Component.literal(entry.id() + " — " + entry.displayName()));
					}
					return 1;
				}))
				.then(literal("set")
					.then(argument("id", StringArgumentType.greedyString())
						.suggests(CAPE_SUGGESTIONS)
						.executes(ctx -> {
							String id = StringArgumentType.getString(ctx, "id");
							var entry = CapeManager.get().findEntry(id);
							if (entry.isEmpty()) {
								ctx.getSource().sendError(Component.translatable("command.customcape.unknown", id));
								return 0;
							}
							CapeManager.get().select(id);
							ctx.getSource().sendFeedback(Component.translatable("command.customcape.set", entry.get().displayName()));
							return 1;
						})))
				.executes(ctx -> {
					Minecraft client = Minecraft.getInstance();
					client.schedule(() -> client.gui.setScreen(new CapeSelectScreen(client.gui.screen())));
					return 1;
				})
		));
	}
}
