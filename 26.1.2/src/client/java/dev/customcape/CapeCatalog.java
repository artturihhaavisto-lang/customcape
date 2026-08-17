package dev.customcape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Catalog of bundled official cape textures shipped with the mod.
 * Custom file-based capes are discovered at runtime by {@link CapeTextureManager}.
 */
public final class CapeCatalog {
	public record Entry(String id, String displayName, Kind kind) {
		public boolean isBundled() {
			return kind == Kind.OFFICIAL;
		}
	}

	public enum Kind {
		VANILLA,
		NONE,
		OFFICIAL,
		CUSTOM
	}

	private static final Map<String, Entry> OFFICIAL = new LinkedHashMap<>();

	static {
		add("minecon_2011", "MINECON 2011");
		add("minecon_2012", "MINECON 2012");
		add("minecon_2013", "MINECON 2013");
		add("minecon_2015", "MINECON 2015");
		add("minecon_2016", "MINECON 2016");
		add("minecon_2019", "MINECON Live 2019");
		add("mojang", "Mojang");
		add("mojang_classic", "Mojang Classic");
		add("mojang_studios", "Mojang Studios");
		add("mojang_office", "Mojang Office");
		add("migrator", "Migrator");
		add("vanilla_cape", "Vanilla");
		add("cherry_blossom", "Cherry Blossom");
		add("pan", "Pan");
		add("followers", "Follower's");
		add("purple_heart", "Purple Heart");
		add("bacon", "Bacon");
		add("birthday", "Birthday");
		add("christmas_2010", "Christmas 2010");
		add("new_year_2011", "New Year 2011");
		add("cobalt", "Cobalt");
		add("scrolls", "Scrolls Champion");
		add("translator", "Translator");
		add("chinese_translator", "Chinese Translator");
		add("mapmaker", "Map Maker");
		add("moderator", "Moderator");
		add("millionth", "Millionth Customer");
		add("prismarine", "Prismarine");
		add("turtle", "Turtle");
		add("cheapshot", "Cheapsh0t");
		add("dannybstyle", "dannyBstyle");
		add("julianclark", "JulianClark");
		add("mrmessiah", "MrMessiah");
		add("experience", "Minecraft Experience");
		add("mcc_15th", "MCC 15th Year");
		add("anniversary_15", "15th Anniversary");
		add("home", "Home");
		add("menace", "Menace");
		add("common", "Common");
	}

	private CapeCatalog() {
	}

	private static void add(String id, String name) {
		OFFICIAL.put(id, new Entry(id, name, Kind.OFFICIAL));
	}

	public static Entry vanilla() {
		return new Entry(CustomCape.VANILLA_ID, "Account Cape (Vanilla)", Kind.VANILLA);
	}

	public static Entry none() {
		return new Entry(CustomCape.NONE_ID, "No Cape", Kind.NONE);
	}

	public static List<Entry> official() {
		return Collections.unmodifiableList(new ArrayList<>(OFFICIAL.values()));
	}

	public static Optional<Entry> official(String id) {
		return Optional.ofNullable(OFFICIAL.get(id));
	}

	public static Optional<Entry> resolveBuiltIn(String id) {
		if (CustomCape.VANILLA_ID.equals(id)) {
			return Optional.of(vanilla());
		}
		if (CustomCape.NONE_ID.equals(id)) {
			return Optional.of(none());
		}
		return official(id);
	}
}
