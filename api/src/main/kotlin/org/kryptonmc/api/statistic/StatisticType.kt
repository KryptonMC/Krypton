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

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of a statistic.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(StatisticTypes::class)
@ImmutableType
public interface StatisticType<T> : Iterable<Statistic<T>>, Translatable, Keyed {

    /**
     * The registry for this statistic type.
     */
    @get:JvmName("registry")
    public val registry: Registry<T>

    /**
     * Returns true if this type contains a statistic for the given [key],
     * false otherwise.
     *
     * @param key the key
     * @return true if this type contains a statistic for the key, false
     * otherwise
     */
    public fun hasStatistic(key: T): Boolean

    /**
     * Gets the statistic for the given [key], creating it if it does not
     * already exist.
     *
     * @param key the key
     * @return the statistic for the key
     */
    public fun getStatistic(key: T): Statistic<T>

    /**
     * Gets the statistic for the given [key] with the given [formatter],
     * creating it if it does not already exist.
     *
     * @param key the key
     * @param formatter the formatter
     * @return the statistic for the key
     */
    public fun getStatistic(key: T, formatter: StatisticFormatter): Statistic<T>
}
