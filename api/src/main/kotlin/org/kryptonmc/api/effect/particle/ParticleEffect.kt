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
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Holds information used to spawn particles for a player.
 *
 * This effect is entirely immutable, and so is safe for both storage and
 * reuse.
 *
 * Effect instances can only be created through particle effect builders (any
 * subclass of [BaseParticleEffectBuilder]).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ParticleEffect {

    /**
     * The type of this effect.
     */
    @get:JvmName("type")
    public val type: ParticleType

    /**
     * The amount of this effect that should be spawned.
     */
    @get:JvmName("quantity")
    public val quantity: Int

    /**
     * The offset vector from the spawn location of this effect.
     */
    @get:JvmName("offset")
    public val offset: Vec3d

    /**
     * If the distance of this effect is longer than usual.
     *
     * Specifically, the distance will increase from 256 to 65536.
     */
    @get:JvmName("longDistance")
    public val longDistance: Boolean

    /**
     * The extra data for this effect.
     *
     * May be null if this effect does not have any extra data.
     */
    @get:JvmName("data")
    public val data: ParticleData?
}
