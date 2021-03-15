package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.extension.writeParticle
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * @author Esophose
 */
class PacketOutParticles(
    private val particleEffect: ParticleEffect,
    private val location: Location
) : PlayPacket(0x22) {

    override fun write(buf: ByteBuf) {
        buf.writeParticle(particleEffect, location)
    }
}
