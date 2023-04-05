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

import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutAwardStatistics
import java.util.Collections
import kotlin.math.max
import kotlin.math.min

class KryptonStatisticsTracker(private val player: KryptonPlayer) : StatisticsTracker {

    val statistics: Object2IntMap<Statistic<*>> = Object2IntMaps.synchronize(Object2IntOpenHashMap())
    private val pendingUpdate = mutableSetOf<Statistic<*>>()

    init {
        statistics.defaultReturnValue(0)
    }

    override fun statistics(): Set<Statistic<*>> = Collections.unmodifiableSet(statistics.keys)

    private fun getAndClearPendingUpdate(): Set<Statistic<*>> {
        val copy = LinkedHashSet(pendingUpdate)
        pendingUpdate.clear()
        return copy
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
        player.scoreboard.forEachScore(statistic, player.teamRepresentation) { it.score += amount }
    }

    override fun decrementStatistic(statistic: Statistic<*>, amount: Int) {
        setStatistic(statistic, max(getStatistic(statistic).toLong() - amount.toLong(), Int.MIN_VALUE.toLong()).toInt())
        player.scoreboard.forEachScore(statistic, player.teamRepresentation) { it.score -= amount }
    }
}
