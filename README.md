# Custom Cape

Clientside Fabric mod that lets you wear any bundled official cape — or your own **64×32** custom cape — without changing your Mojang account.

| Game version | Project path | Fabric API |
|--------------|--------------|------------|
| **26.2** | repo root | `0.157.0+26.2` |
| **26.1.2** | [`26.1.2/`](26.1.2/) | `0.155.2+26.1.2` |

Capes only appear on your client (and on other players if you enable that in config). They are **not** synced to the server or to players without the mod.

## Requirements (26.2)

| Dependency | Version |
|------------|---------|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3+ |
| Fabric API | matching 26.2 |
| Java | 25+ |

For 26.1.2, see [`26.1.2/README.md`](26.1.2/README.md).

## Install

1. Install Fabric for your Minecraft version.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api).
3. Put the matching jar in `.minecraft/mods/`:
   - 26.2: `build/libs/customcape-1.0.0.jar` (from repo root)
   - 26.1.2: `26.1.2/build/libs/customcape-1.0.0+26.1.2.jar`
4. Launch the game.

Build:

```bash
# 26.2
./gradlew build

# 26.1.2
cd 26.1.2 && ./gradlew build
```

## Usage

| Action | How |
|--------|-----|
| Open cape menu | **K** (rebindable) or `/customcape` |
| Equip an official cape | Cape Selector → Official Capes |
| Use your real account cape | **Account Cape (Vanilla)** |
| Hide cape | **No Cape** |
| Equip a custom PNG | See [Custom capes](#custom-capes) |

Elytra use the same texture as the selected cape (vanilla behavior).

## Custom capes

Textures must be **64×32** PNGs in the standard Minecraft cape UV layout (same as SkinMC / official cape files).

1. Launch once. The mod creates:
   - `config/customcape/cape_template.png` — blank layout to edit
   - `config/customcape/custom/` — drop finished capes here
   - `config/customcape/README.txt`
2. Edit a copy of the template (keep **64×32**).
3. Save as something like `my_cape.png` into `custom/`.
4. Press **Reload Custom Capes** in the menu, or run `/customcape reload`.
5. Select it under **Custom Capes**.

Invalid sizes are skipped and logged.

### UV layout (quick reference)

```
64 × 32 cape texture
┌────────────────┬────┐
│  Cape faces    │Edge│
│  (front/back)  │ /  │
│                │Ely.│
└────────────────┴────┘
```

## Commands

```
/customcape              Open the cape selector
/customcape list         List all cape ids
/customcape set <id>     Equip a cape by id
/customcape clear        Restore account (vanilla) cape
/customcape reload       Rescan config/customcape/custom/
```

Examples:

```
/customcape set minecon_2011
/customcape set custom:my_cape
/customcape clear
```

## Config

`config/customcape/config.json`:

```json
{
  "selectedCape": "vanilla",
  "applyToSelf": true,
  "applyToOthers": false
}
```

| Key | Meaning |
|-----|---------|
| `selectedCape` | Cape id (`vanilla`, `none`, an official id, or `custom:filename`) |
| `applyToSelf` | Override your own cape |
| `applyToOthers` | Override other players’ capes **on your client** with *your* selected cape |

## Bundled official capes

All of these are packaged inside the jar under `assets/customcape/textures/cape/`:

| Id | Name |
|----|------|
| `minecon_2011` | MINECON 2011 |
| `minecon_2012` | MINECON 2012 |
| `minecon_2013` | MINECON 2013 |
| `minecon_2015` | MINECON 2015 |
| `minecon_2016` | MINECON 2016 |
| `minecon_2019` | MINECON Live 2019 |
| `mojang` | Mojang |
| `mojang_classic` | Mojang Classic |
| `mojang_studios` | Mojang Studios |
| `mojang_office` | Mojang Office |
| `migrator` | Migrator |
| `vanilla_cape` | Vanilla |
| `cherry_blossom` | Cherry Blossom |
| `pan` | Pan |
| `followers` | Follower's |
| `purple_heart` | Purple Heart |
| `bacon` | Bacon |
| `birthday` | Birthday |
| `christmas_2010` | Christmas 2010 |
| `new_year_2011` | New Year 2011 |
| `cobalt` | Cobalt |
| `scrolls` | Scrolls Champion |
| `translator` | Translator |
| `chinese_translator` | Chinese Translator |
| `mapmaker` | Map Maker |
| `moderator` | Moderator |
| `millionth` | Millionth Customer |
| `prismarine` | Prismarine |
| `turtle` | Turtle |
| `cheapshot` | Cheapsh0t |
| `dannybstyle` | dannyBstyle |
| `julianclark` | JulianClark |
| `mrmessiah` | MrMessiah |
| `experience` | Minecraft Experience |
| `mcc_15th` | MCC 15th Year |
| `anniversary_15` | 15th Anniversary |
| `home` | Home |
| `menace` | Menace |
| `common` | Common |

Special ids (not texture files):

| Id | Meaning |
|----|---------|
| `vanilla` | Real cape from your Microsoft/Mojang account |
| `none` | No cape |
| `custom:<name>` | PNG from `config/customcape/custom/<name>.png` |

## Building

```bash
# Minecraft 26.2 (this directory)
./gradlew build
# → build/libs/customcape-1.0.0.jar

# Minecraft 26.1.2
cd 26.1.2 && ./gradlew build
# → 26.1.2/build/libs/customcape-1.0.0+26.1.2.jar
```

## License

MIT — see [LICENSE](LICENSE).

Official cape artwork belongs to Mojang Studios / Microsoft and is included only for clientside cosmetic use with this mod.
