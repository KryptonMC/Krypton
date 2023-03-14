/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Holds data for block particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BlockParticleData : ParticleData {

    /**
     * The block state that is shown.
     */
    @get:JvmName("block")
    public val block: BlockState

    public companion object {

        /**
         * Creates new block particle data with the given [block] state.
         *
         * @param block the block state that is shown
         * @return new block particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(block: BlockState): BlockParticleData = ParticleData.factory().block(block)
    }
}
