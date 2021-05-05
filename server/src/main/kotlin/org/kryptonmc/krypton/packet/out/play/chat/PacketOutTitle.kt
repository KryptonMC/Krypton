/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.packet.out.play.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeDuration
import org.kryptonmc.krypton.util.writeVarInt
import java.time.Duration

/**
 * Send a title or action bar to the client.
 *
 * @param action the action to send for this packet.
 * @param title the title to send to the player. Not used if the action is [TitleAction.SET_ACTION_BAR]
 * (also includes [TitleAction.HIDE] and [TitleAction.RESET], where no data is sent)
 * @param actionBar the action bar to send to the player. Not used if the action isn't [TitleAction.SET_ACTION_BAR]
 */
class PacketOutTitle(
    private val action: TitleAction,
    private val title: Title = Title.title(Component.empty(), Component.empty()),
    private val actionBar: Component = Component.empty()
) : PlayPacket(0x4F) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        when (action) {
            TitleAction.SET_TITLE -> buf.writeChat(title.title())
            TitleAction.SET_SUBTITLE -> buf.writeChat(title.subtitle())
            TitleAction.SET_ACTION_BAR -> buf.writeChat(actionBar)
            TitleAction.SET_TIMES_AND_DISPLAY -> {
                buf.writeDuration(title.times()?.fadeIn() ?: Duration.ZERO)
                buf.writeDuration(title.times()?.stay() ?: Duration.ZERO)
                buf.writeDuration(title.times()?.fadeOut() ?: Duration.ZERO)
            }
            else -> Unit
        }
    }
}

enum class TitleAction(val id: Int) {

    SET_TITLE(0),
    SET_SUBTITLE(1),
    SET_ACTION_BAR(2),
    SET_TIMES_AND_DISPLAY(3),
    HIDE(4),
    RESET(5)
}
