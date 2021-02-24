package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.space.Position
import org.kryptonmc.krypton.extension.writeMetadata

open class PlayerMetadata(
    override val movementFlags: MovementFlags? = null,
    override val airTicks: Int? = null,
    override val customName: Optional<Component>? = null,
    override val isCustomNameVisible: Boolean? = null,
    override val isSilent: Boolean? = null,
    override val hasNoGravity: Boolean? = null,
    override val pose: Pose? = null,
    override val handFlags: HandFlags? = null,
    override val health: Float? = null,
    override val potionEffectColor: Int? = null,
    override val isPotionEffectAmbient: Boolean? = null,
    override val arrowsInEntity: Int? = null,
    override val absorptionHealth: Int? = null,
    override val bedPosition: Optional<Position>? = null,
    val additionalHearts: Float? = null,
    val score: Int? = null,
    val skinFlags: SkinFlags? = null,
    val mainHand: MainHand? = null,
    val leftShoulderEntityData: CompoundBinaryTag? = null,
    val rightShoulderEntityData: CompoundBinaryTag? = null
) : LivingEntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose, handFlags, health, potionEffectColor, isPotionEffectAmbient, arrowsInEntity, absorptionHealth, bedPosition) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        if (additionalHearts != null) buf.writeMetadata(14u, additionalHearts)
        if (score != null) buf.writeMetadata(15u, score)
        if (skinFlags != null) buf.writeMetadata(16u, skinFlags.toProtocol())
        if (mainHand != null) buf.writeMetadata(17u, mainHand.id)
        if (leftShoulderEntityData != null) buf.writeMetadata(18u, leftShoulderEntityData)
        if (rightShoulderEntityData != null) buf.writeMetadata(19u, rightShoulderEntityData)
    }

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
        SkinFlags(),
        MainHand.RIGHT,
        CompoundBinaryTag.empty(),
        CompoundBinaryTag.empty()
    )
}

data class SkinFlags(
    val cape: Boolean = false,
    val jacket: Boolean = false,
    val leftSleeve: Boolean = false,
    val rightSleeve: Boolean = false,
    val leftPants: Boolean = false,
    val rightPants: Boolean = false,
    val hat: Boolean = false
) {

    fun toProtocol(): Byte {
        var byte = 0x0
        if (cape) byte += 0x01
        if (jacket) byte += 0x02
        if (leftSleeve) byte += 0x04
        if (rightSleeve) byte += 0x08
        if (leftPants) byte += 0x10
        if (rightPants) byte += 0x20
        if (hat) byte += 0x40
        return byte.toByte()
    }
}

fun Short.toSkinFlags() = SkinFlags(
    (toInt() and 0x01) != 0,
    (toInt() and 0x02) != 0,
    (toInt() and 0x04) != 0,
    (toInt() and 0x08) != 0,
    (toInt() and 0x10) != 0,
    (toInt() and 0x20) != 0,
    (toInt() and 0x40) != 0
)