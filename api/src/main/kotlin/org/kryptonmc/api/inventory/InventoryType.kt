/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * Represents a type of [Inventory] that holds items.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(InventoryTypes::class)
public interface InventoryType : Keyed {

    /**
     * The size of the inventory.
     */
    @get:JvmName("size")
    public val size: Int

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, size: Int): InventoryType

        public fun grid(key: Key, columns: Int, rows: Int): GridInventoryType
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new inventory type with the given values.
         *
         * @param key the key
         * @param size the size
         * @return a new inventory type
         */
        @JvmStatic
        public fun of(key: Key, size: Int): InventoryType = FACTORY.of(key, size)
    }
}
