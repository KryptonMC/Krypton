/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.command

import com.mojang.brigadier.ResultConsumer
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.OutgoingChatMessage
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.rule.GameRuleKeys

@Suppress("LongParameterList")
class CommandSourceStack private constructor(
    override val sender: KryptonSender,
    override val position: Position,
    override val world: KryptonWorld,
    override val textName: String,
    override val displayName: Component,
    override val server: KryptonServer,
    val entity: KryptonEntity?,
    private val silent: Boolean,
    private val consumer: ResultConsumer<CommandSourceStack>?,
    val signingContext: CommandSigningContext
) : CommandExecutionContext, CommandSuggestionProvider, Audience by sender {

    constructor(sender: KryptonSender, position: Position, world: KryptonWorld, textName: String, displayName: Component, server: KryptonServer,
                entity: KryptonEntity?) : this(sender, position, world, textName, displayName, server, entity, false, { _, _, _ -> },
        CommandSigningContext.ANONYMOUS)

    fun withCallback(consumer: ResultConsumer<CommandSourceStack>?): CommandSourceStack {
        if (this.consumer == consumer) return this
        return CommandSourceStack(sender, position, world, textName, displayName, server, entity, silent, consumer, signingContext)
    }

    fun withSigningContext(context: CommandSigningContext): CommandSourceStack {
        if (context === signingContext) return this
        return CommandSourceStack(sender, position, world, textName, displayName, server, entity, silent, consumer, context)
    }

    fun getEntityOrError(): KryptonEntity = entity ?: throw ERROR_NOT_ENTITY.create()

    override fun isPlayer(): Boolean = entity is KryptonPlayer

    fun getPlayer(): KryptonPlayer? = entity as? KryptonPlayer

    fun getPlayerOrError(): KryptonPlayer {
        if (entity is KryptonPlayer) return entity
        throw ERROR_NOT_PLAYER.create()
    }

    override fun asPlayer(): Player? = getPlayer()

    fun hasPermission(permission: KryptonPermission): Boolean = sender.hasPermission(permission.node)

    fun shouldFilterMessageTo(target: KryptonPlayer): Boolean {
        val player = getPlayer()
        if (target === player) return false
        return player != null && player.settings.filterText || target.settings.filterText
    }

    fun sendChatMessage(message: OutgoingChatMessage, filter: Boolean, type: RichChatType.Bound) {
        val player = getPlayer()
        if (player != null) player.sendChatMessage(message, filter, type) else sender.sendSystemMessage(type.decorate(message.content()))
    }

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

    fun onCommandComplete(context: CommandContext<CommandSourceStack>, success: Boolean, result: Int) {
        consumer?.onCommandComplete(context, success, result)
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
