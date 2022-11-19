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
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object TitleCommand {

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("title") {
            permission(KryptonPermission.TITLE)
            argument(TARGETS, EntityArgumentType.players()) {
                title("actionbar", KryptonPlayer::sendActionBar)
                title("title", KryptonPlayer::sendTitle)
                title("subtitle", KryptonPlayer::sendSubtitle)
                clearOrReset("clear", "cleared", Player::clearTitle)
                clearOrReset("reset", "reset", Player::resetTitle)
                times()
            }
        })
    }
}

private const val TARGETS = "targets"
private const val FADE_IN = "fadeIn"
private const val STAY = "stay"
private const val FADE_OUT = "fadeOut"

private inline fun LiteralArgumentBuilder<CommandSourceStack>.title(
    name: String,
    crossinline action: (KryptonPlayer, Component) -> Unit
): LiteralArgumentBuilder<CommandSourceStack> = literal(name) {
    argument("message", StringArgumentType.string()) {
        runs { context ->
            val targets = context.entityArgument(TARGETS).players(context.source)
            val message = Component.text(context.argument<String>("message"))
            targets.forEach { action(it, message) }
            context.source.sendFeedback("commands.title.show.$name", targets.size)
        }
    }
}

private inline fun LiteralArgumentBuilder<CommandSourceStack>.clearOrReset(
    name: String,
    translationKey: String,
    crossinline action: (KryptonPlayer) -> Unit
): LiteralArgumentBuilder<CommandSourceStack> = literal(name) {
    runs { context ->
        val targets = context.entityArgument(TARGETS).players(context.source)
        targets.forEach(action)
        context.source.sendFeedback("commands.title.$translationKey", targets.size)
    }
}

private fun LiteralArgumentBuilder<CommandSourceStack>.times(): LiteralArgumentBuilder<CommandSourceStack> = literal("times") {
    argument(FADE_IN, IntegerArgumentType.integer()) {
        argument(STAY, IntegerArgumentType.integer()) {
            argument(FADE_OUT, IntegerArgumentType.integer()) {
                runs { context ->
                    val targets = context.entityArgument(TARGETS).players(context.source)
                    targets.forEach { it.sendTitleTimes(context.argument(FADE_IN), context.argument(STAY), context.argument(FADE_OUT)) }
                    context.source.sendFeedback("commands.title.times", targets.size)
                }
            }
        }
    }
}

private fun CommandSourceStack.sendFeedback(key: String, targets: Int) {
    val feedbackKey = if (targets == 1) "$key.single" else "$key.multiple"
    val feedbackArgument = if (targets == 1) displayName else Component.text(targets)
    sendSuccess(Component.translatable(feedbackKey, feedbackArgument), true)
}
