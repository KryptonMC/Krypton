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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.util.writeMetadata
import org.kryptonmc.krypton.util.writeOptionalMetadata

/**
 * Represents living entity metadata. This is ordered by the index ordering, which can be found on
 *
 * The ordering of this comes from [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
 */
open class LivingEntityMetadata(
    movementFlags: MovementFlags? = null,
    airTicks: Int? = null,
    customName: Optional<Component>? = null,
    isCustomNameVisible: Boolean? = null,
    isSilent: Boolean? = null,
    hasNoGravity: Boolean? = null,
    pose: Pose? = null,
    val handFlags: HandFlags? = null,
    val health: Float? = null,
    val potionEffectColor: Int? = null,
    val isPotionEffectAmbient: Boolean? = null,
    val arrowsInEntity: Int? = null,
    val absorptionHealth: Int? = null,
    val bedPosition: Optional<Vector>? = null
) : EntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        buf.writeMetadata(7u, handFlags?.toProtocol()?.toByte())
        buf.writeMetadata(8u, health)
        buf.writeMetadata(9u, potionEffectColor)
        buf.writeMetadata(10u, isPotionEffectAmbient)
        buf.writeMetadata(11u, arrowsInEntity)
        buf.writeMetadata(12u, absorptionHealth)
        buf.writeOptionalMetadata(13u, bedPosition)
    }

    companion object Default : LivingEntityMetadata(
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
        Optional(null)
    )
}

/**
 * Flags for a living entity's hands.
 *
 * @author Callum Seabrook
 */
data class HandFlags(
    val isHandActive: Boolean = false,
    val activeHand: Hand? = null,
    val isInRiptideSpinAttack: Boolean = false
) {

    /**
     * Convert this [HandFlags] object to flags compatible with the protocol
     * (a single byte that contains all of the values)
     */
    fun toProtocol(): Int {
        var byte = 0x0
        if (isHandActive) byte += 0x01
        if (activeHand != null) {
            byte += 0x02
            byte += activeHand.ordinal
        }
        if (isInRiptideSpinAttack) byte += 0x04
        return byte
    }
}
