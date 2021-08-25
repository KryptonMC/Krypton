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

import com.mojang.datafixers.DataFixUtils
import com.mojang.serialization.Dynamic
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.KryptonGameRuleHolder
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.compound
import java.time.Instant
import java.util.UUID
import java.util.stream.Collectors
import java.util.stream.Stream

class PrimaryWorldData(
    override val name: String,
    override var gamemode: Gamemode,
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
        gamemode: Gamemode,
        difficulty: Difficulty,
        isHardcore: Boolean,
        gameRules: KryptonGameRuleHolder,
        dataPackConfig: DataPackConfig,
        worldGenerationSettings: WorldGenerationSettings
    ) : this(
        name,
        gamemode,
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
        CompoundTag(),
        mutableSetOf(),
        worldGenerationSettings
    )

    fun save() = compound {
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
        int("GameType", gamemode.ordinal)
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

    fun setModdedInfo(name: String, modded: Boolean) {
        serverBrands.add(name)
        isModded = isModded or modded
    }

    companion object {

        private const val ANVIL_VERSION_ID = 19133

        fun parse(dynamic: Dynamic<Tag>, generationSettings: WorldGenerationSettings, dataPackConfig: DataPackConfig): PrimaryWorldData {
            val time = dynamic["Time"].asLong(0L)
            val dragonFightData = dynamic["DragonFight"].result().map(Dynamic<Tag>::getValue).orElseGet { dynamic["DimensionData"]["1"]["DragonFight"].orElseEmptyMap().value } as CompoundTag
            return PrimaryWorldData(
                dynamic["LevelName"].asString(""),
                Gamemode.fromId(dynamic["GameType"].asInt(0)) ?: Gamemode.SURVIVAL,
                dynamic["Difficulty"].asNumber().map { Difficulty.fromId(it.toInt()) }.result().orElse(Difficulty.NORMAL),
                dynamic["hardcore"].asBoolean(false),
                KryptonGameRuleHolder(dynamic["GameRules"]),
                dataPackConfig,
                dynamic["SpawnX"].asInt(0),
                dynamic["SpawnY"].asInt(0),
                dynamic["SpawnZ"].asInt(0),
                dynamic["SpawnAngle"].asFloat(0F),
                time,
                dynamic["DayTime"].asLong(time),
                dynamic["clearWeatherTime"].asInt(0),
                dynamic["raining"].asBoolean(false),
                dynamic["rainTime"].asInt(0),
                dynamic["thundering"].asBoolean(false),
                dynamic["thunderTime"].asInt(0),
                dynamic["initialized"].asBoolean(false),
                dynamic["WanderingTraderSpawnChance"].asInt(0),
                dynamic["WanderingTraderSpawnDelay"].asInt(0),
                dynamic["WanderingTraderId"].read(Codecs.UUID).result().orElse(null),
                dynamic["CustomBossEvents"].orElseEmptyMap().value as CompoundTag,
                dragonFightData,
                dynamic["ServerBrands"].asStream().flatMap { brand -> DataFixUtils.orElseGet(brand.asString().result().map { Stream.of(it) }) { Stream.empty() } }.collect(Collectors.toCollection(::mutableSetOf)),
                generationSettings
            )
        }
    }
}
