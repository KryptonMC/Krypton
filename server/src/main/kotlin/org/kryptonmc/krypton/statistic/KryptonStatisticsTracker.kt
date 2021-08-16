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
package org.kryptonmc.krypton.statistic

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.bardy.gsonkt.keys
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutStatistics
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.createFile
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.parseKey
import java.io.IOException
import java.io.Reader
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.reader
import kotlin.io.path.writer
import kotlin.math.max
import kotlin.math.min

class KryptonStatisticsTracker(
    private val player: KryptonPlayer,
    private val file: Path
) : StatisticsTracker {

    override val statistics: Object2IntMap<Statistic<*>> = Object2IntMaps.synchronize<Statistic<*>>(Object2IntOpenHashMap()).apply {
        defaultReturnValue(0)
    }
    private val pendingUpdate = mutableSetOf<Statistic<*>>()

    private val pendingUpdating: Set<Statistic<*>>
        get() {
            val copy = LinkedHashSet(pendingUpdate)
            pendingUpdate.clear()
            return copy
        }

    init {
        if (file.isRegularFile()) load(file.reader())
        if (!file.exists()) file.createFile()
    }

    @Suppress("UNCHECKED_CAST")
    fun save() = try {
        val map = mutableMapOf<StatisticType<*>, JsonObject>()
        statistics.object2IntEntrySet().forEach {
            map.getOrPut(it.key.type) { JsonObject() }.addProperty((it.key.type.registry as Registry<Any>)[it.key.value]!!.toString(), it.intValue)
        }

        val statsJson = JsonObject()
        map.forEach { statsJson.add(it.key.key.asString(), it.value) }

        val json = JsonObject().apply {
            add("stats", statsJson)
            addProperty("DataVersion", KryptonPlatform.worldVersion)
        }.toString()
        file.writer().use { it.write(json) }
    } catch (exception: IOException) {
        LOGGER.error("Failed to save statistics file $file!", exception)
    }

    fun send() {
        val map = Object2IntOpenHashMap<Statistic<*>>()
        pendingUpdating.forEach { map[it] = get(it) }
        player.session.sendPacket(PacketOutStatistics(map))
    }

    override fun invalidate() {
        pendingUpdate.addAll(statistics.keys)
    }

    override fun get(statistic: Statistic<*>) = statistics.getInt(statistic)

    override fun set(statistic: Statistic<*>, value: Int) {
        statistics[statistic] = value
        pendingUpdate.add(statistic)
    }

    override fun increment(statistic: Statistic<*>, amount: Int) = set(statistic, min(get(statistic).toLong() + amount.toLong(), Int.MAX_VALUE.toLong()).toInt())

    override fun decrement(statistic: Statistic<*>, amount: Int) = set(statistic, max(get(statistic).toLong() - amount.toLong(), Int.MIN_VALUE.toLong()).toInt())

    private fun load(reader: Reader) {
        try {
            val json = JsonReader(reader).apply { isLenient = false }.use { Streams.parse(it) }
            if (json.isJsonNull) {
                LOGGER.error("Unable to parse statistics data from $file to JSON!")
                return
            }

            json as JsonObject
            if (!json.has("DataVersion") || !json["DataVersion"].isJsonPrimitive) json.addProperty("DataVersion", OLD_VERSION)

            val data = DATA_FIXER.update(References.STATS, Dynamic(JsonOps.INSTANCE, json), json["DataVersion"].asInt, KryptonPlatform.worldVersion)
            if (data["stats"].asMapOpt().result().isEmpty) return

            val stats = data["stats"].orElseEmptyMap().value.asJsonObject
            stats.keys.forEach { key ->
                if (!stats[key].isJsonObject) return@forEach
                InternalRegistries.STATISTIC_TYPE[Key.key(key)]?.let { type ->
                    val values = stats[key].asJsonObject
                    values.keys.forEach { k ->
                        if (values[k].isJsonPrimitive) {
                            stat(type, k)?.let { statistics[it] = values[k].asInt } ?: LOGGER.warn("Invalid statistic found in $file! Could not recognise key $k!")
                        } else {
                            LOGGER.warn("Invalid statistic found in $file! Could not recognise value ${values[k]} for key $k")
                        }
                    }
                } ?: LOGGER.warn("Invalid statistic type found in $file! Could not recognise $key!")
            }
        } catch (exception: IOException) {
            LOGGER.error("Failed to load statistics data from $file!", exception)
        } catch (exception: JsonParseException) {
            LOGGER.error("Failed to parse statistics data from $file to JSON!", exception)
        }
    }

    private fun <T : Any> stat(type: StatisticType<T>, name: String): Statistic<T>? {
        val key = name.parseKey() ?: return null
        return type.registry[key]?.let { type[it] }
    }

    companion object {

        private val LOGGER = logger<KryptonStatisticsTracker>()
        private const val OLD_VERSION = 1343
    }
}
