/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import org.kryptonmc.api.world.scoreboard.criteria.Criterion

/**
 * Represents a statistic that may be counted.
 */
public interface Statistic<T : Any> : Criterion {

    /**
     * The type of this statistic.
     */
    public val type: StatisticType<T>

    /**
     * The value for this statistic.
     */
    public val value: T

    /**
     * The formatter used to format values for this statistic.
     */
    public val formatter: StatisticFormatter
}
