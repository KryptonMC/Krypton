/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.banner

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of banner pattern.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BannerPatternTypes::class)
public interface BannerPatternType : Keyed {

    /**
     * The shortened code identifying the banner pattern, as specified by
     * https://minecraft.fandom.com/wiki/Banner#Block_data
     */
    @get:JvmName("code")
    public val code: String

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, code: String): BannerPatternType
    }

    public companion object {

        /**
         * Creates a new banner pattern type with the given [key] and
         * shortened [code].
         *
         * @param key the key
         * @param code the shortened code
         * @return a new banner pattern type
         */
        @JvmStatic
        public fun of(key: Key, code: String): BannerPatternType = Krypton.factoryProvider.provide<Factory>().of(key, code)
    }
}
