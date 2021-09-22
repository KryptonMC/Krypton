/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.converter.types.nbt.NBTMapType
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound
import java.time.Instant
import java.util.UUID

class PrimaryWorldData(
    override val name: String,
    override var gameMode: GameMode,
    override var difficulty: Difficulty,
    override var isHardcore: Boolean,
    override var gameRules: KryptonGameRuleHolder,
    override var dataPackConfig: DataPackConfig,
    override var spawnX: Int,
    override var spawnY: Int,
    override var spawnZ: Int,
    override var spawnAngle: Float,
    override var time: Long,
    override var dayTime: Long,
    override var clearWeatherTime: Int,
    override var isRaining: Boolean,
    override var rainTime: Int,
    override var isThundering: Boolean,
    override var thunderTime: Int,
    override var isInitialized: Boolean,
    override var wanderingTraderSpawnChance: Int,
    override var wanderingTraderSpawnDelay: Int,
    override var wanderingTraderId: UUID?,
    private var customBossEvents: CompoundTag?,
    private var enderDragonFightData: CompoundTag,
    private val serverBrands: MutableSet<String>,
    val worldGenerationSettings: WorldGenerationSettings
) : WorldData {

    private var isModded = true

    constructor(
        name: String,
        gameMode: GameMode,
        difficulty: Difficulty,
        isHardcore: Boolean,
        gameRules: KryptonGameRuleHolder,
        dataPackConfig: DataPackConfig,
        worldGenerationSettings: WorldGenerationSettings
    ) : this(
        name,
        gameMode,
        difficulty,
        isHardcore,
        gameRules,
        dataPackConfig,
        0,
        0,
        0,
        0F,
        0L,
        0L,
        0,
        false,
        0,
        false,
        0,
        false,
        0,
        0,
        null,
        null,
        MutableCompoundTag(),
        mutableSetOf(),
        worldGenerationSettings
    )

    fun save() = compound {
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
            int("GameType", Registries.GAME_MODES.idOf(gameMode))
            byte("Difficulty", difficulty.ordinal.toByte())
            boolean("hardcore", isHardcore)
            put("GameRules", gameRules.save())
            DataPackConfig.CODEC.encodeStart(NBTOps, dataPackConfig).result().ifPresent { put("DataPacks", it) }
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
            boolean("WasModded", isModded)
        }
    }

    fun setModdedInfo(name: String, modded: Boolean) {
        serverBrands.add(name)
        isModded = isModded or modded
    }

    companion object {

        private const val ANVIL_VERSION_ID = 19133

        fun parse(
            data: MapType<String>,
            generationSettings: WorldGenerationSettings,
            dataPackConfig: DataPackConfig
        ): PrimaryWorldData {
            val time = data.getLong("Time", 0L)
            val dragonFightData = data.getMap(
                "DragonFightData",
                data.getMap<String>("DimensionData")?.getMap<String>("1")?.getMap("DragonFight")
                    ?: NBTMapType(MutableCompoundTag())
            )
            return PrimaryWorldData(
                data.getString("LevelName", "")!!,
                Registries.GAME_MODES[data.getInt("GameType", 0)] ?: GameModes.SURVIVAL,
                data.getNumber("Difficulty")?.let { Difficulty.fromId(it.toInt()) } ?: Difficulty.NORMAL,
                data.getBoolean("hardcore", false),
                KryptonGameRuleHolder(data.getMap("GameRules") ?: NBTMapType(MutableCompoundTag())),
                dataPackConfig,
                data.getInt("SpawnX", 0),
                data.getInt("SpawnY", 0),
                data.getInt("SpawnZ", 0),
                data.getFloat("SpawnAngle", 0F),
                time,
                data.getLong("DayTime", time),
                data.getInt("clearWeatherTime", 0),
                data.getBoolean("raining", false),
                data.getInt("rainTime", 0),
                data.getBoolean("thundering", false),
                data.getInt("thunderTime", 0),
                data.getBoolean("initialized", false),
                data.getInt("WanderingTraderSpawnChance", 0),
                data.getInt("WanderingTraderSpawnDelay", 0),
                data.getInts("WanderingTraderId")?.toUUID(),
                (data.getMap<String>("CustomBossEvents") as? NBTMapType)?.map
                    ?: MutableCompoundTag(),
                (dragonFightData as NBTMapType).map,
                data.getList("ServerBrands", ObjectType.STRING)?.let {
                    val set = mutableSetOf<String>()
                    for (i in 0 until it.size()) {
                        set.add(it.getString(i))
                    }
                    set
                } ?: mutableSetOf(),
                generationSettings
            )
        }
    }
}
