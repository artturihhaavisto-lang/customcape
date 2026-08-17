package dev.customcape;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CapeConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private String selectedCape = CustomCape.VANILLA_ID;
	private boolean applyToSelf = true;
	private boolean applyToOthers = false;

	public String selectedCape() {
		return selectedCape;
	}

	public void setSelectedCape(String selectedCape) {
		this.selectedCape = selectedCape;
	}

	public boolean applyToSelf() {
		return applyToSelf;
	}

	public void setApplyToSelf(boolean applyToSelf) {
		this.applyToSelf = applyToSelf;
	}

	public boolean applyToOthers() {
		return applyToOthers;
	}

	public void setApplyToOthers(boolean applyToOthers) {
		this.applyToOthers = applyToOthers;
	}

	public static Path configDir() {
		return FabricLoader.getInstance().getConfigDir().resolve(CustomCape.MOD_ID);
	}

	public static Path configFile() {
		return configDir().resolve("config.json");
	}

	public static Path customDir() {
		return configDir().resolve("custom");
	}

	public static CapeConfig load() {
		CapeConfig config = new CapeConfig();
		Path file = configFile();
		if (!Files.isRegularFile(file)) {
			return config;
		}
		try (Reader reader = Files.newBufferedReader(file)) {
			JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
			if (json.has("selectedCape")) {
				config.selectedCape = json.get("selectedCape").getAsString();
			}
			if (json.has("applyToSelf")) {
				config.applyToSelf = json.get("applyToSelf").getAsBoolean();
			}
			if (json.has("applyToOthers")) {
				config.applyToOthers = json.get("applyToOthers").getAsBoolean();
			}
		} catch (Exception e) {
			CustomCape.LOGGER.warn("Failed to read config, using defaults", e);
		}
		return config;
	}

	public void save() {
		try {
			Files.createDirectories(configDir());
			JsonObject json = new JsonObject();
			json.addProperty("selectedCape", selectedCape);
			json.addProperty("applyToSelf", applyToSelf);
			json.addProperty("applyToOthers", applyToOthers);
			try (Writer writer = Files.newBufferedWriter(configFile())) {
				GSON.toJson(json, writer);
			}
		} catch (IOException e) {
			CustomCape.LOGGER.error("Failed to save config", e);
		}
	}
}
