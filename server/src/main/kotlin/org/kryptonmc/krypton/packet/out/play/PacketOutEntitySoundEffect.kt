package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutEntitySoundEffect(
    private val event: SoundEvent,
    private val source: Sound.Source,
    private val id: Int,
    private val volume: Float,
    private val pitch: Float
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(Registries.SOUND_EVENT.idOf(event))
        buf.writeEnum(source)
        buf.writeVarInt(id)
        buf.writeFloat(volume)
        buf.writeFloat(pitch)
    }
}
