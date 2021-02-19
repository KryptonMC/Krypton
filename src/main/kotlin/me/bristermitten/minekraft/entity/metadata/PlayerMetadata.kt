package me.bristermitten.minekraft.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import me.bristermitten.minekraft.entity.MainHand
import me.bristermitten.minekraft.entity.metadata.MetadataType.*
import me.bristermitten.minekraft.extension.writeMetadata
import me.bristermitten.minekraft.world.block.BlockPosition
import net.kyori.adventure.nbt.CompoundBinaryTag

class PlayerMetadata(
    override val movementFlags: MovementFlags = MovementFlags(),
    override val airTicks: Int = 300,
    override val customName: Component? = null,
    override val isCustomNameVisible: Boolean = false,
    override val isSilent: Boolean = false,
    override val hasNoGravity: Boolean = false,
    override val pose: Pose = Pose.STANDING,
    override val handFlags: HandFlags = HandFlags(),
    override val health: Float = 1.0f,
    override val potionEffectColor: Int = 0,
    override val isPotionEffectAmbient: Boolean = false,
    override val arrowsInEntity: Int = 0,
    override val absorptionHealth: Int = 0,
    override val bedPosition: BlockPosition? = null,
    val additionalHearts: Float = 0.0f,
    val score: Int = 0,
    val skinFlags: SkinFlags = SkinFlags(),
    val mainHand: MainHand = MainHand.RIGHT,
    val leftShoulderEntityData: CompoundBinaryTag = CompoundBinaryTag.empty(),
    val rightShoulderEntityData: CompoundBinaryTag = CompoundBinaryTag.empty()
) : LivingEntityMetadata(movementFlags, airTicks, customName, isCustomNameVisible, isSilent, hasNoGravity, pose, handFlags, health, potionEffectColor, isPotionEffectAmbient, arrowsInEntity, absorptionHealth, bedPosition) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        buf.writeMetadata(14u, additionalHearts)
        buf.writeMetadata(15u, score)
        buf.writeMetadata(16u, skinFlags.toProtocol())
        buf.writeMetadata(17u, mainHand.id.toByte())
        buf.writeMetadata(18u, leftShoulderEntityData)
        buf.writeMetadata(19u, rightShoulderEntityData)
    }
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