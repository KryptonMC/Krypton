/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import org.kryptonmc.api.event.play.SkinSettings
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.util.writeMetadata

/**
 * Represents metadata common to all players
 *
 * The ordering of this comes from [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
 */
open class PlayerMetadata(
    movementFlags: MovementFlags? = null,
    airTicks: Int? = null,
    customName: Optional<Component>? = null,
    isCustomNameVisible: Boolean? = null,
    isSilent: Boolean? = null,
    hasNoGravity: Boolean? = null,
    pose: Pose? = null,
    handFlags: HandFlags? = null,
    health: Float? = null,
    potionEffectColor: Int? = null,
    isPotionEffectAmbient: Boolean? = null,
    arrowsInEntity: Int? = null,
    absorptionHealth: Int? = null,
    bedPosition: Optional<Vector>? = null,
    val additionalHearts: Float? = null,
    val score: Int? = null,
    val skinFlags: SkinSettings? = null,
    val mainHand: MainHand? = null,
    val leftShoulderEntityData: CompoundBinaryTag? = null,
    val rightShoulderEntityData: CompoundBinaryTag? = null
) : LivingEntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose, handFlags, health, potionEffectColor, isPotionEffectAmbient, arrowsInEntity, absorptionHealth, bedPosition) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        buf.writeMetadata(14u, additionalHearts)
        buf.writeMetadata(15u, score)
        buf.writeMetadata(16u, skinFlags?.toProtocol())
        buf.writeMetadata(17u, mainHand?.ordinal?.toByte())
        buf.writeMetadata(18u, leftShoulderEntityData)
        buf.writeMetadata(19u, rightShoulderEntityData)
    }

    @Suppress("BooleanLiteralArgument")
    companion object Default : PlayerMetadata(
        MovementFlags(),
        300,
        Optional(null),
        false,
        false,
        false,
        Pose.STANDING,
        HandFlags(),
        1.0f,
        0,
        false,
        0,
        0,
        Optional(null),
        0.0f,
        0,
        SkinSettings(false, false, false, false, false, false, false),
        MainHand.RIGHT,
        CompoundBinaryTag.empty(),
        CompoundBinaryTag.empty()
    )
}

/**
 * Convert this [SkinSettings] object to a flags field for the protocol
 */
fun SkinSettings.toProtocol(): Byte {
    var byte = 0
    if (cape) byte += 1
    if (jacket) byte += 2
    if (leftSleeve) byte += 4
    if (rightSleeve) byte += 8
    if (leftPants) byte += 0x10
    if (rightPants) byte += 0x20
    if (hat) byte += 0x40
    return byte.toByte()
}

/**
 * Convert a flags field to a [SkinSettings] object
 */
fun Short.toSkinSettings() = SkinSettings(
    toInt() and 0x01 != 0,
    toInt() and 0x02 != 0,
    toInt() and 0x04 != 0,
    toInt() and 0x08 != 0,
    toInt() and 0x10 != 0,
    toInt() and 0x20 != 0,
    toInt() and 0x40 != 0
)
