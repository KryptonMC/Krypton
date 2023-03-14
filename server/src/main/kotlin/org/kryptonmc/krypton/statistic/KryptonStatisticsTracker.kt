/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.statistic

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutAwardStatistics
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.DataConversion
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.Collections
import kotlin.math.max
import kotlin.math.min

class KryptonStatisticsTracker(private val player: KryptonPlayer, private val file: Path) : StatisticsTracker {

    private val statistics = Object2IntMaps.synchronize<Statistic<*>>(Object2IntOpenHashMap())
    private val pendingUpdate = mutableSetOf<Statistic<*>>()

    init {
        statistics.defaultReturnValue(0)
        if (Files.isRegularFile(file)) load(Files.newInputStream(file, StandardOpenOption.READ).reader())
        if (!Files.exists(file)) {
            try {
                Files.createFile(file)
            } catch (exception: IOException) {
                LOGGER.error("Failed to create statistics file ${file.toAbsolutePath()}!", exception)
                throw exception
            }
        }
    }

    override fun statistics(): Set<Statistic<*>> = Collections.unmodifiableSet(statistics.keys)

    private fun getAndClearPendingUpdate(): Set<Statistic<*>> {
        val copy = LinkedHashSet(pendingUpdate)
        pendingUpdate.clear()
        return copy
    }

    @Suppress("UNCHECKED_CAST")
    fun save() {
        try {
            val map = HashMap<StatisticType<*>, JsonObject>()
            statistics.object2IntEntrySet().forEach {
                val registry = it.key.type.registry as Registry<Any>
                map.computeIfAbsent(it.key.type) { JsonObject() }.addProperty(registry.getKey(it.key.value!!)!!.toString(), it.intValue)
            }

            val statsJson = JsonObject()
            map.forEach { statsJson.add(it.key.key().asString(), it.value) }

            val json = JsonObject().apply {
                add(STATS_KEY, statsJson)
                addProperty(DATA_VERSION_KEY, KryptonPlatform.worldVersion)
            }.toString()
            Files.newOutputStream(file).writer().use { it.write(json) }
        } catch (exception: IOException) {
            LOGGER.error("Failed to save statistics file $file!", exception)
        }
    }

    fun sendUpdated() {
        val map = Object2IntOpenHashMap<Statistic<*>>()
        getAndClearPendingUpdate().forEach { map.put(it, getStatistic(it)) }
        player.connection.send(PacketOutAwardStatistics(map))
    }

    override fun invalidate() {
        pendingUpdate.addAll(statistics.keys)
    }

    override fun getStatistic(statistic: Statistic<*>): Int = statistics.getInt(statistic)

    override fun getStatistic(statistic: Key): Int = statistics.getInt(StatisticTypes.CUSTOM.get().getStatistic(statistic))

    override fun setStatistic(statistic: Statistic<*>, value: Int) {
        statistics.put(statistic, value)
        pendingUpdate.add(statistic)
    }

    override fun incrementStatistic(statistic: Statistic<*>, amount: Int) {
        setStatistic(statistic, min(getStatistic(statistic).toLong() + amount.toLong(), Int.MAX_VALUE.toLong()).toInt())
        player.scoreboard.forEachObjective(statistic, player.teamRepresentation) { it.add(amount) }
    }

    override fun decrementStatistic(statistic: Statistic<*>, amount: Int) {
        setStatistic(statistic, max(getStatistic(statistic).toLong() - amount.toLong(), Int.MIN_VALUE.toLong()).toInt())
        player.scoreboard.forEachObjective(statistic, player.teamRepresentation) { it.subtract(amount) }
    }

    private fun load(reader: Reader) {
        try {
            val json = JsonReader(reader).apply { isLenient = false }.use(Streams::parse)
            if (json.isJsonNull) {
                LOGGER.error("Unable to parse statistics data from $file to JSON!")
                return
            }

            json as JsonObject
            if (!json.has(DATA_VERSION_KEY) || !json.get(DATA_VERSION_KEY).isJsonPrimitive) json.addProperty(DATA_VERSION_KEY, OLD_VERSION)
            val version = json.get(DATA_VERSION_KEY).asInt

            val data = if (version < KryptonPlatform.worldVersion) {
                DataConversion.upgrade(json, MCTypeRegistry.STATS, json.get("DataVersion").asInt)
            } else {
                json
            }
            if (data.get(STATS_KEY).asJsonObject.size() == 0) return

            val stats = data.get(STATS_KEY)?.asJsonObject ?: JsonObject()
            loadStatistics(stats)
        } catch (exception: IOException) {
            LOGGER.error("Failed to load statistics data from $file!", exception)
        } catch (exception: JsonParseException) {
            LOGGER.error("Failed to parse statistics data from $file to JSON!", exception)
        }
    }

    private fun loadStatistics(stats: JsonObject) {
        stats.keySet().forEach { key ->
            if (!stats.get(key).isJsonObject) return@forEach
            val type = KryptonRegistries.STATISTIC_TYPE.get(Key.key(key))
            if (type == null) {
                LOGGER.warn("Invalid statistic type found in $file! Could not recognise $key!")
                return@forEach
            }
            val values = stats.get(key).asJsonObject
            values.keySet().forEach { valueKey -> loadStatisticValue(values, valueKey, type) }
        }
    }

    private fun loadStatisticValue(values: JsonObject, valueKey: String, type: StatisticType<*>) {
        val value = values.get(valueKey)
        if (!value.isJsonPrimitive) {
            LOGGER.warn("Invalid statistic found in $file! Could not recognise value ${values.get(valueKey)} for key $valueKey")
            return
        }
        val statistic = getStatistic(type, valueKey)
        if (statistic != null) {
            statistics.put(statistic, value.asInt)
        } else {
            LOGGER.warn("Invalid statistic found in $file! Could not recognise key $valueKey!")
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val OLD_VERSION = 1343

        private const val STATS_KEY = "stats"
        private const val DATA_VERSION_KEY = "DataVersion"

        @JvmStatic
        private fun <T> getStatistic(type: StatisticType<T>, name: String): Statistic<T>? {
            val key = try {
                Key.key(name)
            } catch (_: InvalidKeyException) {
                return null
            }
            return type.registry.get(key)?.let(type::getStatistic)
        }
    }
}
