/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.ItemStack
import javax.annotation.concurrent.Immutable

/**
 * Holds data for item particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface ItemParticleData : ParticleData {

    /**
     * The item that is shown.
     */
    @get:JvmName("item")
    public val item: ItemStack

    public companion object {

        /**
         * Creates new item particle data with the given [item].
         *
         * @param item the item that is shown
         * @return new item particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(item: ItemStack): ItemParticleData = ParticleData.factory().item(item)
    }
}
