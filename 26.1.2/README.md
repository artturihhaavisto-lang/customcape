# Custom Cape (Minecraft 26.1.2)

Clientside Fabric mod for **Minecraft 26.1.2** that lets you wear any bundled official cape — or your own **64×32** custom cape — without changing your Mojang account.

> This folder is the **26.1.2** port. The repo root targets **26.2**.

Capes only appear on your client (and on other players if you enable that in config). They are **not** synced to the server or to players without the mod.

## Requirements

| Dependency | Version |
|------------|---------|
| Minecraft | 26.1.2 |
| Fabric Loader | 0.19.3+ |
| Fabric API | 0.155.2+26.1.2 (or matching) |
| Java | 25+ |

## Install

1. Install Fabric for Minecraft 26.1.2.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for 26.1.2.
3. Put `customcape-1.0.0+26.1.2.jar` in `.minecraft/mods/`.
4. Launch the game.

Build from this directory:

```bash
./gradlew build
```

Jar: `build/libs/customcape-1.0.0+26.1.2.jar`

## Usage

| Action | How |
|--------|-----|
| Open cape menu | **K** (rebindable) or `/customcape` |
| Equip an official cape | Cape Selector → Official Capes |
| Use your real account cape | **Account Cape (Vanilla)** |
| Hide cape | **No Cape** |
| Equip a custom PNG | See below |

### Custom capes

1. Launch once → creates `config/customcape/` with `cape_template.png`
2. Edit a **64×32** PNG and save into `config/customcape/custom/`
3. `/customcape reload` or **Reload Custom Capes**, then select it

### Commands

```
/customcape              open menu
/customcape list         list cape ids
/customcape set <id>     equip cape
/customcape clear        account cape
/customcape reload       rescan custom/
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

## Bundled official capes

39 official cape textures are packaged in the jar. See the root [README](../README.md) for the full id list.

## License

MIT — see [LICENSE](LICENSE).

Official cape artwork belongs to Mojang Studios / Microsoft.
