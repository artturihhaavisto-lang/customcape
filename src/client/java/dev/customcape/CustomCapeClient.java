package dev.customcape;

import com.mojang.blaze3d.platform.InputConstants;
import dev.customcape.command.CapeCommands;
import dev.customcape.gui.CapeSelectScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class CustomCapeClient implements ClientModInitializer {
	private static KeyMapping openMenuKey;
	private static boolean loadedCustoms;

	@Override
	public void onInitializeClient() {
		CapeManager.init();

		KeyMapping.Category category = KeyMapping.Category.register(CustomCape.id("main"));
		openMenuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.customcape.open",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			category
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!loadedCustoms && client.getTextureManager() != null) {
				loadedCustoms = true;
				CapeManager.get().textures().initFolders();
				CapeManager.get().reloadCustom();
			}

			while (openMenuKey.consumeClick()) {
				if (client.gui.screen() == null) {
					client.gui.setScreen(new CapeSelectScreen(null));
				}
			}
		});

		CapeCommands.register();
		CustomCape.LOGGER.info("Custom Cape ready — press K or run /customcape");
	}
}
