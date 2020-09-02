package me.bristermitten.minekraft.packet.play.outbound

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.play.PlayPacket

abstract class EntityPacket(
    open val packetType: EntityPacketType
) : PlayPacket(packetType.id) {

    abstract val entityID: Int

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityID)
    }
}

enum class EntityPacketType(val id: Int) {

    SPAWN_ENTITY(0x00),
    SPAWN_EXPERIENCE_ORB(0x01),
    SPAWN_WEATHER_ENTITY(0x02),
    SPAWN_LIVING_ENTITY(0x03),
    SPAWN_PAINTING(0x04),
    SPAWN_PLAYER(0x05),

    ENTITY_ANIMATION(0x06),
    BLOCK_BREAK_ANIMATION(0x09),

    ENTITY_STATUS(0x1C),
    ENTITY_POSITION(0x29),
    ENTITY_ROTATION(0x2B),
    ENTITY_POSITION_AND_ROTATION(0x2A),
    ENTITY_MOVEMENT(0x2C),
    REMOVE_ENTITY_EFFECT(0x39),
    ENTITY_HEAD_LOOK(0x3C),
    ENTITY_METADATA(0x44),
    ENTITY_VELOCITY(0x46),
    ENTITY_EQUIPMENT(0x47),
    ENTITY_TELEPORT(0x57),
    ENTITY_PROPERTIES(0x59),
    ENTITY_EFFECT(0x5A),

    ATTACH_ENTITY(0x45),
    SET_PASSENGERS(0x4B),

    ENTITY_SOUND_EFFECT(0x51),

    JOIN_GAME(0x26)
}