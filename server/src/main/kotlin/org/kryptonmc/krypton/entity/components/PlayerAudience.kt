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

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.krypton.adventure.BossBarManager
import org.kryptonmc.krypton.adventure.toItemStack
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutCustomSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSetActionBarText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSubtitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTabListHeaderAndFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleAnimationTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.registry.KryptonRegistries
import java.util.concurrent.ThreadLocalRandom

interface PlayerAudience : NetworkPlayer {

    fun openBook(item: KryptonItemStack)

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        // TODO: Update this when Adventure updates - still a few things wrong here, the sender's name is always empty.
//        val chatType = when (type) {
//            MessageType.CHAT -> ChatTypes.CHAT
//            MessageType.SYSTEM -> ChatTypes.SYSTEM
//        }
//        sendMessage(message, MessageSignature.unsigned(), ChatSender.fromIdentity(source), chatType)
    }

    fun sendMessage(message: Component, signature: MessageSignature, sender: ChatSender, type: ChatType) {
        if (settings.chatVisibility != ChatVisibility.FULL) return
        // TODO: Fix chat (again)
//        val typeId = InternalRegistries.CHAT_TYPE.idOf(type)
//        val packet = when (type) {
//            ChatTypes.SYSTEM -> PacketOutSystemChatMessage(message, typeId)
//            ChatTypes.CHAT, ChatTypes.GAME_INFO -> PacketOutPlayerChatMessage(message, null, typeId, sender, signature)
//            else -> throw IllegalArgumentException("Chat type $type is not a message type!")
//        }
//        session.send(packet)
    }

    override fun sendActionBar(message: Component) {
        session.send(PacketOutSetActionBarText(message))
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {
        session.send(PacketOutSetTabListHeaderAndFooter(header, footer))
    }

    override fun showTitle(title: Title) {
        if (title.times() != null) session.send(PacketOutSetTitleAnimationTimes(title.times()!!))
        session.send(PacketOutSetSubtitleText(title.subtitle()))
        session.send(PacketOutSetTitleText(title.title()))
    }

    override fun <T : Any> sendTitlePart(part: TitlePart<T>, value: T) {
        val packet = when (part) {
            TitlePart.TITLE -> PacketOutSetTitleText(value as Component)
            TitlePart.SUBTITLE -> PacketOutSetSubtitleText(value as Component)
            TitlePart.TIMES -> PacketOutSetTitleAnimationTimes(value as Title.Times)
            else -> throw IllegalArgumentException("Unknown title part $part!")
        }
        session.send(packet)
    }

    fun sendTitle(title: Component) {
        session.send(PacketOutSetTitleText(title))
    }

    fun sendSubtitle(subtitle: Component) {
        session.send(PacketOutSetSubtitleText(subtitle))
    }

    fun sendTitleTimes(fadeInTicks: Int, stayTicks: Int, fadeOutTicks: Int) {
        session.send(PacketOutSetTitleAnimationTimes(fadeInTicks, stayTicks, fadeOutTicks))
    }

    override fun clearTitle() {
        session.send(PacketOutClearTitles(false))
    }

    override fun resetTitle() {
        session.send(PacketOutClearTitles(true))
    }

    override fun showBossBar(bar: BossBar) {
        BossBarManager.addBar(bar, this)
    }

    override fun hideBossBar(bar: BossBar) {
        BossBarManager.removeBar(bar, this)
    }

    override fun playSound(sound: Sound) {
        playSound(sound, location.x, location.y, location.z)
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val type = KryptonRegistries.SOUND_EVENT.get(sound.name())
        if (type != null) {
            session.send(PacketOutSoundEffect(sound, type, x, y, z))
            return
        }
        session.send(PacketOutCustomSoundEffect(sound, x, y, z))
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entity = when {
            emitter === Sound.Emitter.self() -> this
            emitter is KryptonEntity -> emitter
            else -> error("Sound emitter must be an entity or self(), was $emitter")
        }

        val event = KryptonRegistries.SOUND_EVENT.get(sound.name())
        if (event != null) {
            session.send(PacketOutEntitySoundEffect(event, sound.source(), entity.id, sound.volume(), sound.pitch()))
            return
        }
        val seed = sound.seed().orElse(ThreadLocalRandom.current().nextLong())
        session.send(PacketOutCustomSoundEffect(sound.name(), sound.source(), entity.location, sound.volume(), sound.pitch(), seed))
    }

    override fun stopSound(stop: SoundStop) {
        session.send(PacketOutStopSound(stop))
    }

    override fun openBook(book: Book) {
        openBook(book.toItemStack())
    }
}
