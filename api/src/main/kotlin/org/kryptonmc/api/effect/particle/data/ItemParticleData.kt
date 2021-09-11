/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.kryptonmc.api.item.ItemType

/**
 * Holds data for item particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemParticleData : ParticleData {

    /**
     * The item that is shown.
     */
    @get:JvmName("item")
    public val item: ItemType

    public companion object {

        /**
         * Creates new item particle data with the given [item].
         *
         * @param item the item that is shown
         * @return new item particle data
         */
        @JvmStatic
        public fun of(item: ItemType): ItemParticleData = ParticleData.FACTORY.item(item)
    }
}
