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
package org.kryptonmc.api.effect.particle.builder

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.internal.annotations.dsl.ParticleDsl

/**
 * The base builder for building particle effects.
 */
@ParticleDsl
public interface BaseParticleEffectBuilder<B : BaseParticleEffectBuilder<B>> : AbstractBuilder<ParticleEffect> {

    /**
     * Sets the number of particles to be spawned by the particle effect.
     *
     * The quantity must be between 1 and 16384, inclusively.
     *
     * @param quantity the quantity
     * @return this builder
     * @throws IllegalArgumentException if the quantity is < 1
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun quantity(quantity: Int): B

    /**
     * Sets the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun offset(offset: Vec3d): B

    /**
     * Sets whether the particle effect can be viewed from a further distance
     * than normal.
     *
     * This value changes the maximum view distance, in blocks, that the
     * effect can be viewed from the location where the effect was spawned to
     * the following:
     * * If true, 65536 blocks
     * * If false, 256 blocks
     *
     * @param longDistance whether the effect can be viewed from a further
     * distance than normal
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun longDistance(longDistance: Boolean): B
}
