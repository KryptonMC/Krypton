package me.bristermitten.minekraft.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import me.bristermitten.minekraft.entity.Hand
import me.bristermitten.minekraft.entity.cardinal.Position
import me.bristermitten.minekraft.extension.writeMetadata
import me.bristermitten.minekraft.extension.writeOptionalMetadata

/**
 * Represents living entity metadata. This is ordered by the index ordering, which can be found on
 * [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
 */
open class LivingEntityMetadata(
    override val movementFlags: MovementFlags = MovementFlags(),
    override val airTicks: Int = 300,
    override val customName: Component? = null,
    override val isCustomNameVisible: Boolean = false,
    override val isSilent: Boolean = false,
    override val hasNoGravity: Boolean = false,
    override val pose: Pose = Pose.STANDING,
    open val handFlags: HandFlags = HandFlags(),
    open val health: Float = 1.0f,
    open val potionEffectColor: Int = 0,
    open val isPotionEffectAmbient: Boolean = false,
    open val arrowsInEntity: Int = 0,
    open val absorptionHealth: Int = 0,
    open val bedPosition: Position? = null
) : EntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        buf.writeMetadata(7u, handFlags.toProtocol())
        buf.writeMetadata(8u, health)
        buf.writeMetadata(9u, potionEffectColor)
        buf.writeMetadata(10u, isPotionEffectAmbient)
        buf.writeMetadata(11u, arrowsInEntity)
        buf.writeMetadata(12u, absorptionHealth)
        buf.writeOptionalMetadata(13u, bedPosition)
    }
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