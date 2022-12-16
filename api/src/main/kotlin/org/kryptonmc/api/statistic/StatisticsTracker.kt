/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * A tracker of statistics.
 */
public interface StatisticsTracker {

    /**
     * All of the statistics being tracked by this tracker.
     */
    public val statistics: Map<Statistic<*>, Int>

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
        incrementStatistic(StatisticTypes.CUSTOM.getStatistic(statistic), amount)
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
        decrementStatistic(StatisticTypes.CUSTOM.getStatistic(statistic), amount)
    }

    /**
     * Invalidates all currently tracked statistics, forcing the server to
     * re-send all statistics when the player next requests them, even if none
     * of them have actually been updated.
     */
    public fun invalidate()
}
