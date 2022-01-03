/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of inventory.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(InventoryTypes::class)
public interface InventoryType : Keyed {

    /**
     * The size of the inventory.
     */
    @get:JvmName("size")
    public val size: Int

    /**
     * The default title that will be displayed when inventories of this type
     * are sent and no custom title has been defined.
     */
    @get:JvmName("defaultTitle")
    public val defaultTitle: Component

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, size: Int, defaultTitle: Component): InventoryType
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new inventory type with the given values.
         *
         * @param key the key
         * @param size the size
         * @param defaultTitle the default title
         * @return a new inventory type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, size: Int, defaultTitle: Component): InventoryType = FACTORY.of(key, size, defaultTitle)
    }
}
