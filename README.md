# Gun Wick

マイクラで銃を使う殺し屋のボスです。  
A [TaCZ](https://github.com/MCModderAnchor/TACZ) addon for **Minecraft Forge 1.20.1**.

Gun Wick is an original assassin boss. Kill **Wick's Wolf** and he arrives on the spot.

## Requirements

- Minecraft **1.20.1**
- **Forge** 47.x
- **[TaCZ] Timeless and Classics Zero** 1.1.6+ (tested against 1.1.8-hotfix)

Put this jar and TaCZ in your `mods` folder. No other addons are required.

## How to fight him

1. Find **Wick's Wolf** in the Overworld. It looks like a vanilla wolf with a name tag.
2. It is neutral, and you can tame it with bones.
3. If **a player** kills it (wild or tamed), **Gun Wick** spawns immediately.
4. Chat: `Gun Wick が現れた` / `Gun Wick has appeared`
5. Repeatable.

Creative testing:

```
/summon gunwick:gun_wick
/summon gunwick:wicks_wolf
```

Spawn eggs are in the **Gun Wick** creative tab.

## Combat

| Phase | HP | Mid / long | Close |
| --- | --- | --- | --- |
| 1 | above 50% | Glock 17 | SPAS-12 |
| 2 | 50% and below | Minigun | SPAS-12 |

- Max health **500**
- Armor **30**
- Incoming damage is capped at **3** per hit
- Solo boss, no minions
- Boss bar only
- If a gun id is missing, SPAS-12 falls back to M870 and the minigun falls back to M249

## Drops

- Iron block x64
- Gold block x64
- Copper block x64
- Gunpowder x64
- Diamond block x64

## Config

`config/gunwick-common.toml`

- Boss health / armor / damage cap / phase 2 ratio / close-range distance
- Wolf spawn enabled, weight, min/max count
- Gun ids (Glock, SPAS-12, minigun, fallbacks)

## Build

JDK 17.

```
./gradlew build
```

The jar is written to `build/libs/gunwick-1.0.0.jar`.

## License

Code: GPL-3.0 (same family as TaCZ).  
Boss texture is the public Minecraft skin [2966f9277a040ad6](https://ja.namemc.com/skin/2966f9277a040ad6).
