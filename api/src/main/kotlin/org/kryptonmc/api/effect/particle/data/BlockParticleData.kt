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
import org.kryptonmc.api.block.Block

/**
 * Holds data for block particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BlockParticleData : ParticleData {

    /**
     * The block that is shown.
     */
    @get:JvmName("block")
    public val block: Block

    public companion object {

        /**
         * Creates new block particle data with the given [block].
         *
         * @param block the block that is shown
         * @return new block particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(block: Block): BlockParticleData = ParticleData.FACTORY.block(block)
    }
}
