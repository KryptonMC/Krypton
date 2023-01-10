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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literal
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object TitleCommand {

    private const val TARGETS = "targets"
    private const val FADE_IN = "fadeIn"
    private const val STAY = "stay"
    private const val FADE_OUT = "fadeOut"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("title") {
            requiresPermission(KryptonPermission.TITLE)
            argument(TARGETS, EntityArgumentType.players()) {
                title(this@literalCommand, "actionbar", KryptonPlayer::sendActionBar)
                title(this@literalCommand, "title", KryptonPlayer::sendTitle)
                title(this@literalCommand, "subtitle", KryptonPlayer::sendSubtitle)
                clearOrReset(this@literalCommand, "clear", "cleared", Player::clearTitle)
                clearOrReset(this@literalCommand, "reset", "reset", Player::resetTitle)
                times(this@literalCommand)
            }
        })
    }

    @JvmStatic
    private inline fun title(builder: LiteralArgumentBuilder<CommandSourceStack>, name: String,
                             crossinline action: (KryptonPlayer, Component) -> Unit): LiteralArgumentBuilder<CommandSourceStack> {
        return builder.literal(name) {
            argument("message", StringArgumentType.string()) {
                runs { context ->
                    val targets = EntityArgumentType.getPlayers(context, TARGETS)
                    val message = Component.text(context.getArgument("message", String::class.java))
                    targets.forEach { action(it, message) }
                    sendFeedback(context.source, "commands.title.show.$name", targets.size)
                }
            }
        }
    }

    @JvmStatic
    private inline fun clearOrReset(builder: LiteralArgumentBuilder<CommandSourceStack>, name: String, translationKey: String,
                                    crossinline action: (KryptonPlayer) -> Unit): LiteralArgumentBuilder<CommandSourceStack> {
        return builder.literal(name) {
            runs { context ->
                val targets = EntityArgumentType.getPlayers(context, TARGETS)
                targets.forEach(action)
                sendFeedback(context.source, "commands.title.$translationKey", targets.size)
            }
        }
    }

    @JvmStatic
    private fun times(builder: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> = builder.literal("times") {
        argument(FADE_IN, IntegerArgumentType.integer()) {
            argument(STAY, IntegerArgumentType.integer()) {
                argument(FADE_OUT, IntegerArgumentType.integer()) {
                    runs { context ->
                        val targets = EntityArgumentType.getPlayers(context, TARGETS)
                        targets.forEach { it.sendTitleTimes(context.getArgument(FADE_IN), context.getArgument(STAY), context.getArgument(FADE_OUT)) }
                        sendFeedback(context.source, "commands.title.times", targets.size)
                    }
                }
            }
        }
    }

    @JvmStatic
    private fun sendFeedback(source: CommandSourceStack, key: String, targets: Int) {
        val feedbackKey = if (targets == 1) "$key.single" else "$key.multiple"
        val feedbackArgument = if (targets == 1) source.displayName else Component.text(targets)
        source.sendSuccess(Component.translatable(feedbackKey, feedbackArgument), true)
    }
}
