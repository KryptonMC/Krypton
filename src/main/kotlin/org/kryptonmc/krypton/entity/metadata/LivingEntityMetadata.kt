package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import org.kryptonmc.krypton.entity.Hand
import org.kryptonmc.krypton.space.Position
import org.kryptonmc.krypton.extension.writeMetadata
import org.kryptonmc.krypton.extension.writeOptionalMetadata

/**
 * Represents living entity metadata. This is ordered by the index ordering, which can be found on
 * [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
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
    val bedPosition: Optional<Position>? = null
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

data class HandFlags(
    val isHandActive: Boolean = false,
    val activeHand: Hand? = null,
    val isInRiptideSpinAttack: Boolean = false
) {

    fun toProtocol(): Int {
        var byte = 0x0
        if (isHandActive) byte += 0x01
        if (activeHand != null) {
            byte += 0x02
            byte += activeHand.id
        }
        if (isInRiptideSpinAttack) byte += 0x04
        return byte
    }
}