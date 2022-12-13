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
package org.kryptonmc.krypton.statistic

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import it.unimi.dsi.fastutil.objects.Object2IntMap
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
import kotlin.math.max
import kotlin.math.min

class KryptonStatisticsTracker(private val player: KryptonPlayer, private val file: Path) : StatisticsTracker {

    override val statistics: Object2IntMap<Statistic<*>> = Object2IntMaps.synchronize(Object2IntOpenHashMap())
    private val pendingUpdate = mutableSetOf<Statistic<*>>()
    private val pendingUpdating: Set<Statistic<*>>
        get() {
            val copy = LinkedHashSet(pendingUpdate)
            pendingUpdate.clear()
            return copy
        }

    init {
        statistics.defaultReturnValue(0)
        if (Files.isRegularFile(file)) load(Files.newInputStream(file, StandardOpenOption.READ).reader())
        if (!Files.exists(file)) {
            try {
                Files.createFile(file)
            } catch (exception: Exception) {
                LOGGER.error("Failed to create statistics file ${file.toAbsolutePath()}!", exception)
                throw exception
            }
        }
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

    fun send() {
        val map = Object2IntOpenHashMap<Statistic<*>>()
        pendingUpdating.forEach { map.put(it, get(it)) }
        player.session.send(PacketOutAwardStatistics(map))
    }

    override fun invalidate() {
        pendingUpdate.addAll(statistics.keys)
    }

    override fun get(statistic: Statistic<*>): Int = statistics.getInt(statistic)

    override fun get(statistic: Key): Int = statistics.getInt(StatisticTypes.CUSTOM.get(statistic))

    override fun set(statistic: Statistic<*>, value: Int) {
        statistics.put(statistic, value)
        pendingUpdate.add(statistic)
    }

    override fun increment(statistic: Statistic<*>, amount: Int) {
        set(statistic, min(get(statistic).toLong() + amount.toLong(), Int.MAX_VALUE.toLong()).toInt())
        player.scoreboard.forEachObjective(statistic, player.teamRepresentation) { it.add(amount) }
    }

    override fun decrement(statistic: Statistic<*>, amount: Int) {
        set(statistic, max(get(statistic).toLong() - amount.toLong(), Int.MIN_VALUE.toLong()).toInt())
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
            // We won't upgrade data if use of the data converter is disabled.
            if (version < KryptonPlatform.worldVersion && !player.server.config.advanced.useDataConverter) {
                DataConversion.sendWarning(LOGGER, "statistics data for player with UUID ${player.uuid}")
                error("Tried to load old statistics from version $version when data conversion is disabled!")
            }

            // Don't use data converter if the version isn't older than our version.
            val data = DataConversion.upgrade(json, MCTypeRegistry.STATS, json.get("DataVersion").asInt)
            if (data.get(STATS_KEY).asJsonObject.size() == 0) return

            val stats = data.get(STATS_KEY)?.asJsonObject ?: JsonObject()
            stats.keySet().forEach { key ->
                if (!stats.get(key).isJsonObject) return@forEach
                val type = KryptonRegistries.STATISTIC_TYPE.get(Key.key(key))
                if (type == null) {
                    LOGGER.warn("Invalid statistic type found in $file! Could not recognise $key!")
                    return@forEach
                }
                val values = stats.get(key).asJsonObject
                values.keySet().forEach { valueKey ->
                    if (values.get(valueKey).isJsonPrimitive) {
                        val statistic = statistic(type, valueKey)
                        if (statistic != null) {
                            statistics.put(statistic, values.get(valueKey).asInt)
                        } else {
                            LOGGER.warn("Invalid statistic found in $file! Could not recognise key $valueKey!")
                        }
                    } else {
                        LOGGER.warn("Invalid statistic found in $file! Could not recognise value ${values.get(valueKey)} for key $valueKey")
                    }
                }
            }
        } catch (exception: IOException) {
            LOGGER.error("Failed to load statistics data from $file!", exception)
        } catch (exception: JsonParseException) {
            LOGGER.error("Failed to parse statistics data from $file to JSON!", exception)
        }
    }

    private fun <T> statistic(type: StatisticType<T>, name: String): Statistic<T>? {
        val key = try {
            Key.key(name)
        } catch (_: InvalidKeyException) {
            return null
        }
        return type.registry.get(key)?.let(type::get)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val OLD_VERSION = 1343

        private const val STATS_KEY = "stats"
        private const val DATA_VERSION_KEY = "DataVersion"
    }
}
