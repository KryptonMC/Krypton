/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.rule

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * A rule dictating how a specific aspect of the game functions.
 *
 * @param V the type of the value
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(GameRules::class)
@Immutable
public interface GameRule<V> : Translatable, Keyed {

    /**
     * The name of this rule.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The default value of this rule.
     */
    @get:JvmName("defaultValue")
    public val defaultValue: V

    @ApiStatus.Internal
    public interface Factory {

        public fun <V> of(name: String, defaultValue: V): GameRule<V>
    }

    public companion object {

        /**
         * Creates a new game rule with the given values.
         *
         * @param V the game rule value type
         * @param name the name
         * @param defaultValue the default value
         * @return a new game rule
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun <V> of(name: String, defaultValue: V): GameRule<V> = Krypton.factory<Factory>().of(name, defaultValue)
    }
}
