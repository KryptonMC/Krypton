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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.MobCategory
import org.kryptonmc.krypton.registry.KryptonDefaultedRegistry
import org.kryptonmc.krypton.util.keys
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.random.Weight
import org.kryptonmc.krypton.util.random.WeightedEntry
import org.kryptonmc.krypton.util.random.WeightedRandomList

class MobSpawnSettings(
    val creatureGenerationProbability: Float,
    val spawners: Map<MobCategory, WeightedRandomList<SpawnerData>>,
    val mobSpawnCosts: Map<EntityType<*>, MobSpawnCost>,
    val playerSpawnFriendly: Boolean
) {

    fun getMobs(category: MobCategory) = spawners.getOrDefault(category, EMPTY_MOB_LIST)

    fun getMobSpawnCost(type: EntityType<*>) = mobSpawnCosts[type]

    class Builder {

        private val spawners = MobCategory.values().associateWith { mutableListOf<SpawnerData>() }
        private val mobSpawnCosts = mutableMapOf<EntityType<*>, MobSpawnCost>()
        private var creatureGenerationProbability = DEFAULT_CREATURE_SPAWN_PROBABILITY
        private var playerCanSpawn = false

        fun creatureGenerationProbability(probability: Float) = apply { creatureGenerationProbability = probability }

        fun playerCanSpawn() = apply { playerCanSpawn = true }

        fun spawn(category: MobCategory, data: SpawnerData) = apply { spawners[category]!!.add(data) }

        fun mobCharge(type: EntityType<*>, energyBudget: Double, charge: Double) = apply { mobSpawnCosts[type] = MobSpawnCost(energyBudget, charge) }

        fun build() = MobSpawnSettings(creatureGenerationProbability, spawners.mapValues { WeightedRandomList(it.value) }, mobSpawnCosts, playerCanSpawn)
    }

    class MobSpawnCost(val energyBudget: Double, val charge: Double) {

        companion object {

            val CODEC: Codec<MobSpawnCost> = RecordCodecBuilder.create {
                it.group(
                    Codec.DOUBLE.fieldOf("energy_budget").forGetter(MobSpawnCost::energyBudget),
                    Codec.DOUBLE.fieldOf("charge").forGetter(MobSpawnCost::charge)
                ).apply(it, ::MobSpawnCost)
            }
        }
    }

    class SpawnerData(
        val type: EntityType<*>,
        weight: Weight,
        val minCount: Int,
        val maxCount: Int
    ) : WeightedEntry.IntrusiveBase(weight) {

        constructor(type: EntityType<*>, weight: Int, minCount: Int, maxCount: Int) : this(type, Weight.of(weight), minCount, maxCount)

        override fun toString() = "${Registries.ENTITY_TYPE[type]}*($minCount-$maxCount):$weight"

        companion object {

            val CODEC: Codec<SpawnerData> = RecordCodecBuilder.create {
                it.group(
                    (Registries.ENTITY_TYPE as KryptonDefaultedRegistry<EntityType<*>>).fieldOf("type").forGetter(SpawnerData::type),
                    Weight.CODEC.fieldOf("weight").forGetter(SpawnerData::weight),
                    Codec.INT.fieldOf("minCount").forGetter(SpawnerData::minCount),
                    Codec.INT.fieldOf("maxCount").forGetter(SpawnerData::maxCount)
                ).apply(it, ::SpawnerData)
            }
        }
    }

    companion object {

        private val LOGGER = logger<MobSpawnSettings>()
        private const val DEFAULT_CREATURE_SPAWN_PROBABILITY = 0.1F
        private val EMPTY_MOB_LIST = WeightedRandomList<SpawnerData>()
        val EMPTY = MobSpawnSettings(DEFAULT_CREATURE_SPAWN_PROBABILITY, MobCategory.values().associateWith { EMPTY_MOB_LIST }, emptyMap(), false)
        val CODEC: MapCodec<MobSpawnSettings> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.floatRange(0F, 0.9999999F).optionalFieldOf("creature_spawn_probability", DEFAULT_CREATURE_SPAWN_PROBABILITY).forGetter(MobSpawnSettings::creatureGenerationProbability),
                Codec.simpleMap(MobCategory.CODEC, WeightedRandomList.codec(SpawnerData.CODEC).promotePartial { LOGGER.error("Spawn data: $it") }, MobCategory.values().keys()).fieldOf("spawners").forGetter(MobSpawnSettings::spawners),
                Codec.simpleMap(Registries.ENTITY_TYPE as KryptonDefaultedRegistry<EntityType<*>>, MobSpawnCost.CODEC, Registries.ENTITY_TYPE as KryptonDefaultedRegistry<EntityType<*>>).fieldOf("spawn_costs").forGetter(MobSpawnSettings::mobSpawnCosts),
                Codec.BOOL.fieldOf("player_spawn_friendly").orElse(false).forGetter(MobSpawnSettings::playerSpawnFriendly)
            ).apply(instance, ::MobSpawnSettings)
        }
    }
}
