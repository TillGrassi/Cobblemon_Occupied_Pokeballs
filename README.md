# Cobblemon Occupied Pokéballs

A [Cobblemon](https://cobblemon.com/) mod for Minecraft 1.21.1 (Fabric).

Made by: M3Till

Eject any party Pokémon as its physical Poké Ball item — hand it to a friend, stash it in a chest, or drop it on the ground. Press the same key with the item in your hand to reclaim it.

---

## Features

- **Eject** — press **T** to eject your currently selected party Pokémon as its caught ball
- **Reclaim** — hold the ball and press **T** again to add it back to your party (or PC if full)
- **Ball throwing blocked** — occupied balls cannot be thrown; they work only as carriers
- **Rich item info** — the ball shows the Pokémon's name, level, gender, type(s), nature, and a ✦ Shiny marker when applicable
- **Original ball preserved** — a Pokémon caught in a Master Ball drops as a Master Ball, etc.
- **Full data preserved** — IVs, EVs, moves, ability, nature, held item, nickname, marks — everything round-trips perfectly
- **Trading** — give or chest-transfer the ball to another player; when they reclaim it, the Pokémon is theirs
- **Drop on death** _(optional, off by default)_ — configure via ModMenu to drop party balls as items on death; respects the `keepInventory` gamerule

The keybind is rebindable under **Options → Controls → PokéCapsule**.

---

## Requirements

| Dependency             | Version                 |
| ---------------------- | ----------------------- |
| Minecraft              | 1.21.1                  |
| Fabric Loader          | ≥ 0.16.5                |
| Fabric API             | ≥ 0.102.0+1.21.1        |
| Fabric Language Kotlin | ≥ 1.13.10+kotlin.2.3.20 |
| Cobblemon (Fabric)     | ≥ 1.7.1+1.21.1          |

**Optional (for in-game config GUI):**

- [ModMenu](https://modrinth.com/mod/modmenu)
- [Cloth Config](https://modrinth.com/mod/cloth-config)

Without these, the mod works fully — edit `config/pokecapsule.json` by hand instead.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for 1.21.1
2. Drop these into your `mods/` folder:
   - `pokecapsule-<version>.jar`
   - Fabric API
   - Fabric Language Kotlin
   - Cobblemon
3. Launch with the Fabric profile

For servers: the mod must be installed on **both the server and every client**.

---

## Configuration

The config file is created automatically at `config/cobblemon_occupied_pokeballs.json` on first launch:

```json
{
  "dropPartyOnDeath": false
}
```

| Option             | Default | Description                                                                      |
| ------------------ | ------- | -------------------------------------------------------------------------------- |
| `dropPartyOnDeath` | `false` | Drop all party Pokémon as ball items on death. Skipped if `keepInventory` is on. |

If ModMenu and Cloth Config are installed, a **Config** button appears on PokéCapsule's entry in the Mods screen.

---

## Building from source

```
.\gradlew.bat build
```

The first run downloads Minecraft, mappings, Fabric, and Cobblemon — this takes a few minutes. On success the jar is at:

```
build/libs/pokecapsule-<version>.jar
```
