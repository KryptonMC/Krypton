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
