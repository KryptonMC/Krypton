/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

/**
 * A tracker of statistics.
 */
interface StatisticsTracker {

    /**
     * All of the statistics being tracked by this tracker.
     */
    val statistics: Map<Statistic<*>, Int>

    /**
     * Gets the value for the given [statistic].
     *
     * @param statistic the statistic
     * @return the value
     */
    operator fun get(statistic: Statistic<*>): Int

    /**
     * Sets the value for the given [statistic] to the given [value].
     *
     * @param statistic the statistic
     * @param value the value
     */
    operator fun set(statistic: Statistic<*>, value: Int)

    /**
     * Increments the given [statistic] by the given [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    fun increment(statistic: Statistic<*>, amount: Int)

    /**
     * Decrements the given [statistic] by the given [amount].
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    fun decrement(statistic: Statistic<*>, amount: Int)

    /**
     * Invalidates all currently tracked statistics, forcing the server to re-send
     * all statistics when the player next requests them, even if none of them have
     * actually been updated.
     */
    fun invalidate()
}
