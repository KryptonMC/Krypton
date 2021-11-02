/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A type representing a method of rendering an objective to a client.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ObjectiveRenderType : Keyed {

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key): ObjectiveRenderType
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new objective render type with the given [key].
         *
         * @param key the key
         * @return a new objective render type
         */
        public fun of(key: Key): ObjectiveRenderType = FACTORY.of(key)
    }
}
