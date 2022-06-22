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
    private var enderDragonFightData: CompoundTag = CompoundTag.empty(),
    private val serverBrands: Set<String> = persistentSetOf()
) : WorldData {

    fun save(): CompoundTag = compound {
        compound("Data") {
            compound("Krypton") {
                string("Version", KryptonPlatform.version)
            }
            int("DataVersion", KryptonPlatform.worldVersion)
            int("version", ANVIL_VERSION_ID)
            compound("Version") {
                int("Id", KryptonPlatform.worldVersion)
                string("Name", KryptonPlatform.minecraftVersion)
                boolean("Snapshot", !KryptonPlatform.isStableMinecraft)
            }
            string("LevelName", name)
            int("GameType", gameMode.ordinal)
            byte("Difficulty", difficulty.ordinal.toByte())
            boolean("hardcore", isHardcore)
            put("GameRules", gameRules.save())
            put("WorldGenSettings", generationSettings.save())
            int("SpawnX", spawnX)
            int("SpawnY", spawnY)
            int("SpawnZ", spawnZ)
            float("SpawnAngle", spawnAngle)
            long("Time", time)
            long("DayTime", dayTime)
            long("LastPlayed", Instant.now().toEpochMilli())
            int("clearWeatherTime", clearWeatherTime)
            boolean("raining", isRaining)
            int("rainTime", rainTime)
            boolean("thundering", isThundering)
            int("thunderTime", thunderTime)
            boolean("initialized", isInitialized)
            int("WanderingTraderSpawnChance", wanderingTraderSpawnChance)
            int("WanderingTraderSpawnDelay", wanderingTraderSpawnDelay)
            wanderingTraderId?.let { uuid("WanderingTraderId", it) }
            customBossEvents?.let { put("CustomBossEvents", it) }
            put("DragonFight", enderDragonFightData)
            list("ServerBrands", StringTag.ID, serverBrands.map { StringTag.of(it) })
            boolean("WasModded", true)
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
            if (brandData.isEmpty()) return persistentSetOf()
            val result = persistentSetOf<String>().builder()
            for (i in 0 until brandData.size) {
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
