/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import org.kryptonmc.api.scoreboard.criteria.Criterion
import javax.annotation.concurrent.Immutable

/**
 * A statistic that may be counted.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface Statistic<T> : Criterion {

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
