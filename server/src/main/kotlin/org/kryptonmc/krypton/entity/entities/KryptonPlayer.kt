package org.kryptonmc.krypton.entity.entities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import net.kyori.adventure.title.Title
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.effect.particle.ColorParticleData
import org.kryptonmc.krypton.api.effect.particle.DirectionalParticleData
import org.kryptonmc.krypton.api.effect.particle.NoteParticleData
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.api.entity.MainHand
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkData
import org.kryptonmc.krypton.packet.out.play.PacketOutParticles
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutPlayerListHeaderFooter
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutTitle
import org.kryptonmc.krypton.packet.out.play.chat.TitleAction
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.util.canBuild
import org.kryptonmc.krypton.util.chunkInSpiral
import org.kryptonmc.krypton.util.toArea
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.net.InetSocketAddress
import java.util.Locale
import java.util.UUID
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.math.min

class KryptonPlayer(
    override val name: String,
    private val server: KryptonServer,
    val session: Session,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : Player, KryptonSender(server) {

    override lateinit var uuid: UUID

    override var displayName: Component = Component.empty()
    override lateinit var abilities: Abilities
    var attributes: Set<Attribute> = DEFAULT_PLAYER_ATTRIBUTES

    override lateinit var location: Location

    override var velocity = Vector.ZERO

    override var isOnGround = false
    override var isCrouching = false
    override var isSprinting = false

    override var viewDistance = 10
    override var time = 0L

    override lateinit var mainHand: MainHand
    var hand = Hand.MAIN

    override var scoreboard: Scoreboard? = null

    override val inventory = KryptonPlayerInventory(this)

    override var locale: Locale? = null

    var oldGamemode: Gamemode? = null
    var gamemode = Gamemode.SURVIVAL

    override lateinit var world: KryptonWorld
    override lateinit var dimension: NamespacedKey

    private var previousCentralX = 0
    private var previousCentralZ = 0
    private var hasLoadedChunks = false
    private val visibleChunks = mutableSetOf<ChunkPosition>()

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

    override fun asHoverEvent(op: UnaryOperator<ShowEntity>) =
        showEntity(ShowEntity.of(Key.key("minecraft", "player"), uuid, displayName))

    fun updateAbilities() {
        abilities = when (gamemode) {
            Gamemode.CREATIVE -> Abilities(
                isInvulnerable = true,
                canFly = true,
                canInstantlyBuild = true
            )
            Gamemode.SPECTATOR -> Abilities(
                isInvulnerable = true,
                canFly = true,
                isFlying = true,
                canInstantlyBuild = false
            )
            else -> Abilities()
        }
        abilities.canBuild = gamemode.canBuild
    }

    fun updateChunks() {
        var previousChunks: MutableSet<ChunkPosition>? = null
        val newChunks = mutableListOf<ChunkPosition>()

        val centralX = location.blockX shr 4
        val centralZ = location.blockZ shr 4
        val radius = min(server.config.world.viewDistance, 1 + viewDistance)

        if (!hasLoadedChunks) {
            hasLoadedChunks = true
            repeat(server.config.world.viewDistance.toArea()) { newChunks += chunkInSpiral(it, centralX, centralZ) }
        } else if (abs(centralX - previousCentralX) > radius || abs(centralZ - previousCentralZ) > radius) {
            visibleChunks.clear()
            repeat(server.config.world.viewDistance.toArea()) { newChunks += chunkInSpiral(it, centralX, centralZ) }
        } else if (previousCentralX != centralX || previousCentralZ != centralZ) {
            previousChunks = HashSet(visibleChunks)
            repeat(server.config.world.viewDistance.toArea()) {
                val position = chunkInSpiral(it, centralX, centralZ)
                if (position in visibleChunks) previousChunks.remove(position) else newChunks += position
            }
        } else {
            return
        }

        previousCentralX = centralX
        previousCentralZ = centralZ

        GlobalScope.launch(Dispatchers.IO) {
            val loadedChunks = world.chunkManager.load(newChunks)
            visibleChunks += newChunks

            session.sendPacket(PacketOutUpdateViewPosition(ChunkPosition(centralX, centralZ)))
            loadedChunks.forEach {
                session.sendPacket(PacketOutUpdateLight(it))
                session.sendPacket(PacketOutChunkData(it))
            }

            previousChunks?.forEach {
                session.sendPacket(PacketOutUnloadChunk(it))
                visibleChunks -= it
            }
            previousChunks?.clear()
        }
    }
}
