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
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.registry.ops.RegistryWriteOps
import org.kryptonmc.krypton.util.UUID_CODEC
import org.kryptonmc.krypton.util.logger
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
    val kryptonData: KryptonWorldData,
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
    var customBossEvents: CompoundTag?,
    var enderDragonFightData: CompoundTag,
    val serverBrands: MutableSet<String>,
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
    ) : this(KryptonWorldData.DEFAULT, name, gamemode, difficulty, isHardcore, gameRules, dataPackConfig, 0, 0, 0, 0F, 0L, 0L, 0, false, 0, false, 0, false, 0, 0, null, null, CompoundTag(), mutableSetOf(), worldGenerationSettings)

    fun save(registryHolder: RegistryHolder) = compound {
        compound("Krypton") {
            string("Version", kryptonData.version)
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
        val ops = RegistryWriteOps(NBTOps, registryHolder)
        WorldGenerationSettings.CODEC.encodeStart(ops, worldGenerationSettings)
            .resultOrPartial { LOGGER.error("WorldGenSettings: $it") }
            .ifPresent { put("WorldGenSettings", it) }
    }

    fun setModdedInfo(name: String, modded: Boolean) {
        serverBrands.add(name)
        isModded = isModded or modded
    }

    companion object {

        private val LOGGER = logger<PrimaryWorldData>()
        private const val ANVIL_VERSION_ID = 19133
    }
}

fun Dynamic<Tag>.parseWorldData(worldGenSettings: WorldGenerationSettings, dataPackConfig: DataPackConfig): PrimaryWorldData {
    val time = get("Time").asLong(0L)
    val dragonFightData = get("DragonFight").result().map(Dynamic<Tag>::getValue).orElseGet { get("DimensionData")["1"]["DragonFight"].orElseEmptyMap().value } as CompoundTag
    return PrimaryWorldData(
        get("Krypton").result().map { it.toKryptonWorldData() }.orElse(KryptonWorldData.DEFAULT),
        get("LevelName").asString(""),
        Gamemode.fromId(get("GameType").asInt(0)) ?: Gamemode.SURVIVAL,
        get("Difficulty").asNumber().map { Difficulty.fromId(it.toInt()) }.result().orElse(Difficulty.NORMAL),
        get("hardcore").asBoolean(false),
        KryptonGameRuleHolder(get("GameRules")),
        dataPackConfig,
        get("SpawnX").asInt(0),
        get("SpawnY").asInt(0),
        get("SpawnZ").asInt(0),
        get("SpawnAngle").asFloat(0F),
        time,
        get("DayTime").asLong(time),
        get("clearWeatherTime").asInt(0),
        get("raining").asBoolean(false),
        get("rainTime").asInt(0),
        get("thundering").asBoolean(false),
        get("thunderTime").asInt(0),
        get("initialized").asBoolean(false),
        get("WanderingTraderSpawnChance").asInt(0),
        get("WanderingTraderSpawnDelay").asInt(0),
        get("WanderingTraderId").read(UUID_CODEC).result().orElse(null),
        get("CustomBossEvents").orElseEmptyMap().value as CompoundTag,
        dragonFightData,
        get("ServerBrands").asStream().flatMap { brand -> DataFixUtils.orElseGet(brand.asString().result().map { Stream.of(it) }) { Stream.empty() } }.collect(Collectors.toCollection(::mutableSetOf)),
        worldGenSettings
    )
}
