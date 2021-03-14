package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.event.events.play.SkinSettings
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.entity.MainHand
import org.kryptonmc.krypton.extension.writeMetadata

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
        buf.writeMetadata(17u, mainHand?.id?.toByte())
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

fun Short.toSkinSettings() = SkinSettings(
    (toInt() and 0x01) != 0,
    (toInt() and 0x02) != 0,
    (toInt() and 0x04) != 0,
    (toInt() and 0x08) != 0,
    (toInt() and 0x10) != 0,
    (toInt() and 0x20) != 0,
    (toInt() and 0x40) != 0
)