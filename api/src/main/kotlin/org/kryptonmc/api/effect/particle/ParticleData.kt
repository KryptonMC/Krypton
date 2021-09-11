/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector

/**
 * Interface used to denote that a class can be used as a [ParticleEffect]'s
 * data.
 */
public sealed interface ParticleData

/**
 * Holds data for directional [ParticleEffect]s.
 *
 * @param direction the direction of the particle, random if null
 * @param velocity the velocity of this directional particle in the direction
 * it's moving
 */
@JvmRecord
public data class DirectionalParticleData(
    public val direction: Vector?,
    public val velocity: Float
) : ParticleData

/**
 * Holds data for item [ParticleEffect]s.
 *
 * @param item the item type
 */
@JvmRecord
public data class ItemParticleData(public val item: ItemType) : ParticleData

/**
 * Holds data for block [ParticleEffect]s.
 *
 * @param block the block
 */
@JvmRecord
public data class BlockParticleData(public val block: Block) : ParticleData

/**
 * Holds data for colored [ParticleEffect]s.
 *
 * @param red the red component of this RGB color
 * @param green the green component of this RGB color
 * @param blue the blue component of this RGB color
 */
@JvmRecord
public data class ColorParticleData(
    public val red: Short,
    public val green: Short,
    public val blue: Short
) : ParticleData

/**
 * Holds data for dust [ParticleEffect]s.
 *
 * @param color the color of the dust particle
 * @param scale the scale
 */
@JvmRecord
public data class DustParticleData(
    public val color: ColorParticleData,
    public val scale: Float
) : ParticleData

/**
 * Holds data for dust color transition [ParticleEffect]s.
 *
 * @param from the color to transition from
 * @param scale the scale
 * @param to the color to transition to
 */
@JvmRecord
public data class DustTransitionParticleData(
    public val from: ColorParticleData,
    public val scale: Float,
    public val to: ColorParticleData
) : ParticleData

/**
 * Holds data for note [ParticleEffect]s.
 *
 * @param note the note of this particle, must be between 0 and 24 (inclusive)
 * @throws IllegalArgumentException if the [note] is not between 0 and 24
 * (inclusive)
 */
@JvmRecord
public data class NoteParticleData(public val note: Byte) : ParticleData {

    init {
        require(note in 0..24) { "Note must be between 0 and 24!" }
    }
}

/**
 * Holds data for vibration [ParticleEffect]s.
 *
 * @param origin the origin location
 * @param destination the destination location
 * @param ticks the amount of ticks it takes for this particle to vibrate from
 * the [origin] to the [destination]
 */
@JvmRecord
public data class VibrationParticleData(
    public val origin: Position,
    public val destination: Position,
    public val ticks: Int
) : ParticleData
