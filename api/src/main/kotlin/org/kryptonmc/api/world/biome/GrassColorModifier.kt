/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A modifier for the colouring of grass within a biome.
 */
@CataloguedBy(GrassColorModifiers::class)
public interface GrassColorModifier : Keyed {

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key): GrassColorModifier
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new grass colour modifier with the given [key].
         *
         * @param key the key
         * @return a new grass colour modifier
         */
        @JvmStatic
        public fun of(key: Key): GrassColorModifier = FACTORY.of(key)
    }
}
