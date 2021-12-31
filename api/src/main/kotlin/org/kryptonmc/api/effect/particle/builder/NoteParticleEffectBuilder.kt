/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleDsl

/**
 * A builder for building note particle effects.
 */
public interface NoteParticleEffectBuilder : BaseParticleEffectBuilder<NoteParticleEffectBuilder> {

    /**
     * Sets the note for the particle effect.
     *
     * Must be between 0 and 24 inclusively.
     *
     * @param note the note
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun note(note: Int): NoteParticleEffectBuilder
}
