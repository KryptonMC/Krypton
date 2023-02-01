/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.components

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.adventure.BossBarManager
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutDeleteChat
import org.kryptonmc.krypton.packet.out.play.PacketOutDisguisedChat
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSetActionBarText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSubtitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTabListHeaderAndFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleAnimationTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.world.KryptonWorld

interface PlayerAudience : Player, NetworkPlayer, KryptonSender {

    override val world: KryptonWorld

    fun openBook(item: KryptonItemStack)

    override fun deleteMessage(signature: SignedMessage.Signature) {
        connection.send(PacketOutDeleteChat(MessageSignature.Packed(MessageSignature(signature.bytes()))))
    }

    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        val message = signedMessage.unsignedContent() ?: Component.text(signedMessage.message())
        if (signedMessage.isSystem) {
            sendMessage(message, boundChatType)
            return
        }
        // TODO: Add support for signed player messages
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        connection.send(PacketOutDisguisedChat(message, RichChatType.Bound.from(boundChatType).toNetwork()))
    }

    override fun sendActionBar(message: Component) {
        connection.send(PacketOutSetActionBarText(message))
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {
        connection.send(PacketOutSetTabListHeaderAndFooter(header, footer))
    }

    override fun showTitle(title: Title) {
        title.times()?.let { connection.send(PacketOutSetTitleAnimationTimes.fromTimes(it)) }
        connection.send(PacketOutSetSubtitleText(title.subtitle()))
        connection.send(PacketOutSetTitleText(title.title()))
    }

    override fun <T : Any> sendTitlePart(part: TitlePart<T>, value: T) {
        val packet = when (part) {
            TitlePart.TITLE -> PacketOutSetTitleText(value as Component)
            TitlePart.SUBTITLE -> PacketOutSetSubtitleText(value as Component)
            TitlePart.TIMES -> PacketOutSetTitleAnimationTimes.fromTimes(value as Title.Times)
            else -> throw IllegalArgumentException("Unknown title part $part!")
        }
        connection.send(packet)
    }

    fun sendTitle(title: Component) {
        connection.send(PacketOutSetTitleText(title))
    }

    fun sendSubtitle(subtitle: Component) {
        connection.send(PacketOutSetSubtitleText(subtitle))
    }

    fun sendTitleTimes(fadeInTicks: Int, stayTicks: Int, fadeOutTicks: Int) {
        connection.send(PacketOutSetTitleAnimationTimes(fadeInTicks, stayTicks, fadeOutTicks))
    }

    override fun clearTitle() {
        connection.send(PacketOutClearTitles(false))
    }

    override fun resetTitle() {
        connection.send(PacketOutClearTitles(true))
    }

    override fun showBossBar(bar: BossBar) {
        BossBarManager.addBar(bar, this)
    }

    override fun hideBossBar(bar: BossBar) {
        BossBarManager.removeBar(bar, this)
    }

    override fun playSound(sound: Sound) {
        playSound(sound, position.x, position.y, position.z)
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val seed = sound.seed().orElseGet { world.generateSoundSeed() }
        val event = getSoundEventHolder(sound)
        connection.send(PacketOutSoundEffect.create(event, sound.source(), x, y, z, sound.volume(), sound.pitch(), seed))
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entity = when {
            emitter === Sound.Emitter.self() -> this as KryptonEntity
            emitter is KryptonEntity -> emitter
            else -> error("Sound emitter must be an entity or self(), was $emitter")
        }

        val seed = sound.seed().orElseGet { world.generateSoundSeed() }
        val event = getSoundEventHolder(sound)
        connection.send(PacketOutEntitySoundEffect.create(entity, sound, event, seed))
    }

    private fun getSoundEventHolder(sound: Sound): Holder<SoundEvent> {
        val name = sound.name()
        val event = KryptonRegistries.SOUND_EVENT.get(name)
        return if (event != null) KryptonRegistries.SOUND_EVENT.wrapAsHolder(event) else Holder.Direct(KryptonSoundEvent.createVariableRange(name))
    }

    override fun stopSound(stop: SoundStop) {
        connection.send(PacketOutStopSound.create(stop))
    }

    override fun openBook(book: Book) {
        openBook(KryptonAdventure.toItemStack(book))
    }
}
