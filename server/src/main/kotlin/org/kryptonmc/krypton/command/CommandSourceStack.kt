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
package org.kryptonmc.krypton.command

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.world.KryptonWorld

class CommandSourceStack(
    override val sender: KryptonSender,
    override val position: Vec3d,
    val yaw: Float,
    val pitch: Float,
    override val world: KryptonWorld,
    val textName: String,
    val displayName: Component,
    override val server: KryptonServer,
    val entity: KryptonEntity?
) : CommandExecutionContext, Audience by sender {

    fun asChatSender(): ChatSender = entity?.asChatSender() ?: ChatSender.SYSTEM

    fun getEntityOrError(): KryptonEntity = entity ?: throw ERROR_NOT_ENTITY.create()

    fun isPlayer(): Boolean = entity is KryptonPlayer

    fun getPlayer(): KryptonPlayer? = entity as? KryptonPlayer

    fun getPlayerOrError(): KryptonPlayer {
        if (entity is KryptonPlayer) return entity
        throw ERROR_NOT_PLAYER.create()
    }

    /**
     * Caller must ensure that [isPlayer] is called beforehand or this may result in a ClassCastException!
     */
    fun getPlayerUnchecked(): KryptonPlayer = entity as KryptonPlayer

    fun hasPermission(permission: KryptonPermission): Boolean = sender.hasPermission(permission.node)

    fun sendSystemMessage(message: Component) {
        val player = getPlayer()
        if (player != null) player.sendSystemMessage(message) else sender.sendSystemMessage(message)
    }

    fun sendSuccess(message: Component, allowLogging: Boolean) {
        if (sender.acceptsSuccess()) sender.sendSystemMessage(message)
        if (allowLogging && sender.shouldInformAdmins()) broadcastToAdmins(message)
    }

    fun sendFailure(message: Component) {
        if (sender.acceptsFailure()) sender.sendSystemMessage(Component.text().color(NamedTextColor.RED).append(message).build())
    }

    fun broadcastToAdmins(message: Component) {
        val broadcast = Messages.CHAT_TYPE_ADMIN.build(displayName, message)
        if (world.gameRules.get(GameRules.SEND_COMMAND_FEEDBACK)) {
            server.players.forEach { player ->
                if (player !== this.sender && player.hasPermission(KryptonPermission.BROADCAST_ADMIN.node)) player.sendSystemMessage(broadcast)
            }
        }
        if (sender !== server.console && world.gameRules.get(GameRules.LOG_ADMIN_COMMANDS)) server.console.sendSystemMessage(broadcast)
    }

    companion object {

        private val ERROR_NOT_PLAYER = CommandExceptions.simple("permissions.requires.player")
        private val ERROR_NOT_ENTITY = CommandExceptions.simple("permissions.requires.entity")
    }
}
