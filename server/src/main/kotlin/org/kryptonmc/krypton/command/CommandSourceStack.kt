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
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.rule.GameRuleKeys

class CommandSourceStack(
    override val sender: KryptonSender,
    override val position: Vec3d,
    val yaw: Float,
    val pitch: Float,
    override val world: KryptonWorld,
    override val textName: String,
    override val displayName: Component,
    override val server: KryptonServer,
    val entity: KryptonEntity?
) : CommandExecutionContext, CommandSuggestionProvider, Audience by sender {

    fun asChatSender(): ChatSender = entity?.asChatSender() ?: ChatSender.SYSTEM

    fun getEntityOrError(): KryptonEntity = entity ?: throw ERROR_NOT_ENTITY.create()

    override fun isPlayer(): Boolean = entity is KryptonPlayer

    fun getPlayer(): KryptonPlayer? = entity as? KryptonPlayer

    fun getPlayerOrError(): KryptonPlayer {
        if (entity is KryptonPlayer) return entity
        throw ERROR_NOT_PLAYER.create()
    }

    override fun asPlayer(): Player? = getPlayer()

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

    override fun sendSuccessMessage(message: Component) {
        sendSuccess(message, true)
    }

    override fun sendFailureMessage(message: Component) {
        sendFailure(message)
    }

    fun broadcastToAdmins(message: Component) {
        val broadcast = Component.translatable("chat.type.admin", NamedTextColor.GRAY, setOf(TextDecoration.ITALIC), displayName, message)
        if (world.gameRules().getBoolean(GameRuleKeys.SEND_COMMAND_FEEDBACK)) {
            server.players.forEach { player ->
                if (player !== this.sender && player.hasPermission(KryptonPermission.BROADCAST_ADMIN.node)) player.sendSystemMessage(broadcast)
            }
        }
        if (sender !== server.console && world.gameRules().getBoolean(GameRuleKeys.LOG_ADMIN_COMMANDS)) server.console.sendSystemMessage(broadcast)
    }

    companion object {

        private val ERROR_NOT_PLAYER = CommandExceptions.simple("permissions.requires.player")
        private val ERROR_NOT_ENTITY = CommandExceptions.simple("permissions.requires.entity")
    }
}
