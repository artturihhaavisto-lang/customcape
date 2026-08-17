package dev.customcape;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Loads custom 64x32 cape PNGs from {@code config/customcape/custom/}
 * and registers them with the texture manager.
 */
public final class CapeTextureManager {
	public static final String CUSTOM_PREFIX = "custom:";

	private final Map<String, CapeCatalog.Entry> customEntries = new LinkedHashMap<>();
	private final Map<String, ClientAsset.Texture> textures = new LinkedHashMap<>();

	public void initFolders() {
		try {
			Files.createDirectories(CapeConfig.customDir());
			copyTemplateIfMissing();
			Path readme = CapeConfig.configDir().resolve("README.txt");
			if (!Files.exists(readme)) {
				Files.writeString(readme, """
					Custom Cape — 64x32 template
					============================
					1. Copy cape_template.png (or any 64x32 Minecraft cape PNG).
					2. Edit it in any pixel editor (keep size 64x32).
					3. Save as .png into the custom/ folder.
					4. Press the cape menu key (default: K) or run /customcape reload.
					5. Select your cape from the Custom Capes section.

					UV layout (standard Minecraft cape):
					- Left block: cape front/back faces
					- Right column: cape edges + elytra wing texture
					""");
			}
		} catch (IOException e) {
			CustomCape.LOGGER.error("Failed to create config folders", e);
		}
	}

	private void copyTemplateIfMissing() {
		Path templateOut = CapeConfig.configDir().resolve("cape_template.png");
		if (Files.exists(templateOut)) {
			return;
		}
		FabricLoader.getInstance().getModContainer(CustomCape.MOD_ID).ifPresent(container -> {
			Optional<Path> inMod = container.findPath("assets/customcape/textures/cape_template.png");
			if (inMod.isPresent()) {
				try {
					Files.copy(inMod.get(), templateOut, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					CustomCape.LOGGER.warn("Could not copy cape template", e);
				}
			}
		});
	}

	public void reload() {
		clearDynamicTextures();
		customEntries.clear();
		textures.clear();

		Path dir = CapeConfig.customDir();
		if (!Files.isDirectory(dir)) {
			return;
		}

		try (var stream = Files.list(dir)) {
			List<Path> files = stream
				.filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png"))
				.sorted()
				.toList();
			for (Path file : files) {
				loadCustom(file);
			}
		} catch (IOException e) {
			CustomCape.LOGGER.error("Failed to scan custom capes", e);
		}

		CustomCape.LOGGER.info("Loaded {} custom cape(s)", customEntries.size());
	}

	private void loadCustom(Path file) {
		String fileName = file.getFileName().toString();
		String base = fileName.substring(0, fileName.length() - 4);
		String id = CUSTOM_PREFIX + sanitize(base);
		Identifier textureId = CustomCape.id("dynamic/" + sanitize(base));

		try (InputStream in = Files.newInputStream(file)) {
			NativeImage image = NativeImage.read(in);
			if (image.getWidth() != 64 || image.getHeight() != 32) {
				CustomCape.LOGGER.warn("Skipping {}: expected 64x32, got {}x{}", fileName, image.getWidth(), image.getHeight());
				image.close();
				return;
			}

			DynamicTexture dynamic = new DynamicTexture(() -> "customcape/" + base, image);
			Minecraft.getInstance().getTextureManager().register(textureId, dynamic);
			textures.put(id, new ClientAsset.DownloadedTexture(textureId, file.toUri().toString()));
			customEntries.put(id, new CapeCatalog.Entry(id, base, CapeCatalog.Kind.CUSTOM));
		} catch (Exception e) {
			CustomCape.LOGGER.error("Failed to load custom cape {}", fileName, e);
		}
	}

	private void clearDynamicTextures() {
		var textureManager = Minecraft.getInstance().getTextureManager();
		for (ClientAsset.Texture texture : textures.values()) {
			textureManager.release(texture.texturePath());
		}
	}

	private static String sanitize(String name) {
		return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9/._-]", "_");
	}

	public List<CapeCatalog.Entry> customEntries() {
		return Collections.unmodifiableList(new ArrayList<>(customEntries.values()));
	}

	public Optional<ClientAsset.Texture> getCustomTexture(String id) {
		return Optional.ofNullable(textures.get(id));
	}

	public ClientAsset.Texture getOfficialTexture(String id) {
		Identifier path = CustomCape.id("textures/cape/" + id + ".png");
		return new ClientAsset.ResourceTexture(CustomCape.id("cape/" + id), path);
	}

	public Optional<ClientAsset.Texture> resolveTexture(String id) {
		if (id == null || CustomCape.VANILLA_ID.equals(id) || CustomCape.NONE_ID.equals(id)) {
			return Optional.empty();
		}
		if (id.startsWith(CUSTOM_PREFIX)) {
			return getCustomTexture(id);
		}
		if (CapeCatalog.official(id).isPresent()) {
			return Optional.of(getOfficialTexture(id));
		}
		return Optional.empty();
	}
}
