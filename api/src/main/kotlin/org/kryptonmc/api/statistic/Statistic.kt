/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import org.kryptonmc.api.scoreboard.criteria.Criterion

/**
 * Represents a statistic that may be counted.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Statistic<T : Any> : Criterion {

    /**
     * The type of this statistic.
     */
    @get:JvmName("type")
    public val type: StatisticType<T>

    /**
     * The value for this statistic.
     */
    @get:JvmName("value")
    public val value: T

    /**
     * The formatter used to format values for this statistic.
     */
    @get:JvmName("formatter")
    public val formatter: StatisticFormatter
}
