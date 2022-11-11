/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.util.Vec3d
import javax.annotation.concurrent.Immutable

/**
 * The supertype of all particle data. Merely serves as a marker interface for
 * its subtypes.
 */
@Immutable
public interface ParticleData {

    @ApiStatus.Internal
    public interface Factory {

        public fun directional(direction: Vec3d?, velocity: Float): DirectionalParticleData

        public fun item(item: ItemStack): ItemParticleData

        public fun block(block: BlockState): BlockParticleData

        public fun color(color: Color): ColorParticleData

        public fun dust(color: Color, scale: Float): DustParticleData

        public fun transition(from: Color, scale: Float, to: Color): DustTransitionParticleData

        public fun note(note: Byte): NoteParticleData

        public fun vibration(destination: Vec3d, ticks: Int): VibrationParticleData
    }

    public companion object {

        @JvmSynthetic
        internal fun factory(): Factory = Krypton.factory()
    }
}
