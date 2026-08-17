package dev.customcape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CapeManager {
	private static CapeManager instance;

	private final CapeConfig config;
	private final CapeTextureManager textures;

	private CapeManager(CapeConfig config, CapeTextureManager textures) {
		this.config = config;
		this.textures = textures;
	}

	public static void init() {
		CapeConfig config = CapeConfig.load();
		CapeTextureManager textures = new CapeTextureManager();
		textures.initFolders();
		instance = new CapeManager(config, textures);
	}

	public static CapeManager get() {
		return instance;
	}

	public CapeConfig config() {
		return config;
	}

	public CapeTextureManager textures() {
		return textures;
	}

	public void reloadCustom() {
		textures.reload();
	}

	public void select(String id) {
		config.setSelectedCape(id);
		config.save();
	}

	public List<CapeCatalog.Entry> allEntries() {
		List<CapeCatalog.Entry> list = new ArrayList<>();
		list.add(CapeCatalog.vanilla());
		list.add(CapeCatalog.none());
		list.addAll(CapeCatalog.official());
		list.addAll(textures.customEntries());
		return list;
	}

	public Optional<CapeCatalog.Entry> findEntry(String id) {
		Optional<CapeCatalog.Entry> builtIn = CapeCatalog.resolveBuiltIn(id);
		if (builtIn.isPresent()) {
			return builtIn;
		}
		return textures.customEntries().stream().filter(e -> e.id().equals(id)).findFirst();
	}

	/**
	 * @return overridden skin, or null to keep vanilla skin as-is
	 */
	public @Nullable PlayerSkin overrideSkin(AbstractClientPlayer player, PlayerSkin original) {
		if (!shouldApply(player)) {
			return null;
		}

		String selected = config.selectedCape();
		if (CustomCape.VANILLA_ID.equals(selected)) {
			return null;
		}

		if (CustomCape.NONE_ID.equals(selected)) {
			return PlayerSkin.insecure(original.body(), null, null, original.model());
		}

		Optional<ClientAsset.Texture> cape = textures.resolveTexture(selected);
		if (cape.isEmpty()) {
			return null;
		}

		ClientAsset.Texture texture = cape.get();
		// Match vanilla: elytra reuses cape texture when no dedicated elytra is set.
		return PlayerSkin.insecure(original.body(), texture, texture, original.model());
	}

	private boolean shouldApply(AbstractClientPlayer player) {
		Minecraft client = Minecraft.getInstance();
		LocalPlayer self = client.player;
		boolean isSelf = self != null && self.getUUID().equals(player.getUUID());
		if (isSelf) {
			return config.applyToSelf();
		}
		return config.applyToOthers();
	}
}
