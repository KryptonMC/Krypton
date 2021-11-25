/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A variant of an axolotl.
 */
@CataloguedBy(AxolotlVariants::class)
public interface AxolotlVariant : Keyed {

    /**
     * If this axolotl variant is common to find.
     *
     * Currently only applies to the [blue variant][AxolotlVariants.BLUE] in
     * vanilla, but it can be used to drastically decrease the spawn chance for
     * custom axolotl variants.
     */
    public val isCommon: Boolean

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, isCommon: Boolean): AxolotlVariant
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new axolotl variant with the given values.
         *
         * @param key the key
         * @param isCommon if the variant spawns commonly
         * @return a new axolotl variant
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, isCommon: Boolean): AxolotlVariant = FACTORY.of(key, isCommon)
    }
}
