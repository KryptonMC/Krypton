package me.bristermitten.minekraft.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import me.bristermitten.minekraft.entity.Hand
import me.bristermitten.minekraft.entity.cardinal.Position
import me.bristermitten.minekraft.extension.writeMetadata
import me.bristermitten.minekraft.extension.writeOptionalMetadata
import me.bristermitten.minekraft.world.block.BlockPosition

/**
 * Represents living entity metadata. This is ordered by the index ordering, which can be found on
 * [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
 */
open class LivingEntityMetadata(
    override val movementFlags: MovementFlags? = null,
    override val airTicks: Int? = null,
    override val customName: Optional<Component>? = null,
    override val isCustomNameVisible: Boolean? = null,
    override val isSilent: Boolean? = null,
    override val hasNoGravity: Boolean? = null,
    override val pose: Pose? = null,
    open val handFlags: HandFlags? = null,
    open val health: Float? = null,
    open val potionEffectColor: Int? = null,
    open val isPotionEffectAmbient: Boolean? = null,
    open val arrowsInEntity: Int? = null,
    open val absorptionHealth: Int? = null,
    open val bedPosition: Optional<Position>? = null
) : EntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        if (handFlags != null) buf.writeMetadata(7u, requireNotNull(handFlags).toProtocol())
        if (health != null) buf.writeMetadata(8u, requireNotNull(health))
        if (potionEffectColor != null) buf.writeMetadata(9u, requireNotNull(potionEffectColor))
        if (isPotionEffectAmbient != null) buf.writeMetadata(10u, requireNotNull(isPotionEffectAmbient))
        if (arrowsInEntity != null) buf.writeMetadata(11u, requireNotNull(arrowsInEntity))
        if (absorptionHealth != null) buf.writeMetadata(12u, requireNotNull(absorptionHealth))
        if (bedPosition != null) buf.writeOptionalMetadata(13u, requireNotNull(bedPosition))
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
        Optional(null))
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