package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.effect.particle.ColorParticleData
import org.kryptonmc.krypton.api.effect.particle.DirectionalParticleData
import org.kryptonmc.krypton.api.effect.particle.NoteParticleData
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.entity.entities.data.PlayerData
import org.kryptonmc.krypton.packet.out.play.PacketOutParticles
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutPlayerListHeaderFooter
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutTitle
import org.kryptonmc.krypton.packet.out.play.chat.TitleAction
import org.kryptonmc.krypton.session.Session
import org.kryptonmc.krypton.world.KryptonWorld
import java.net.InetSocketAddress
import java.util.*

class KryptonPlayer(
    override val name: String,
    server: KryptonServer,
    val session: Session,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : Player, KryptonSender(server) {

    override lateinit var uuid: UUID

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

    override var locale: Locale? = null

    var gamemode = Gamemode.SURVIVAL
    override lateinit var world: KryptonWorld
    override lateinit var dimension: NamespacedKey

    lateinit var data: PlayerData

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

    override fun sendActionBar(message: Component) {
        session.sendPacket(PacketOutTitle(TitleAction.SET_ACTION_BAR, actionBar = message))
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {
        session.sendPacket(PacketOutPlayerListHeaderFooter(header, footer))
    }

    override fun showTitle(title: Title) {
        session.sendPacket(PacketOutTitle(TitleAction.SET_TITLE, title))
        session.sendPacket(PacketOutTitle(TitleAction.SET_SUBTITLE, title))
        session.sendPacket(PacketOutTitle(TitleAction.SET_TIMES_AND_DISPLAY, title))
    }

    override fun clearTitle() {
        session.sendPacket(PacketOutTitle(TitleAction.HIDE))
    }

    override fun resetTitle() {
        session.sendPacket(PacketOutTitle(TitleAction.RESET))
    }

    override fun identity() = Identity.identity(uuid)
}