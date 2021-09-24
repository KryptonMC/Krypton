/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.util.provide

/**
 * The supertype of all particle data. Merely serves as a marker interface for
 * its subtypes.
 */
public interface ParticleData {

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun directional(direction: Vector?, velocity: Float): DirectionalParticleData

        public fun item(item: ItemType): ItemParticleData

        public fun block(block: Block): BlockParticleData

        public fun color(red: Short, green: Short, blue: Short): ColorParticleData

        public fun dust(red: Short, green: Short, blue: Short, scale: Float): DustParticleData

        public fun transition(
            fromRed: Short,
            fromGreen: Short,
            fromBlue: Short,
            scale: Float,
            toRed: Short,
            toGreen: Short,
            toBlue: Short
        ): DustTransitionParticleData

        public fun note(note: Byte): NoteParticleData

        public fun vibration(origin: Position, destination: Position, ticks: Int): VibrationParticleData
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()
    }
}
