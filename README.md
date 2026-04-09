# Cobblemon Occupied Pokéballs

A [Cobblemon](https://cobblemon.com/) mod for Minecraft 1.21.1 (Fabric).

Eject any party Pokémon as its physical Poké Ball item, hand it to a friend, stash it in a chest, place it on a shelf, or send it straight out into the world.

---

## Features

- **Eject** — press **T** to eject your currently selected party Pokémon as its caught ball
- **Reclaim** — hold the ball and press **T** to add it back to your party (or PC if full)
- **Send out from hand** — hold the ball and press **Shift+T** to add it to your party and immediately send it out with the full throw animation (only works if your party has space)
- **Pokéball Display** — right-click the top face of any solid block while holding an occupied ball to place it as a 3D display; reclaim by breaking it
- **Floating name label** — placed displays show the Pokémon's name (and ✦ if shiny) as a floating label; toggleable in config
- **Jade tooltip** — when looking at a placed ball, Jade shows the Pokémon's name, level, type(s), nature, and shiny status
- **Ball throwing blocked** — occupied balls cannot be thrown; they work only as carriers
- **Rich item tooltip** — the ball shows the Pokémon's name, level, gender, type(s), nature, and a ✦ Shiny marker when applicable
- **Original ball preserved** — a Pokémon caught in a Master Ball drops as a Master Ball, etc.
- **Full data preserved** — IVs, EVs, moves, ability, nature, held item, nickname, marks — everything round-trips perfectly
- **Trading** — give or chest-transfer the ball to another player; when they reclaim it, the Pokémon is theirs
- **Drop on death** _(optional, off by default)_ — configure via ModMenu to drop party balls as items on death; respects the `keepInventory` gamerule

All keybinds are rebindable under **Options → Controls → Cobblemon Occupied Pokéballs**.

---

## Requirements

| Dependency             | Version                 |
| ---------------------- | ----------------------- |
| Minecraft              | 1.21.1                  |
| Fabric Loader          | ≥ 0.16.5                |
| Fabric API             | ≥ 0.102.0+1.21.1        |
| Fabric Language Kotlin | ≥ 1.13.10+kotlin.2.3.20 |
| Cobblemon (Fabric)     | ≥ 1.7.3+1.21.1          |

**Optional:**

- [ModMenu](https://modrinth.com/mod/modmenu) + [Cloth Config](https://modrinth.com/mod/cloth-config) — in-game config GUI
- [Jade](https://modrinth.com/mod/jade) — tooltip when looking at a placed Pokéball Display

Without ModMenu/Cloth Config the mod works fully — edit `config/cobblemon_occupied_pokeballs.json` by hand instead.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for 1.21.1
2. Drop these into your `mods/` folder:
   - `cobblemon-occupied-pokeballs-<version>.jar`
   - Fabric API
   - Fabric Language Kotlin
   - Cobblemon
3. Launch with the Fabric profile

For servers: the mod must be installed on **both the server and every client**.

---

## Configuration

The config file is created automatically at `config/cobblemon_occupied_pokeballs.json` on first launch:

| Option             | Default | Description                                                                      |
| ------------------ | ------- | -------------------------------------------------------------------------------- |
| `dropPartyOnDeath` | `false` | Drop all party Pokémon as ball items on death. Skipped if `keepInventory` is on. |

If ModMenu and Cloth Config are installed, a **Config** button appears on the mod's entry in the Mods screen.

---

## Building from source

```
.\gradlew.bat build
```

The first run downloads Minecraft, mappings, Fabric, and Cobblemon — this takes a few minutes. On success the jar is at:

```
build/libs/cobblemon-occupied-pokeballs-<version>.jar
```
