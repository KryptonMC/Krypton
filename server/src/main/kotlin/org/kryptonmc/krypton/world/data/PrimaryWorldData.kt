/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.data

import kotlinx.collections.immutable.persistentSetOf
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.Difficulties
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound
import java.nio.file.Path
import java.time.Instant
import java.util.UUID

class PrimaryWorldData(
    override val name: String,
    override val folder: Path,
    override var gameMode: GameMode,
    override var difficulty: Difficulty,
    override var isHardcore: Boolean,
    override var gameRules: KryptonGameRuleHolder,
    override val generationSettings: WorldGenerationSettings,
    override var spawnX: Int = 0,
    override var spawnY: Int = 0,
    override var spawnZ: Int = 0,
    override var spawnAngle: Float = 0F,
    override var time: Long = 0L,
    override var dayTime: Long = 0L,
    override var clearWeatherTime: Int = 0,
    override var isRaining: Boolean = false,
    override var rainTime: Int = 0,
    override var isThundering: Boolean = false,
    override var thunderTime: Int = 0,
    override var isInitialized: Boolean = false,
    override var wanderingTraderSpawnChance: Int = 0,
    override var wanderingTraderSpawnDelay: Int = 0,
    override var wanderingTraderId: UUID? = null,
    private var customBossEvents: CompoundTag? = null,
    private var enderDragonFightData: CompoundTag = CompoundTag.EMPTY,
    private val serverBrands: Set<String> = persistentSetOf()
) : WorldData {

    fun save(): CompoundTag = compound {
        compound("Data") {
            compound("Krypton") {
                putString("Version", KryptonPlatform.version)
            }
            putInt("DataVersion", KryptonPlatform.worldVersion)
            putInt("version", ANVIL_VERSION_ID)
            compound("Version") {
                putInt("Id", KryptonPlatform.worldVersion)
                putString("Name", KryptonPlatform.minecraftVersion)
                putBoolean("Snapshot", !KryptonPlatform.isStableMinecraft)
            }
            putString("LevelName", name)
            putInt("GameType", gameMode.ordinal)
            putByte("Difficulty", difficulty.ordinal.toByte())
            putBoolean("hardcore", isHardcore)
            put("GameRules", gameRules.save())
            put("WorldGenSettings", generationSettings.save())
            putInt("SpawnX", spawnX)
            putInt("SpawnY", spawnY)
            putInt("SpawnZ", spawnZ)
            putFloat("SpawnAngle", spawnAngle)
            putLong("Time", time)
            putLong("DayTime", dayTime)
            putLong("LastPlayed", Instant.now().toEpochMilli())
            putInt("clearWeatherTime", clearWeatherTime)
            putBoolean("raining", isRaining)
            putInt("rainTime", rainTime)
            putBoolean("thundering", isThundering)
            putInt("thunderTime", thunderTime)
            putBoolean("initialized", isInitialized)
            putInt("WanderingTraderSpawnChance", wanderingTraderSpawnChance)
            putInt("WanderingTraderSpawnDelay", wanderingTraderSpawnDelay)
            if (wanderingTraderId != null) putUUID("WanderingTraderId", wanderingTraderId!!)
            if (customBossEvents != null) put("CustomBossEvents", customBossEvents!!)
            put("DragonFight", enderDragonFightData)
            putList("ServerBrands", StringTag.ID, serverBrands.map(StringTag::of))
            putBoolean("WasModded", true)
        }
    }

    companion object {

        private const val ANVIL_VERSION_ID = 19133

        @JvmStatic
        fun parse(folder: Path, data: CompoundTag): PrimaryWorldData {
            val generationSettings = WorldGenerationSettings.parse(data.getCompound("WorldGenSettings"))
            val time = data.getLong("Time", 0L)
            return PrimaryWorldData(
                data.getString("LevelName"),
                folder,
                GameModes.fromId(data.getInt("GameType", 0)) ?: GameMode.SURVIVAL,
                resolveDifficulty(data),
                data.getBoolean("hardcore", false),
                KryptonGameRuleHolder.from(data.getCompound("GameRules")),
                generationSettings,
                data.getInt("SpawnX"),
                data.getInt("SpawnY"),
                data.getInt("SpawnZ"),
                data.getFloat("SpawnAngle"),
                time,
                data.getLong("DayTime", time),
                data.getInt("clearWeatherTime"),
                data.getBoolean("raining"),
                data.getInt("rainTime"),
                data.getBoolean("thundering"),
                data.getInt("thunderTime"),
                data.getBoolean("initialized"),
                data.getInt("WanderingTraderSpawnChance"),
                data.getInt("WanderingTraderSpawnDelay"),
                data.getUUID("WanderingTraderId"),
                data.getCompound("CustomBossEvents"),
                resolveDragonFightData(data),
                extractBrands(data)
            )
        }

        @JvmStatic
        private fun extractBrands(data: CompoundTag): Set<String> {
            if (!data.contains("ServerBrands", ListTag.ID)) return persistentSetOf()
            val brandData = data.getList("ServerBrands", StringTag.ID)
            if (brandData.isEmpty) return persistentSetOf()
            val result = persistentSetOf<String>().builder()
            for (i in 0 until brandData.size()) {
                result.add(brandData.getString(i))
            }

            // Add ourselves
            if (!result.contains(KryptonPlatform.name)) result.add(KryptonPlatform.name)
            return result.build()
        }

        @JvmStatic
        private fun resolveDragonFightData(data: CompoundTag): CompoundTag {
            if (data.contains("DragonFightData", CompoundTag.ID)) return data.getCompound("DragonFightData")
            return data.getCompound("DimensionData").getCompound("1").getCompound("DragonFight")
        }

        @JvmStatic
        private fun resolveDifficulty(data: CompoundTag): Difficulty {
            var difficulty: Difficulty? = null
            if (data.contains("Difficulty", 99)) difficulty = Difficulties.fromId(data.getInt("Difficulty"))
            return difficulty ?: Difficulty.NORMAL
        }
    }
}
