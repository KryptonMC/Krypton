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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.spongepowered.math.vector.Vector3d

/**
 * Allows building a [ParticleEffect] for note particle effects using method
 * chaining.
 */
public class NoteParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var note: Byte = 0
) : AbstractParticleEffectBuilder<NoteParticleEffectBuilder>(type, quantity, offset, longDistance) {

    /**
     * Sets the type of note to use.
     *
     * Must be between 0 and 24 inclusively.
     *
     * @param note the note value
     */
    @Contract("_ -> this", mutates = "this")
    public fun note(note: Int): NoteParticleEffectBuilder = apply {
        require(note in 0..24) { "Note must be between 0 and 24!" }
        this.note = note.toByte()
    }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(type, quantity, offset, longDistance, NoteParticleData.of(note))
}
