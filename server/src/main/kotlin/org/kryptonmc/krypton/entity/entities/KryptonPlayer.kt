package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.effect.particle.ColorParticleData
import org.kryptonmc.krypton.api.effect.particle.DirectionalParticleData
import org.kryptonmc.krypton.api.effect.particle.NoteParticleData
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.packet.out.play.PacketOutParticles
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutChat
import org.kryptonmc.krypton.session.Session
import java.net.InetSocketAddress
import java.util.Locale
import java.util.UUID

class KryptonPlayer(
    val session: Session,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : Player, KryptonSender() {

    override lateinit var uuid: UUID
    override lateinit var name: String

    override var displayName: Component = Component.empty()
    override lateinit var abilities: Abilities

    override lateinit var location: Location

    override var velocity = Vector(0.0, 0.0, 0.0)

    override var isOnGround = false
    override var isCrouching = false
    override var isSprinting = false
    override var isFlying = false

    override var viewDistance = 10
    override var time = 0L

    override var scoreboard: Scoreboard? = null

    override lateinit var locale: Locale

    var gamemode = Gamemode.SURVIVAL

    override fun spawnParticles(particleEffect: ParticleEffect, location: Location) {
        val packet = PacketOutParticles(particleEffect, location)
        when (particleEffect.data) {
            // Send multiple packets based on the quantity
            is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> repeat(particleEffect.quantity) { session.sendPacket(packet) }
            // Send particles to player at location
            else -> session.sendPacket(packet)
        }
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        session.sendPacket(PacketOutChat(message, type, source.uuid()))
    }
}