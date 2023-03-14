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
package org.kryptonmc.api.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * A tracker of statistics.
 */
public interface StatisticsTracker {

    /**
     * Gets all the statistics that are being tracked by this tracker.
     *
     * @return the tracked statistics
     */
    public fun statistics(): Set<Statistic<*>>

    /**
     * Gets the value for the given [statistic].
     *
     * @param statistic the statistic
     * @return the value
     */
    public fun getStatistic(statistic: Statistic<*>): Int

    /**
     * Gets the value for the given custom [statistic].
     *
     * If the given [statistic] is not in the
     * [custom statistics registry][Registries.CUSTOM_STATISTIC], this will
     * return -1.
     *
     * @param statistic the custom statistic
     * @return the value for the given custom statistic
     */
    public fun getStatistic(statistic: Key): Int

    /**
     * Sets the value for the given [statistic] to the given [value].
     *
     * @param statistic the statistic
     * @param value the value
     */
    public fun setStatistic(statistic: Statistic<*>, value: Int)

    /**
     * Increases the value of the given [statistic] by 1.
     *
     * @param statistic the statistic
     */
    public fun incrementStatistic(statistic: Statistic<*>) {
        incrementStatistic(statistic, 1)
    }

    /**
     * Increases the value of the given [statistic] by the given [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun incrementStatistic(statistic: Statistic<*>, amount: Int)

    /**
     * Increases the value of the given custom [statistic] by 1.
     *
     * @param statistic the statistic
     */
    public fun incrementStatistic(statistic: Key) {
        incrementStatistic(statistic, 1)
    }

    /**
     * Increases the value of the given custom [statistic] by the given
     * [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun incrementStatistic(statistic: Key, amount: Int) {
        incrementStatistic(StatisticTypes.CUSTOM.get().getStatistic(statistic), amount)
    }

    /**
     * Decreases the value of the given [statistic] by 1.
     *
     * @param statistic the statistic
     */
    public fun decrementStatistic(statistic: Statistic<*>) {
        decrementStatistic(statistic, 1)
    }

    /**
     * Decreases the value of the given [statistic] by the given [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun decrementStatistic(statistic: Statistic<*>, amount: Int)

    /**
     * Decreases the value of the given custom [statistic] by 1.
     *
     * @param statistic the statistic
     */
    public fun decrementStatistic(statistic: Key) {
        decrementStatistic(statistic, 1)
    }

    /**
     * Decreases the value of the given custom [statistic] by the given
     * [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun decrementStatistic(statistic: Key, amount: Int) {
        decrementStatistic(StatisticTypes.CUSTOM.get().getStatistic(statistic), amount)
    }

    /**
     * Invalidates all currently tracked statistics, forcing the server to
     * re-send all statistics when the player next requests them, even if none
     * of them have actually been updated.
     */
    public fun invalidate()
}
