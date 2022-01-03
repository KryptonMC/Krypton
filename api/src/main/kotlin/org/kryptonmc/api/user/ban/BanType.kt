/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.ban

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of ban.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BanTypes::class)
public interface BanType : Keyed {

    /**
     * The ban class that this type represents.
     */
    @get:JvmName("banClass")
    public val banClass: Class<out Ban>

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, clazz: Class<out Ban>): BanType
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new ban type with the given values.
         *
         * @param key the key
         * @param clazz the class
         * @return a new ban type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, clazz: Class<out Ban>): BanType = FACTORY.of(key, clazz)
    }
}
